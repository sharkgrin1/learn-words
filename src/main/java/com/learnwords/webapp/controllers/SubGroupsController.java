package com.learnwords.webapp.controllers;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.User;
import com.learnwords.webapp.service.GroupService;
import com.learnwords.webapp.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sub")
public class SubGroupsController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private WordService wordService;

    @GetMapping("/{id}")
    public String currentGroup(@PathVariable("id") Long id,
                               Model model,
                               @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                               @RequestParam(name = "switch", defaultValue = "off") boolean isWords,
                               @RequestParam(name = "search", required = false) String search) {

        model.addAttribute("isWords", isWords);

        Group group = groupService.getGroupById(id);
        model.addAttribute("group", group);

        //поиск
        if (StringUtils.hasText(search)) {
            model.addAttribute("words", wordService.search(search, group.getGroupAuthor(), group, pageable));
            model.addAttribute("search", search);
        } else model.addAttribute("words", wordService.getUserWordsByGroup(group, group.getGroupAuthor(), pageable));
        //поиск

        return "sub-group";
    }

    @GetMapping
    public String allUserSubscriptions(@AuthenticationPrincipal User user,
                                       Model model,
                                       @RequestParam(name = "search", required = false) String search,
                                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 5) Pageable pageable) {

        Page<Group> groups = groupService.allSubscriptionsUser(user, pageable);

        if (StringUtils.hasText(search)) {
            model.addAttribute("groups", groupService.searchSubscription(search, user, pageable));
            model.addAttribute("search", search);
        } else model.addAttribute("groups", groups);

        return "sub-group-list";
    }
}
