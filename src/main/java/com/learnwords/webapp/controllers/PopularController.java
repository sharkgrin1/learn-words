package com.learnwords.webapp.controllers;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.Language;
import com.learnwords.webapp.models.User;
import com.learnwords.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Controller
public class PopularController {
    @Autowired
    private UserService userService;

    @GetMapping("popular")
    public String getPopular(Model model) {
        User user = userService.userByUsername("admin");
        Set<Group> groups = user.getUserGroups();

        model.addAttribute("english", groups.stream().filter(group -> group.getLanguage() == Language.ENGLISH).toArray());
        model.addAttribute("spanish", groups.stream().filter(group -> group.getLanguage() == Language.SPANISH).toArray());
        model.addAttribute("french", groups.stream().filter(group -> group.getLanguage() == Language.FRENCH).toArray());

        return "popular";
    }
}
