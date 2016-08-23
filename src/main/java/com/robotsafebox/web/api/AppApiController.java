package com.robotsafebox.web.api;

import com.robotsafebox.base.json.JsonResult;
import com.robotsafebox.base.web.BaseAppController;
import com.robotsafebox.dao.GroupMapper;
import com.robotsafebox.entity.Group;
import com.robotsafebox.entity.GroupMember;
import com.robotsafebox.entity.User;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.framework.tools.ApiTokenTool;
import com.robotsafebox.framework.tools.CodeCheckTool;
import com.robotsafebox.framework.utils.DateUtil;
import com.robotsafebox.service.GroupMemberService;
import com.robotsafebox.service.GroupService;
import com.robotsafebox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(Constant.API_HEAD_URL)  // url:  /模块/资源/{id}细分
public class AppApiController extends BaseAppController {

    @Autowired
    protected GroupService groupService;
    @Autowired
    protected GroupMemberService groupMemberService;

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
            group.setCreateTime(new Date());
            groupService.saveGroup(group);
            Group nowGroup = groupService.getGroupByGroupName(group.getGroupName());

            //存入创始人
            GroupMember groupMember = new GroupMember();
            groupMember.setGroupId(nowGroup.getId());
            groupMember.setUserId(user.getId());
            groupMember.setType((byte) 0);
            groupMember.setJoinDate(new Date());
            groupMember.setCreateTime(new Date());
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

}
