package com.robotsafebox.base.web;

import com.robotsafebox.entity.User;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseAppController {

    @Autowired
    protected UserService userService;

    protected Integer getCurrentUserId() {
        HttpServletRequest request = getRequest();
        HttpSession session = request.getSession();
        if (session.getAttribute(Constant.API_SESSION_USERID) != null) {
            return Integer.valueOf((String) session.getAttribute(Constant.API_SESSION_USERID));
        }
        return null;
    }

    protected User getCurrentUser() {
        User user = userService.getUser(getCurrentUserId());
        return user;
    }

    protected HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        return attrs.getRequest();
    }

}
