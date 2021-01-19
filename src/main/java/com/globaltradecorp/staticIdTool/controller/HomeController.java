package com.globaltradecorp.staticIdTool.controller;

import com.globaltradecorp.staticIdTool.model.ComponentType;
import com.globaltradecorp.staticIdTool.service.StaticIdService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class HomeController {

    private final StaticIdService staticIdService;

    @GetMapping
    public String getHomePage(Model model) {
        List<ComponentType> componentTypes = staticIdService.getComponentList();
        model.addAttribute("componentTypes", componentTypes);
        return "home";
    }

}
