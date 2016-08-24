package com.robotsafebox.web.api;

import com.robotsafebox.base.json.JsonResult;
import com.robotsafebox.base.web.BaseAppController;
import com.robotsafebox.entity.*;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.framework.utils.DateUtil;
import com.robotsafebox.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    //创建财盒群
    @RequestMapping(value = "/group/add", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult groupAdd(@ModelAttribute("group") Group group,
                               @RequestParam("creatorName") String creatorName,
                               @RequestParam("gCreateTime") String gCreateTime) {
        JsonResult jsonResult = new JsonResult();
        try {

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
            List<Group> groupList = groupService.searchGroup(groupName);
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

            jsonResult.setData(groupMember);
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
            box.setCreateTime(DateUtil.getCurrentDateTime());
            boxService.saveBox(box);

            //新增记录
            BoxUser boxUser = new BoxUser();
            boxUser.setBoxId(box.getId());
            boxUser.setUserId(getCurrentUserId());
            boxUser.setType((byte) 1);
            boxUser.setCreateTime(DateUtil.getCurrentDateTime());
            boxUserService.saveBoxUser(boxUser);

            jsonResult.setData(box);
            jsonResult.setMessage("创建成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    //查看财盒
    @RequestMapping(value = "/box/{id}/detail", method = RequestMethod.GET, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxDetail(@PathVariable("id") Long id) {
        JsonResult jsonResult = new JsonResult();
        try {
            Box box = boxService.getBox(id);
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
    @RequestMapping(value = "/boxRecord/{type}/{boxId}/add", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult boxRecordAdd(@PathVariable("type") int type, @PathVariable("boxId") Long boxId) {
        JsonResult jsonResult = new JsonResult();
        try {
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
            boxRecordService.saveBoxRecord(boxRecord);

            jsonResult.setData(boxRecord);
            jsonResult.setMessage(Constant.SUCCESS_MESSAGE);
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

//    //开箱握手协议
//    @RequestMapping(value = "xxx/xxx", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
//    @ResponseBody
//    public JsonResult xxxx(String xxxx) {
//        JsonResult jsonResult = new JsonResult();
//        try {
////          todo：xxx
//
////            jsonResult.setData(xxx);
////            jsonResult.setMessage("xxxx");
//            jsonResult.setStateSuccess();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
//        }
//        return jsonResult;
//    }


//    //授权管理列表
//    @RequestMapping(value = "xxx/xxx", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
//    @ResponseBody
//    public JsonResult xxxx(String xxxx) {
//        JsonResult jsonResult = new JsonResult();
//        try {
////          todo：xxx
//
////            jsonResult.setData(xxx);
////            jsonResult.setMessage("xxxx");
//            jsonResult.setStateSuccess();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
//        }
//        return jsonResult;
//    }

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

            //新增记录
            BoxUser boxUser = new BoxUser();
            boxUser.setBoxId(boxId);
            boxUser.setUserId(userId);
            boxUser.setType((byte) 1);
            boxUser.setCreateTime(DateUtil.getCurrentDateTime());
            boxUserService.saveBoxUser(boxUser);

            jsonResult.setData(boxUser);
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
            //删除记录
            boxUserService.delteBoxUser(userId);

//            jsonResult.setData();
            jsonResult.setMessage("取消成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

//    //获取用户个人信息
//    @RequestMapping(value = "xxx/xxx", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
//    @ResponseBody
//    public JsonResult xxxx(String xxxx) {
//        JsonResult jsonResult = new JsonResult();
//        try {
////          todo：xxx
//
////            jsonResult.setData(xxx);
////            jsonResult.setMessage("xxxx");
//            jsonResult.setStateSuccess();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
//        }
//        return jsonResult;
//    }

//    //用户动态
//    @RequestMapping(value = "xxx/xxx", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
//    @ResponseBody
//    public JsonResult xxxx(String xxxx) {
//        JsonResult jsonResult = new JsonResult();
//        try {
////          todo：xxx
//
////            jsonResult.setData(xxx);
////            jsonResult.setMessage("xxxx");
//            jsonResult.setStateSuccess();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
//        }
//        return jsonResult;
//    }

//    //查看开箱记录
//    @RequestMapping(value = "/boxRecord/openRecord/list", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
//    @ResponseBody
//    public JsonResult xxxx(String xxxx) {
//        JsonResult jsonResult = new JsonResult();
//        try {
////          todo：xxx
//
////            jsonResult.setData(xxx);
////            jsonResult.setMessage("xxxx");
//            jsonResult.setStateSuccess();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
//        }
//        return jsonResult;
//    }

    //查看群成员
    @RequestMapping(value = "/groupMember/{groupId}/list", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult groupMemberList(@PathVariable("groupId") Long groupId) {
        JsonResult jsonResult = new JsonResult();
        try {
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

            jsonResult.setData(suggestion);
            jsonResult.setMessage("反馈成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

}
