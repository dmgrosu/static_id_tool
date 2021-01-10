package com.globaltradecorp.staticIdTool.controller;

import com.globaltradecorp.staticIdTool.model.ComponentType;
import com.globaltradecorp.staticIdTool.service.StaticIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/8/21
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    private final StaticIdService staticIdService;

    @Autowired
    public HomeController(StaticIdService staticIdService) {
        this.staticIdService = staticIdService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        List<ComponentType> componentTypes = staticIdService.getComponentList();
        model.addAttribute("componentTypes", componentTypes);
        return "home";
    }

}
