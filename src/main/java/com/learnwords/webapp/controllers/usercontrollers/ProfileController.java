package com.learnwords.webapp.controllers.usercontrollers;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.Privacy;
import com.learnwords.webapp.models.User;
import com.learnwords.webapp.service.GroupService;
import com.learnwords.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @GetMapping
    public String userProfile(@AuthenticationPrincipal User user) {
        return "redirect:/profile/" + user.getId();
    }


    @GetMapping("/{user}")
    public String anotherUserProfile(@AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
        model.addAttribute("groups",
                user.getUserGroups().stream().filter(group -> group.getPrivacy() == Privacy.PUBLIC).toArray()
        );
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isUserPage", currentUser.getUsername().equals(user.getUsername()));
        return "profile";
    }

    @PostMapping
    public String subscribe(@AuthenticationPrincipal User currentUser,
                            @RequestParam Group group,
                            @RequestParam User user) {

        userService.subscribeGroup(currentUser.getId(), group);

        return "redirect:/profile/" + user.getId();
    }


    @DeleteMapping
    public String unsubscribe(@AuthenticationPrincipal User currentUser,
                              @RequestParam Group group,
                              @RequestParam User user) {

        userService.unsubscribeGroup(currentUser.getId(), group);

        return "redirect:/profile/" + user.getId();
    }

    @PutMapping("/hide")
    public String hideGroup(@RequestParam Group group) {

        groupService.privateGroup(group);

        return "redirect:/profile";
    }
}
