package com.learnwords.webapp.controllers;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.Language;
import com.learnwords.webapp.models.User;
import com.learnwords.webapp.service.GroupService;
import com.learnwords.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal User user,
                           @RequestParam(required = false) Language language,
                           @RequestParam(required = false) String search,
                           Model model) {
        if (StringUtils.hasText(search)) {
            Set<User> users = userService.allUsersLikeUsername(search);
            List<Group> groups = groupService.allPublicGroupsWithLanguageLike(search, language, user);
            users.remove(user); //чтобы в поиске не было себя же
            model.addAttribute("users", users);
            model.addAttribute("groups", groups);
            model.addAttribute("search", search);
            model.addAttribute("selectedLanguage", language);
        } else model.addAttribute("selectedLanguage", Language.DEFAULT);
        return "main";
    }
}
