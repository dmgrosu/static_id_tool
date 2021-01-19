package com.globaltradecorp.staticIdTool.controller;

import com.globaltradecorp.staticIdTool.model.AppUser;
import com.globaltradecorp.staticIdTool.model.dto.AdminActionRequestDto;
import com.globaltradecorp.staticIdTool.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Dmitri Grosu (dmitri.grosu@codefactorygroup.com), 1/17/21
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class AdminController {

    private final AppUserService userService;

    @GetMapping("/users")
    public String getUsersList(Model model) {
        List<AppUser> userList = userService.getAllUsers();
        model.addAttribute("usersList", userList);
        return "userList";
    }

    @PostMapping("admin/action")
    public ResponseEntity<String> approveUser(@RequestBody AdminActionRequestDto requestDto) {
        switch (requestDto.getAction()) {
            case APPROVE:
                userService.approveUser(requestDto.getUserId(), requestDto.getUserTime());
                break;
            case DELETE:
                userService.deleteUser(requestDto.getUserId(), requestDto.getUserTime());
                break;
            case BLOCK:
                userService.blockUser(requestDto.getUserId());
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid admin action provided");
        }
        return ResponseEntity.ok().build();
    }
}
