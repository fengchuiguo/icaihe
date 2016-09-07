package com.robotsafebox.web.api;

import com.robotsafebox.base.json.JsonResult;
import com.robotsafebox.base.web.BaseAppController;
import com.robotsafebox.entity.*;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.framework.push.jpush.JPushUtils;
import com.robotsafebox.framework.tools.AgreementTool;
import com.robotsafebox.framework.utils.DateUtil;
import com.robotsafebox.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(Constant.API_HEAD_URL)  // url:  /模块/资源/{id}细分
public class AppApiController extends BaseAppController {

    @Autowired
    protected GroupService groupService;
    @Autowired
    protected GroupMemberService groupMemberService;
    @Autowired
    protected BoxService boxService;
    @Autowired
    protected BoxUserService boxUserService;
    @Autowired
    protected BoxRecordService boxRecordService;
    @Autowired
    protected SuggestionService suggestionService;
    @Autowired
    private BoxMessageService boxMessageService;

    //创建财盒群
    @RequestMapping(value = "/group/add", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult groupAdd(@ModelAttribute("group") Group group,
                               @RequestParam("creatorName") String creatorName,
                               @RequestParam("gCreateTime") String gCreateTime) {
        JsonResult jsonResult = new JsonResult();
        try {

            //todo权限，只允许创建一个？加入了就不允许创建？需求未定，接口暂时不限制。

            Group checkGroup = groupService.getGroupByGroupName(group.getGroupName());
            if (checkGroup != null) {
                jsonResult.setMessage("企业组织名称已存在！");
                return jsonResult;
            }

            //获取当前用户，修改name
            User user = getCurrentUser();
            user.setName(creatorName);
            userService.saveUser(user);

            //保存群组信息
            group.setGroupCreateTime(DateUtil.string2Date(gCreateTime, DateUtil.FORMAT_DATE));
            group.setCreateTime(DateUtil.getCurrentDateTime());
            groupService.saveGroup(group);
            Group nowGroup = groupService.getGroupByGroupName(group.getGroupName());

            //存入创始人
            GroupMember groupMember = new GroupMember();
            groupMember.setGroupId(nowGroup.getId());
            groupMember.setUserId(user.getId());
            groupMember.setType((byte) 0);
//            groupMember.setJoinDate();
            groupMember.setCreateTime(DateUtil.getCurrentDateTime());
            groupMemberService.saveGroupMember(groupMember);

            jsonResult.setData(nowGroup);
            jsonResult.setMessage("恭喜！创建财盒群成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //搜索财盒群
    @RequestMapping(value = "group/list", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult groupList(@RequestParam("groupName") String groupName) {
        JsonResult jsonResult = new JsonResult();
        try {
            List<Map> groupList = groupService.searchGroup(groupName);
            jsonResult.setData(groupList);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //加入财盒群
    @RequestMapping(value = "/group/join", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult groupJoin(Long groupId, String userName, String joinDate) {
        JsonResult jsonResult = new JsonResult();
        try {

//          todo不予许重复加入

            GroupMember groupMember = new GroupMember();
            groupMember.setGroupId(groupId);
            groupMember.setUserId(getCurrentUserId());
            groupMember.setJoinDate(DateUtil.string2Date(joinDate, DateUtil.FORMAT_DATE));
            groupMember.setType((byte) 1);
            groupMember.setCreateTime(DateUtil.getCurrentDateTime());
            groupMemberService.saveGroupMember(groupMember);

            User user = getCurrentUser();
            user.setName(userName);
            user.setUpdateTime(DateUtil.getCurrentDateTime());
            userService.saveUser(user);

//            jsonResult.setData(groupMember);
            jsonResult.setMessage("加入财盒群成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //添加财盒
    @RequestMapping(value = "/box/add", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxAdd(Box box) {
        JsonResult jsonResult = new JsonResult();
        try {
            //权限(群组创建人才可以添加财盒)
            User checkUser = userService.getCreateUserByGroupId(box.getGroupId());
            if (!checkUser.getId().equals(getCurrentUserId())) {
                jsonResult.setMessage("无操作权限！");
                return jsonResult;
            }

            if (StringUtils.isBlank(box.getIchId())) {
                jsonResult.setMessage("请输入ICHID！");
                return jsonResult;
            }
            Box checkBox = boxService.getBoxByIchId(box.getIchId());
            if (checkBox != null) {
                jsonResult.setMessage("ICHID已存在！");
                return jsonResult;
            }

            box.setCreateTime(DateUtil.getCurrentDateTime());
            boxService.saveBox(box);

            //新增记录
            BoxUser boxUser = new BoxUser();
            boxUser.setBoxId(box.getId());
            boxUser.setUserId(getCurrentUserId());
            boxUser.setType((byte) 1);
            boxUser.setCreateTime(DateUtil.getCurrentDateTime());
            boxUserService.saveBoxUser(boxUser);

            Map resultMap = new HashMap();
            resultMap.put("boxId", box.getId());
            jsonResult.setData(resultMap);
            jsonResult.setMessage("创建成功！");
            jsonResult.setStateSuccess();

            //推送消息（说明：actiontype == 1 WIFI配置成功 硬件上传出不推送，改到  此处  添加财盒成功后推送）
            if (box != null) {
                int actiontype = 1;
                String alertContent = "您的财盒“" + box.getBoxName() + "”创建成功";
                if (alertContent != null) {
                    Boolean pushOK = JPushUtils.sendPush(getCurrentUserId(), alertContent, actiontype);
                    if (pushOK) {
                        BoxMessage boxMessage = new BoxMessage();
                        boxMessage.setBoxId(box.getId());
                        boxMessage.setUserId(getCurrentUserId());
                        boxMessage.setType((byte) actiontype);
                        boxMessage.setMessage(alertContent);
                        boxMessage.setCreateTime(DateUtil.getCurrentDateTime());
                        boxMessageService.saveBoxMessage(boxMessage);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //查看财盒
    @RequestMapping(value = "/box/detail", method = RequestMethod.GET, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxDetail(Long boxId) {
        JsonResult jsonResult = new JsonResult();
        try {
            //权限
            //判断是否有开箱的权限
            List<BoxUser> boxUsers = boxUserService.searchBoxUser(boxId, (byte) 1, getCurrentUserId());
            if (boxUsers == null || boxUsers.size() < 1) {
                jsonResult.setMessage("您无开箱权限！");
                return jsonResult;
            }

            Box box = boxService.getBox(boxId);
            jsonResult.setData(box);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //开箱(type=1),外借(type=4),即借即还(type=5)
    @RequestMapping(value = "/boxRecord/add", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxRecordAdd(int type, Long boxId) {
        JsonResult jsonResult = new JsonResult();
        try {
            if (type != 1 && type != 4 && type != 5) {
                jsonResult.setMessage("参数有误！");
                return jsonResult;
            }
            //判断是否有开箱的权限
            List<BoxUser> boxUsers = boxUserService.searchBoxUser(boxId, (byte) 1, getCurrentUserId());
            if (boxUsers == null || boxUsers.size() < 1) {
                jsonResult.setMessage("您无开箱权限！");
                return jsonResult;
            }

//            外借(type=4)的时候
            String backTime = getRequest().getParameter("backTime");
            BoxRecord boxRecord = new BoxRecord();
            boxRecord.setBoxId(boxId);
            boxRecord.setUserId(getCurrentUserId());
            boxRecord.setType((byte) type);
            if (type == 4 && StringUtils.isNotBlank(backTime)) {
                boxRecord.setBackTime(DateUtil.string2Date(backTime, DateUtil.FORMAT_DATE));
            }
            boxRecord.setCreateTime(DateUtil.getCurrentDateTime());
            boxRecord.setRemark(type == 1 ? "开了财盒" : (type == 4 ? "外借" : (type == 5 ? "即借即还" : "")));
            boxRecordService.saveBoxRecord(boxRecord);

//            jsonResult.setData(boxRecord);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //开箱握手协议
    @RequestMapping(value = "/agreement/openBox/detail", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult agreementOpenBoxDetail(Long boxId, String key) {
        JsonResult jsonResult = new JsonResult();
        try {
            //判断是否有开箱的权限
            List<BoxUser> boxUsers = boxUserService.searchBoxUser(boxId, (byte) 1, getCurrentUserId());
            if (boxUsers == null || boxUsers.size() < 1) {
                jsonResult.setMessage("您无开箱权限！");
                return jsonResult;
            }
            String result = AgreementTool.getOpenBoxAgreement(key);
            jsonResult.setData(result);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }


    //授权管理列表 （通讯录暂时也用这个）
    @RequestMapping(value = "/groupMember/authority/list", method = RequestMethod.GET, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult groupMemberGroupIdBoxIdList(Long groupId, Long boxId) {
        JsonResult jsonResult = new JsonResult();
        try {
            //判断该箱子是否存在，是否属于该群组
            Box box = boxService.getBox(boxId);
            if (box == null || !groupId.equals(box.getGroupId())) {
                jsonResult.setMessage("您无该保险箱");
                return jsonResult;
            }

//            //权限(群组创建人才可以进入授权管理)（通讯录暂时也用到这里，暂时去掉此处权限）
//            User checkUser = userService.getCreateUserByGroupId(groupId);
//            if (!checkUser.getId().equals(getCurrentUserId())) {
//                jsonResult.setMessage("无操作权限！");
//                return jsonResult;
//            }

            //判断是否已经授权
            List<Map> mapList = groupMemberService.searchGroupMemberByGroupId(groupId);
            if (mapList != null && mapList.size() > 0) {
                List<BoxUser> boxUsers = boxUserService.searchBoxUser(boxId, (byte) 1, null);
                Long userId;
//                authority  (‘0’表示未授权开箱，‘1’表示已经授权开箱)
                Integer authority;
                for (Map itemMap : mapList) {
                    authority = 0;
                    userId = (Long) itemMap.get("userId");
                    for (BoxUser boxUser : boxUsers) {
                        if (boxUser.getUserId() == userId) {
                            authority = 1;
                            break;
                        }
                    }
                    itemMap.put("authority", authority);
                }
            }
            jsonResult.setData(mapList);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //授权开箱
    @RequestMapping(value = "/boxUser/add", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxUserAdd(Long boxId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        try {
            //检查当前用户是否有授权的权限
            User createUser = userService.getCreateUserByBoxId(boxId);
            if (createUser == null) {
                jsonResult.setMessage("保险箱无创建人！");
                return jsonResult;
            }
            if (!createUser.getId().equals(getCurrentUserId())) {
                jsonResult.setMessage("您无权限！");
                return jsonResult;
            }

            //新增授权
            BoxUser boxUser = new BoxUser();
            boxUser.setBoxId(boxId);
            boxUser.setUserId(userId);
            boxUser.setType((byte) 1);
            boxUser.setCreateTime(DateUtil.getCurrentDateTime());
            boxUserService.saveBoxUser(boxUser);

            //新增记录
            BoxRecord boxRecord = new BoxRecord();
            boxRecord.setBoxId(boxId);
            boxRecord.setUserId(getCurrentUserId());
            boxRecord.setType((byte) 2);
            boxRecord.setCreateTime(DateUtil.getCurrentDateTime());
            boxRecord.setRemark("授权给：" + userService.getUser(userId).getName());
            boxRecordService.saveBoxRecord(boxRecord);

//            jsonResult.setData(boxUser);
            jsonResult.setMessage("授权成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //取消开箱授权
    @RequestMapping(value = "boxUser/delete", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxUserDelete(Long boxId, Long userId) {
        JsonResult jsonResult = new JsonResult();
        try {
            //检查当前用户是否有授权的权限
            User createUser = userService.getCreateUserByBoxId(boxId);
            if (createUser == null) {
                jsonResult.setMessage("保险箱无创建人！");
                return jsonResult;
            }
            if (!createUser.getId().equals(getCurrentUserId())) {
                jsonResult.setMessage("您无权限！");
                return jsonResult;
            }

            //取消授权
            List<BoxUser> boxUsers = boxUserService.searchBoxUser(boxId, null, userId);
            for (BoxUser boxUser : boxUsers) {
                boxUserService.delteBoxUser(boxUser.getId());
            }

            //新增记录
            BoxRecord boxRecord = new BoxRecord();
            boxRecord.setBoxId(boxId);
            boxRecord.setUserId(getCurrentUserId());
            boxRecord.setType((byte) 3);
            boxRecord.setCreateTime(DateUtil.getCurrentDateTime());
            boxRecord.setRemark("取消授权给：" + userService.getUser(userId).getName());
            boxRecordService.saveBoxRecord(boxRecord);

//            jsonResult.setData();
            jsonResult.setMessage("取消成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //获取用户个人信息
    @RequestMapping(value = "/user/detail", method = RequestMethod.GET, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult userDetail() {
        JsonResult jsonResult = new JsonResult();
        try {
            Map resultMap = new LinkedHashMap();

            //用户信息
            User user = getCurrentUser();
//            resultMap.put("user", user);

//            //是否创始人
//            //创建的群组
//            List<Group> groupList0 = groupService.searchGroupByUserIdAndMemberType(user.getId(), (byte) 0);
//            if (groupList0 != null && groupList0.size() > 0) {
//                resultMap.put("createFlag", "yes");
//                resultMap.put("createGroupList", groupList0);
//            } else {
//                resultMap.put("createFlag", "no");
//                resultMap.put("createGroupList", null);
//            }
//
//            //是否是成员
//            //所属的群组
//            List<Group> groupList1 = groupService.searchGroupByUserIdAndMemberType(user.getId(), (byte) 1);
//            if (groupList1 != null && groupList1.size() > 0) {
//                resultMap.put("memberFlag", "yes");
//                resultMap.put("memberGroupList", groupList1);
//            } else {
//                resultMap.put("memberFlag", "no");
//                resultMap.put("memberGroupList", null);
//            }

            //用户信息
            resultMap.put("userId", user.getId());
            resultMap.put("name", user.getName());
            resultMap.put("phone", user.getPhone());

            Long groupId = null;
            String companyName = null;
            Long boxId = null;

            Boolean isNewUser = true;
            //是否创始人
            //创建的群组
            List<Group> groupList0 = groupService.searchGroupByUserIdAndMemberType(user.getId(), (byte) 0);
            if (groupList0 != null && groupList0.size() > 0) {
                isNewUser = false;
                groupId = groupList0.get(0).getId();
                companyName = groupList0.get(0).getGroupName();
            } else {
                //是否是成员
                //所属的群组
                List<Group> groupList1 = groupService.searchGroupByUserIdAndMemberType(user.getId(), (byte) 1);
                if (groupList1 != null && groupList1.size() > 0) {
                    isNewUser = false;
                    groupId = groupList1.get(0).getId();
                    companyName = groupList1.get(0).getGroupName();
                }
            }

            //判断是否有开箱的权限
            List<BoxUser> boxUsers = boxUserService.searchBoxUser(boxId, (byte) 1, user.getId());
            if (boxUsers != null && boxUsers.size() > 0) {
                boxId = boxUsers.get(0).getBoxId();
            }

            //isNewUser用户标识
            resultMap.put("isNewUser", isNewUser);

            resultMap.put("groupId", groupId);
            resultMap.put("companyName", companyName);
            resultMap.put("boxId", boxId);

            jsonResult.setData(resultMap);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //用户动态
    @RequestMapping(value = "/boxRecord/userRecord/list", method = RequestMethod.GET, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxRecordUserRecordList() {
        JsonResult jsonResult = new JsonResult();
        try {
            List<Map> mapList = boxRecordService.searchUserRecord(getCurrentUserId());
            //时间戳转换为时间
            for (Map map : mapList) {
                map.put("createTime", DateUtil.formatDateTime((Date) map.get("createTime"), DateUtil.FORMAT_DATETIME));
            }
            jsonResult.setData(mapList);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //查看开箱记录
    @RequestMapping(value = "/boxRecord/openRecord/list", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxRecordOpenRecordList(Long boxId, String userName) {
        JsonResult jsonResult = new JsonResult();
        try {
            //（todo权限）
            List<Map> mapList = boxRecordService.searchOpenRecord(boxId, userName);
            //时间戳转换为时间
            for (Map map : mapList) {
                map.put("createTime", DateUtil.formatDateTime((Date) map.get("createTime"), DateUtil.FORMAT_DATETIME));
            }
            jsonResult.setData(mapList);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //查看群成员
    @RequestMapping(value = "/groupMember/group/list", method = RequestMethod.GET, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult groupMemberList() {
        JsonResult jsonResult = new JsonResult();
        try {
            //（todo权限）
            Long groupId = null;
            //创建的群组
            List<Group> groupList0 = groupService.searchGroupByUserIdAndMemberType(getCurrentUserId(), (byte) 0);
            if (groupList0 != null && groupList0.size() > 0) {
                groupId = groupList0.get(0).getId();
            } else {
                //是否是成员
                //所属的群组
                List<Group> groupList1 = groupService.searchGroupByUserIdAndMemberType(getCurrentUserId(), (byte) 1);
                if (groupList1 != null && groupList1.size() > 0) {
                    groupId = groupList1.get(0).getId();
                }
            }
            if (groupId == null) {
                jsonResult.setMessage("您尚未创建或加入财盒群！");
                return jsonResult;
            }

            Group group = groupService.getGroup(groupId);

            Map result = new LinkedHashMap();
            result.put("groupId", group.getId());
            result.put("groupName", group.getGroupName());
            result.put("createTime", DateUtil.formatDateTime(group.getCreateTime(), DateUtil.FORMAT_DATETIME));
            List<Map> mapList = groupMemberService.searchGroupMemberByGroupId(groupId);
            result.put("memberList", mapList);

            jsonResult.setData(result);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }


    //意见反馈
    @RequestMapping(value = "message/add", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult messageAdd(String message) {
        JsonResult jsonResult = new JsonResult();
        try {

            Suggestion suggestion = new Suggestion();
            suggestion.setUserId(getCurrentUserId());
            suggestion.setMessage(message);
            suggestion.setCreateTime(DateUtil.getCurrentDateTime());
            suggestionService.saveSuggestion(suggestion);

//            jsonResult.setData(suggestion);
            jsonResult.setMessage("反馈成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

}
