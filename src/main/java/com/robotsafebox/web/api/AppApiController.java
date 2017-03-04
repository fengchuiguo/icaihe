package com.robotsafebox.web.api;

import com.robotsafebox.base.json.JsonResult;
import com.robotsafebox.base.web.BaseAppController;
import com.robotsafebox.entity.*;
import com.robotsafebox.framework.model.Pager;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.framework.push.PushTask.PushTaskTool;
import com.robotsafebox.framework.push.jpush.JPushUtils;
import com.robotsafebox.framework.tools.AgreementTool;
import com.robotsafebox.framework.tools.SignInTool;
import com.robotsafebox.framework.utils.DateUtil;
import com.robotsafebox.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(Constant.API_HEAD_URL)  // url:  /模块/资源/{id}细分
@Scope("prototype")
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
    @Autowired
    private HardwareReportLogService hardwareReportLogService;
    @Autowired
    private SignInService signInService;

    //创建财盒群
    @RequestMapping(value = "/group/add", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult groupAdd(@ModelAttribute("group") Group group,
                               @RequestParam("creatorName") String creatorName,
                               @RequestParam("gCreateTime") String gCreateTime) {
        JsonResult jsonResult = new JsonResult();
        try {

            //不予许重复加入
            //所属的群组
            List<Group> groupList = groupService.searchGroupByUserIdAndMemberType(getCurrentUserId(), null);
            if (groupList != null && groupList.size() > 0) {
                jsonResult.setMessage("只允许加入一个群组!");
                return jsonResult;
            }

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
    @RequestMapping(value = "/group/list", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
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

            //不予许重复加入
            //所属的群组
            List<Group> groupList = groupService.searchGroupByUserIdAndMemberType(getCurrentUserId(), null);
            if (groupList != null && groupList.size() > 0) {
                jsonResult.setMessage("只允许加入一个群组!");
                return jsonResult;
            }

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
            if (checkUser == null || !getCurrentUserId().equals(checkUser.getId())) {
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

    //修改财盒wifiid
    @RequestMapping(value = "/box/wifiId/update", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxWifiIdUpdate(Long boxId, String wifiId) {
        JsonResult jsonResult = new JsonResult();
        try {
            Box box = boxService.getBox(boxId);
            if (box == null) {
                jsonResult.setMessage("保险箱不存在！");
                return jsonResult;
            }
            //权限(保险箱创建人才可以修改)
            User checkUser = userService.getCreateUserByBoxId(box.getId());
            if (!checkUser.getId().equals(getCurrentUserId())) {
                jsonResult.setMessage("无操作权限！");
                return jsonResult;
            }
            box.setWifiId(wifiId);
            box.setUpdateTime(DateUtil.getCurrentDateTime());
            boxService.saveBox(box);

            jsonResult.setMessage("修改成功!");
            jsonResult.setStateSuccess();
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
    public JsonResult boxRecordAdd(int type, Long boxId, @RequestParam(required = false) String remark) {
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
            if (StringUtils.isNotBlank(remark)) {
                if (type == 4) {
                    remark = "外借:" + remark;
                }
                if (type == 5) {
                    remark = "即借即还:" + remark;
                }
                boxRecord.setRemark(remark);
            } else {
                boxRecord.setRemark(type == 1 ? "开了财盒" : (type == 4 ? "外借" : (type == 5 ? "即借即还" : "")));
            }
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
    public JsonResult agreementOpenBoxDetail(String ichId, String key) {
        JsonResult jsonResult = new JsonResult();
        try {
            Box box = boxService.getBoxByIchId(ichId);
            if (box == null) {
                jsonResult.setMessage("保险箱不存在！");
                return jsonResult;
            }
            //判断是否有开箱的权限
            List<BoxUser> boxUsers = boxUserService.searchBoxUser(box.getId(), (byte) 1, getCurrentUserId());
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


    //授权管理列表
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

            //权限(群组创建人才可以进入授权管理)
            User checkUser = userService.getCreateUserByGroupId(groupId);
            if (!checkUser.getId().equals(getCurrentUserId())) {
                jsonResult.setMessage("无操作权限！");
                return jsonResult;
            }

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


    //通讯录
    @RequestMapping(value = "/groupMember/list", method = RequestMethod.GET, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult groupMemberList(Long groupId) {
        JsonResult jsonResult = new JsonResult();
        try {
            //判断当前用户是否属于该群组
            GroupMember groupMember = groupMemberService.getGroupMemberByGroupIdAndUserId(groupId, getCurrentUserId());
            if (groupMember == null) {
                jsonResult.setMessage("您不属于该群组！");
                return jsonResult;
            }
            List<Map> mapList = groupMemberService.searchGroupMemberByGroupId(groupId);
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
    @RequestMapping(value = "/boxUser/delete", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
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
            resultMap.put("alarmNum", user.getAlarmNum() == null ? 0 : user.getAlarmNum());

            Long groupId = null;
            String companyName = "";
            String wifiId = "";
            Long boxId = null;

            Boolean isNewUser = true;
            Boolean isGroupCreator = false;
            //是否创始人
            //创建的群组
            List<Group> groupList0 = groupService.searchGroupByUserIdAndMemberType(user.getId(), (byte) 0);
            if (groupList0 != null && groupList0.size() > 0) {
                isNewUser = false;
                groupId = groupList0.get(0).getId();
                companyName = groupList0.get(0).getGroupName();
                isGroupCreator = true;
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

            if (boxId != null) {
                Box box = boxService.getBox(boxId);
                if (box != null && StringUtils.isNotBlank(box.getWifiId())) {
                    wifiId = box.getWifiId();
                }
            }
            resultMap.put("wifiId", wifiId);

            resultMap.put("isGroupCreator", isGroupCreator);


            jsonResult.setData(resultMap);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

//    //用户动态
//    @RequestMapping(value = "/boxRecord/userRecord/list", method = RequestMethod.GET, produces = {Constant.CONTENT_TYPE_JSON})
//    @ResponseBody
//    public JsonResult boxRecordUserRecordList() {
//        JsonResult jsonResult = new JsonResult();
//        try {
//            List<Map> mapList = boxRecordService.searchUserRecord(getCurrentUserId());
//            //时间戳转换为时间
//            for (Map map : mapList) {
//                map.put("createTime", DateUtil.formatDateTime((Date) map.get("createTime"), DateUtil.FORMAT_DATETIME));
//            }
//            jsonResult.setData(mapList);
//            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
//            jsonResult.setStateSuccess();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
//        }
//        return jsonResult;
//    }

    //用户动态(包含分页)
    @RequestMapping(value = "/boxRecord/userRecord/list", method = RequestMethod.GET, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxRecordUserRecordList(Integer pageNo) {
        JsonResult jsonResult = new JsonResult();
        try {
            Pager pager = new Pager();
            pager.setPageNo(pageNo == null ? 1 : pageNo);

            Map paramMap = new HashMap();
            paramMap.put("userId", getCurrentUserId());
            paramMap.put("pager", pager);

            List<Map> mapList = boxRecordService.searchUserRecordByMap(paramMap);
            //时间戳转换为时间
            for (Map map : mapList) {
                map.put("createTime", DateUtil.formatDateTime((Date) map.get("createTime"), DateUtil.FORMAT_DATETIME));
            }
            pager.setResults(mapList);

            jsonResult.setData(pager);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

//    //查看开箱记录
//    @RequestMapping(value = "/boxRecord/openRecord/list", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
//    @ResponseBody
//    public JsonResult boxRecordOpenRecordList(Long boxId, String userName) {
//        JsonResult jsonResult = new JsonResult();
//        try {
//            List<Map> mapList = boxRecordService.searchOpenRecord(boxId, userName);
//            //时间戳转换为时间
//            for (Map map : mapList) {
//                map.put("createTime", DateUtil.formatDateTime((Date) map.get("createTime"), DateUtil.FORMAT_DATETIME));
//            }
//            jsonResult.setData(mapList);
//            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
//            jsonResult.setStateSuccess();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
//        }
//        return jsonResult;
//    }

    //查看开箱记录(包含分页)
    @RequestMapping(value = "/boxRecord/openRecord/list", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxRecordOpenRecordList(Long boxId, String userName, Integer pageNo) {
        JsonResult jsonResult = new JsonResult();
        try {
            Pager pager = new Pager();
            pager.setPageNo(pageNo == null ? 1 : pageNo);

            Map paramMap = new HashMap();
            paramMap.put("boxId", boxId);
            paramMap.put("userName", userName);
            paramMap.put("pager", pager);
            List<Map> mapList = boxRecordService.searchOpenRecordByMap(paramMap);
            //时间戳转换为时间
            for (Map map : mapList) {
                map.put("createTime", DateUtil.formatDateTime((Date) map.get("createTime"), DateUtil.FORMAT_DATETIME));
            }
            pager.setResults(mapList);

            jsonResult.setData(pager);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //查看报警记录（保险箱的报警和电量不足）(包含分页)
    @RequestMapping(value = "/boxRecord/alarmRecord/list", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxRecordAlarmRecordList(Long boxId, Integer pageNo) {
        JsonResult jsonResult = new JsonResult();
        try {
            Pager pager = new Pager();
            pager.setPageNo(pageNo == null ? 1 : pageNo);

            Map paramMap = new HashMap();
            paramMap.put("boxId", boxId);
            paramMap.put("pager", pager);
            List<Map> mapList = boxRecordService.searchAlarmRecordByMap(paramMap);
            //时间戳转换为时间
            for (Map map : mapList) {
                map.put("createTime", DateUtil.formatDateTime((Date) map.get("createTime"), DateUtil.FORMAT_DATETIME));
            }
            pager.setResults(mapList);

            jsonResult.setData(pager);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }


    //未读报警记录条数清零
    @RequestMapping(value = "/alarmNum/delete", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult alarmNumDelete() {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = getCurrentUser();
            user.setAlarmNum(0);
            userService.saveUser(user);

            jsonResult.setMessage("清零成功！");
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
    @RequestMapping(value = "/message/add", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
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


    //获取硬件上报记录（全部类型，包含分页）
    @RequestMapping(value = "/reportLog/list", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult reportLogList(Long boxId, Integer pageNo) {
        JsonResult jsonResult = new JsonResult();
        try {
            Box box = boxService.getBox(boxId);
            if (box == null) {
                jsonResult.setMessage("财盒不存在！");
                return jsonResult;
            }
            //判断是否有开箱的权限
            List<BoxUser> boxUsers = boxUserService.searchBoxUser(boxId, (byte) 1, getCurrentUserId());
            if (boxUsers == null || boxUsers.size() < 1) {
                jsonResult.setMessage("您无权限！");
                return jsonResult;
            }
            Pager pager = new Pager();
            pager.setPageNo(pageNo == null ? 1 : pageNo);

            Map paramMap = new HashMap();
            paramMap.put("ichId", box.getIchId());
            paramMap.put("pager", pager);
            List<HardwareReportLog> mapList = hardwareReportLogService.selectRecordByMap(paramMap);
            pager.setResults(mapList);

            jsonResult.setData(pager);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //获取硬件上报记录（仅包含硬件上报的开箱记录）
    @RequestMapping(value = "/reportLog/openRecord/list", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult reportLogOpenRecordList(Long boxId, Integer pageNo) {
        JsonResult jsonResult = new JsonResult();
        try {
            Box box = boxService.getBox(boxId);
            if (box == null) {
                jsonResult.setMessage("财盒不存在！");
                return jsonResult;
            }
            //判断是否有开箱的权限
            List<BoxUser> boxUsers = boxUserService.searchBoxUser(boxId, (byte) 1, getCurrentUserId());
            if (boxUsers == null || boxUsers.size() < 1) {
                jsonResult.setMessage("您无权限！");
                return jsonResult;
            }
            Pager pager = new Pager();
            pager.setPageNo(pageNo == null ? 1 : pageNo);

            Map paramMap = new HashMap();
            paramMap.put("ichId", box.getIchId());
            paramMap.put("actionType", 4);
            paramMap.put("pager", pager);
            List<HardwareReportLog> mapList = hardwareReportLogService.selectRecordByMap(paramMap);
            pager.setResults(mapList);

            jsonResult.setData(pager);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }


    //打卡签到
    @RequestMapping(value = "/signIn/add", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult signInAdd(@ModelAttribute("signIn") SignIn signIn) {
        JsonResult jsonResult = new JsonResult();
        try {
//          类型（1：上班打卡，2：下班打卡，3：外出）
            if (signIn.getType() == null) {
                jsonResult.setMessage("请选择打卡类型！");
                return jsonResult;
            }
            if (signIn.getType() < 1 || signIn.getType() > 3) {
                jsonResult.setMessage("请选择正确的打卡类型！");
                return jsonResult;
            }

            User user = getCurrentUser();
            signIn.setUserId(user.getId());
            //获取当前用户的群组和群组绑定的财盒
            Group group;
            Box box;
            //所属的群组
            List<Group> groupList = groupService.searchGroupByUserIdAndMemberType(user.getId(), null);
            if (groupList != null && groupList.size() > 0) {
                group = groupList.get(0);
                box = boxService.getBoxByGroupId(group.getId());
                if (box != null) {
                    signIn.setBoxId(box.getId());
                }
            } else {
                jsonResult.setMessage("请先加入群组！");
                return jsonResult;
            }

            //打卡方式（1财盒打卡。2坐标打卡）
            if (StringUtils.isNotBlank(signIn.getMajor()) && StringUtils.isNotBlank(signIn.getMinor())) {
                //1财盒打卡
                if (box == null) {
                    jsonResult.setMessage("您的群组没有绑定财盒！");
                    return jsonResult;
                }
                //根据Major和Minor解析出ichid
                String checkIchId = SignInTool.getIchId(signIn.getMajor(), signIn.getMinor());
                if (StringUtils.isBlank(checkIchId)) {
                    jsonResult.setMessage("转化ichId错误！");
                    return jsonResult;
                }
                if (!checkIchId.equals(box.getIchId())) {
                    jsonResult.setMessage("该财盒不属于您的群组！");
                    return jsonResult;
                }
                signIn.setFlag(1);
                signIn.setIchId(checkIchId);
            } else {
                if (StringUtils.isNotBlank(signIn.getAddressX()) && StringUtils.isNotBlank(signIn.getAddressY())) {
                    //2坐标打卡
                    if (StringUtils.isBlank(group.getAddressX()) || StringUtils.isBlank(group.getAddressY())) {
                        jsonResult.setMessage("所属群组的坐标信息不完整！");
                        return jsonResult;
                    }
                    if (SignInTool.checkDistanceOK(group.getAddressX(), group.getAddressY(), signIn.getAddressX(), signIn.getAddressY())) {
                        signIn.setFlag(2);
                        signIn.setDistance("" + SignInTool.getDistance(group.getAddressX(), group.getAddressY(), signIn.getAddressX(), signIn.getAddressY()));
                    } else {
                        jsonResult.setMessage("超出打卡范围！");
                        return jsonResult;
                    }
                } else {
                    jsonResult.setMessage("传递参数不完整！");
                    return jsonResult;
                }
            }

            //保存打卡签到记录
            signIn.setCreateTime(DateUtil.getCurrentDateTime());
            signIn.setUpdateTime(DateUtil.getCurrentDateTime());
            signInService.saveSignIn(signIn);

//            jsonResult.setData();
            jsonResult.setMessage("操作成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //搜索当前登录人的打卡记录。日期字符串格式（yyyy-mm-dd）
    @RequestMapping(value = "/signIn/list", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult signInList(@RequestParam("searchDate") String searchDate) {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = getCurrentUser();
            List<Map> mapList = signInService.searchSignInByUserIdAndDate(user.getId(), searchDate);
            jsonResult.setData(mapList);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }


    //注销财盒
    @RequestMapping(value = "/box/writtenOff", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxWrittenOff(@RequestParam("boxId") Long boxId) {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = getCurrentUser();
            //权限(群组创建人才可以注销财盒)
            Box box = boxService.getBox(boxId);
            if (box == null) {
                jsonResult.setMessage("财盒不存在！");
                return jsonResult;
            }
            User checkUser = userService.getCreateUserByGroupId(box.getGroupId());
            if (!checkUser.getId().equals(user.getId())) {
                jsonResult.setMessage("无操作权限！");
                return jsonResult;
            }
            //删除保险箱box删除保险箱用户表boxUser
            boxService.deleteBox(box.getId());

            //未读报警记录条数清零
            user.setAlarmNum(0);
            userService.saveUser(user);

//            jsonResult.setData();
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }


    //注销群组
    @RequestMapping(value = "/group/writtenOff", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult groupWrittenOff(@RequestParam("groupId") Long groupId) {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = getCurrentUser();
            User checkUser = userService.getCreateUserByGroupId(groupId);
            if (!checkUser.getId().equals(user.getId())) {
                jsonResult.setMessage("无操作权限！");
                return jsonResult;
            }
            //删除保险箱box删除保险箱用户表boxUser
            groupService.groupWrittenOff(groupId, user.getId());
            //未读报警记录条数清零
            user.setAlarmNum(0);
            userService.saveUser(user);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //退出群组
    @RequestMapping(value = "/group/quit", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult groupQuit(@RequestParam("groupId") Long groupId) {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = getCurrentUser();
            //权限(群组成员才可以退出群组)
            //是否是成员
            //所属的群组
            List<Group> groupList1 = groupService.searchGroupByUserIdAndMemberType(user.getId(), (byte) 1);
            if (groupList1 != null && groupList1.size() > 0) {
                if (groupList1.get(0).getId() != groupId) {
                    jsonResult.setMessage("您不属于该群组！");
                    return jsonResult;
                }
            } else {
                jsonResult.setMessage("您尚未加入任何群组！");
                return jsonResult;
            }
            //删除
            groupService.groupQuit(groupId, user.getId());
            //未读报警记录条数清零
            user.setAlarmNum(0);
            userService.saveUser(user);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }


    //取消报警继续推送（财盒一次报警时候，服务器会推送三次。调用此接口后，比如已经报警2次，可以取消后续第3次推送）
    @RequestMapping(value = "/alertPush/stop", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult alertPushStop() {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = getCurrentUser();
            PushTaskTool.removeTask(user.getId(),2);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

}
