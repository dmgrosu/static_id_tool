package com.globaltradecorp.staticIdTool.controller;

import com.globaltradecorp.staticIdTool.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Controller
@RequestMapping(value = {"/","/login","/index"})
public class LoginController {

    private final static Logger LOG = LoggerFactory.getLogger(LoginController.class);

    private final AppUserService userService;

    @Autowired
    public LoginController(AppUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String loginForm(final Model model) {
        model.addAttribute("loginError", "error");
        return "loginForm";
    }

}
