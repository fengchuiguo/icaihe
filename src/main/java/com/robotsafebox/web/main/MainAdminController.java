package com.robotsafebox.web.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("")
public class MainAdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String index(Model model) {

        return "admin/index";
    }

}