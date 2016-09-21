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

    protected Long getCurrentUserId() {
        HttpServletRequest request = getRequest();
        HttpSession session = request.getSession();
        if (session.getAttribute(Constant.API_SESSION_USERID) != null) {
            return Long.valueOf((String) session.getAttribute(Constant.API_SESSION_USERID));
        } else {
            throw new RuntimeException("BaseAppController:API_SESSION_USERID  ==  null");
        }
    }

    protected User getCurrentUser() {
        User user = userService.getUser(getCurrentUserId());
        if (user == null) {
            throw new RuntimeException("BaseAppController:getCurrentUser() == null");
        }
        return user;
    }

    protected HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        return attrs.getRequest();
    }

}
