package com.globaltradecorp.staticIdTool.controller;

import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.dto.UserDto;
import com.globaltradecorp.staticIdTool.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/7/21
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    private final AppUserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(AppUserService userService,
                              PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping
    public String register(@ModelAttribute("user") UserDto userDto, BindingResult result) {

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "password.error", "Password doesn't match!");
        }

        if (userService.usernameExists(userDto.getUsername())) {
            result.rejectValue("username", "username.error", "Username is not unique!");
        }

        if (userService.userEmailExists(userDto.getEmail())) {
            result.rejectValue("email", "email.error", "E-mail is not unique!");
        }

        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.registerNewUser(convertFromDto(userDto));
        } catch (Exception ex) {
            result.rejectValue("global", "global", ex.getMessage());
            return "register";
        }

        return "redirect:/register?success";
    }

    private AppUser convertFromDto(UserDto userDto) {
        return AppUser.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .passwd(passwordEncoder.encode(userDto.getPassword()))
                .build();
    }

}
