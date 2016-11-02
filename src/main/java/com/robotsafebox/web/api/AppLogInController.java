package com.robotsafebox.web.api;

import com.robotsafebox.base.json.JsonResult;
import com.robotsafebox.base.web.BaseAppController;
import com.robotsafebox.entity.Box;
import com.robotsafebox.entity.BoxUser;
import com.robotsafebox.entity.Group;
import com.robotsafebox.entity.User;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.framework.sms.SmsSendUtils;
import com.robotsafebox.framework.tools.ApiTokenTool;
import com.robotsafebox.framework.tools.CodeCheckTool;
import com.robotsafebox.framework.utils.RandomUtil;
import com.robotsafebox.service.BoxService;
import com.robotsafebox.service.BoxUserService;
import com.robotsafebox.service.GroupService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;


@Controller
@RequestMapping("/initApi")  // url:  /模块/资源/{id}细分
public class AppLogInController extends BaseAppController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    protected GroupService groupService;

    @Autowired
    protected BoxUserService boxUserService;

    @Autowired
    protected BoxService boxService;

    /**
     * 此接口暂时无用
     *
     * @return
     */
    @RequestMapping(value = "/init", method = RequestMethod.GET, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult init() {
        JsonResult jsonResult = new JsonResult();
        try {
//            String token = ApiTokenTool.getToken("156");
//            jsonResult.setData(token);
            jsonResult.setMessage("获取数据成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    /**
     * 登录
     *
     * @param phone
     * @param code
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult login(@RequestParam("phone") String phone, @RequestParam("code") String code, HttpSession httpSession) {
        JsonResult jsonResult = new JsonResult();
        try {

//            //todo start-测试时候，注释掉，发布前需要注释回来
//            Object smscode = httpSession.getAttribute(CodeCheckTool.SMS_CODE);
//            if (smscode == null) {
//                jsonResult.setMessage("请重新获取验证码！");
//                return jsonResult;
//            }
//            if (CodeCheckTool.checkSmsCodeFailure(phone, code, "1", smscode.toString())) {
//                jsonResult.setMessage("验证码错误！");
//                return jsonResult;
//            }
//            //todo end--测试时候，注释掉，发布前需要注释回来

            String userFlag = "old";
            //用户不存在的话，注册新用户
            User checkUser = userService.getUser(phone);
            User newUser = new User();
            if (checkUser == null) {
                newUser.setPhone(phone);
                newUser.setCreateTime(new Date());
                userService.saveUser(newUser);
                userFlag = "new";
            } else {
                newUser = checkUser;
            }
            String token = ApiTokenTool.getToken(newUser.getId().toString());

            httpSession.removeAttribute(CodeCheckTool.SMS_CODE);

            Map resultMap = new LinkedHashMap();
            //token
            resultMap.put("token", token);
            //用户信息
            resultMap.put("userId", newUser.getId());
            resultMap.put("name", newUser.getName());
            resultMap.put("phone", newUser.getPhone());
            resultMap.put("alarmNum", newUser.getAlarmNum() == null ? 0 : newUser.getAlarmNum());


            Long groupId = null;
            String companyName = "";
            Long boxId = null;
            String wifiId = "";

            Boolean isNewUser = true;
            //是否创始人
            //创建的群组
            List<Group> groupList0 = groupService.searchGroupByUserIdAndMemberType(newUser.getId(), (byte) 0);
            if (groupList0 != null && groupList0.size() > 0) {
                isNewUser = false;
                groupId = groupList0.get(0).getId();
                companyName = groupList0.get(0).getGroupName();
            } else {
                //是否是成员
                //所属的群组
                List<Group> groupList1 = groupService.searchGroupByUserIdAndMemberType(newUser.getId(), (byte) 1);
                if (groupList1 != null && groupList1.size() > 0) {
                    isNewUser = false;
                    groupId = groupList1.get(0).getId();
                    companyName = groupList1.get(0).getGroupName();
                }
            }

            //判断是否有开箱的权限
            List<BoxUser> boxUsers = boxUserService.searchBoxUser(boxId, (byte) 1, newUser.getId());
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

            jsonResult.setData(resultMap);
            jsonResult.setMessage(userFlag.equals("old") ? "登录成功！" : "注册成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult sendCode(@RequestParam("phone") String phone, @RequestParam("type") String type, HttpSession httpSession) {
        JsonResult jsonResult = new JsonResult();
        try {
            String code = RandomUtil.getRandom(4);
            String codeSession = CodeCheckTool.saveSmsCode(phone, code, type);
            httpSession.setAttribute(CodeCheckTool.SMS_CODE, codeSession);
            SmsSendUtils.sendSms(phone, code, "");
            jsonResult.setMessage("发送验证码成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }


}
