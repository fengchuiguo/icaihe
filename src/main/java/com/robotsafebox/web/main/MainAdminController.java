package com.robotsafebox.web.main;

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
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class MainAdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {

        return "admin/index";
    }

    @RequestMapping(value = "/userinfo", method = RequestMethod.GET)
    public String userinfo(Model model) {

        return "admin/userinfo/index";
    }

    @RequestMapping(value = "/sysconfig/druid", method = RequestMethod.GET)
    public String druid(Model model) {

        return "admin/sysconfig/druid/index";
    }

}