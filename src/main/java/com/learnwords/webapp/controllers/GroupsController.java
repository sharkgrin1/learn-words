package com.learnwords.webapp.controllers;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.User;
import com.learnwords.webapp.models.Word;
import com.learnwords.webapp.service.GroupService;
import com.learnwords.webapp.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupsController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private WordService wordService;

    @GetMapping
    public String allUserGroups(@AuthenticationPrincipal User user,
                                @ModelAttribute("group") Group group,
                                Model model,
                                @RequestParam(name = "search", required = false) String search,
                                @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 5) Pageable pageable) {

        Page<Group> groups = groupService.allUserGroups(user, pageable);

        if (StringUtils.hasText(search)) {
            model.addAttribute("groups", groupService.search(search, user, pageable));
            model.addAttribute("search", search);
        } else model.addAttribute("groups", groups);

        model.addAttribute("groupNames", groupService.getNames(groupService.allUserGroups(user)));
        return "group-list";
    }

    @PostMapping
    public String addGroup(@AuthenticationPrincipal User user,
                           @ModelAttribute("group") Group group) {
        groupService.saveGroup(group, user);
        return "redirect:/groups";
    }


    @DeleteMapping("/{id}")
    public String deleteGroup(@PathVariable("id") Group group) {
        groupService.deleteGroup(group);
        return "redirect:/groups";
    }

    @PutMapping
    public String updateGroup(@AuthenticationPrincipal User user,
                              @ModelAttribute("group") Group group) {
        groupService.updateGroup(group, user);
        return "redirect:/groups";
    }

    @GetMapping("/{id}")
    public String currentGroup(@AuthenticationPrincipal User user,
                               @PathVariable("id") Group group,
                               Model model,
                               @ModelAttribute("word") Word word,
                               @ModelAttribute("newGroup") Group newGroup,
                               @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                               @RequestParam(name = "switch", defaultValue = "off") boolean isWords,
                               @RequestParam(name = "search", required = false) String search) {
        if (!user.getUsername().equals(group.getGroupAuthor().getUsername())) {
            return "redirect:/sub/" + group.getId(); //TODO:возможно временное решение
        }

        model.addAttribute("isWords", isWords);

        model.addAttribute("group", group);

        List<Group> groupList = groupService.allUserGroups(user);
        model.addAttribute("groupList", groupList);

        Page<Word> words = wordService.getUserWordsByGroup(group, user, pageable);

        //поиск
        if (StringUtils.hasText(search)) {
            model.addAttribute("words", wordService.search(search, user, group, pageable));
            model.addAttribute("search", search);
        } else model.addAttribute("words", words);
        //поиск

        model.addAttribute("wordNames", wordService.getNames(wordService.allUserWords(user)));
        model.addAttribute("groupNames", groupService.getNames(groupList));
        return "group";
    }


    @DeleteMapping("/{id}/word")
    public String deleteGroup(@PathVariable("id") Group group,
                              @RequestParam Long wordId) {
        groupService.deleteWordFromGroup(group, wordId);
        return "redirect:/groups/" + group.getId();
    }
}
