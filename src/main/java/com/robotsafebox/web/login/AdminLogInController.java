package com.robotsafebox.web.login;

import com.robotsafebox.base.json.JsonResult;
import com.robotsafebox.entity.AdminUser;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.framework.tools.PasswordTool;
import com.robotsafebox.service.AdminUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class AdminLogInController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminUserService adminUserService;


    @RequestMapping(value = "/adminLogin", method = RequestMethod.GET)
    public String adminLogin() {

        return "admin/adminLogin";
    }

    @RequestMapping(value = "/adminLogOut", method = RequestMethod.GET)
    public String adminLogOut(HttpSession httpSession) {
        if (httpSession.getAttribute(Constant.ADMIN_USER) != null) {
            httpSession.removeAttribute(Constant.ADMIN_USER);
        }
        return "redirect:/adminLogin";
    }

    @RequestMapping(value = "/doAdminLogin", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult doAdminLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession httpSession) {
        JsonResult jsonResult = new JsonResult();
        try {
            AdminUser adminUser = adminUserService.getAdminUser(username.trim());
            if (adminUser == null) {
                jsonResult.setMessage("用户名不存在！");
            } else {
                if (!adminUser.getPassWord().equals(PasswordTool.encryptPassword(password.trim()))) {
                    jsonResult.setMessage("您输入的密码有误！");
                } else {
                    jsonResult.setMessage("登录成功！");
                    jsonResult.setStateSuccess();
                    httpSession.setAttribute(Constant.ADMIN_USER, adminUser);
                }
            }
        } catch (Exception ex) {
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
            logger.error(ex.getMessage(), ex);
        }
        return jsonResult;
    }

}