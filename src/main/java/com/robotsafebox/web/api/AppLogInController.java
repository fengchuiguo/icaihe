package com.robotsafebox.web.api;

import com.robotsafebox.base.json.JsonResult;
import com.robotsafebox.base.web.BaseAppController;
import com.robotsafebox.entity.User;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.framework.sms.SmsSendUtils;
import com.robotsafebox.framework.tools.ApiTokenTool;
import com.robotsafebox.framework.tools.CodeCheckTool;
import com.robotsafebox.framework.utils.RandomUtil;
import com.robotsafebox.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;


@Controller
@RequestMapping("/initApi/")  // url:  /模块/资源/{id}细分
public class AppLogInController extends BaseAppController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

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

            //todo 登录

            Object smscode = httpSession.getAttribute(CodeCheckTool.SMS_CODE);
            if (smscode == null) {
                jsonResult.setMessage("请重新获取验证码！");
                return jsonResult;
            }
            if (CodeCheckTool.checkSmsCodeFailure(phone, code, "1", smscode.toString())) {
                jsonResult.setMessage("验证码错误！");
                return jsonResult;
            }

            //用户不存在的话，注册新用户
            User checkUser = userService.getUser(phone);
            if (checkUser == null) {
                User newUser = new User();
                newUser.setPhone(phone);
                newUser.setCreateTime(new Date());
                userService.addUser(newUser);
            }
            User nowUser = userService.getUser(phone);
            String token = ApiTokenTool.getToken(nowUser.getId().toString());
            jsonResult.setData(token);
            jsonResult.setMessage("登录成功！");
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
