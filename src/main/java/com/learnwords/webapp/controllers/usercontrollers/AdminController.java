package com.learnwords.webapp.controllers.usercontrollers;

import com.learnwords.webapp.models.User;
import com.learnwords.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String usersTable(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "admin/users-table";
    }

    @PutMapping
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(name = "isActive") boolean isActive) {
        user.setActive(isActive);
        userService.updateUser(user);
        return "redirect:/admin";
    }
}
