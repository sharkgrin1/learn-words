package com.learnwords.webapp.controllers;

import com.learnwords.webapp.models.*;
import com.learnwords.webapp.service.GroupService;
import com.learnwords.webapp.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/tests")
public class TestController {
    @Autowired
    private TestService testService;

    @Autowired
    private GroupService groupService;

    @GetMapping
    public String allUserTests(@AuthenticationPrincipal User user,
                               Model model,
                               @ModelAttribute("test") Test test,
                               @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        List<MiniGroup> groups = new ArrayList<>();
        groupService.allUserGroups(user).stream().filter(group -> group.getWords().size() > 0).forEach(group -> groups.add(new MiniGroup(group.getNameGroup(), group.getWords().size())));
        model.addAttribute("tests", testService.allUserTests(user, pageable));
        model.addAttribute("groups", groups);

        return "test-list";
    }

    @PostMapping
    public String addTest(@AuthenticationPrincipal User user,
                          @ModelAttribute Test test,
                          @RequestParam("selectedGroup") String group) {
        testService.saveTest(test, user, group);
        return "redirect:/tests";
    }

    @GetMapping("/{id}")
    public String getTest(@PathVariable("id") Test test, Model model) {
        model.addAttribute("test", test);
        List<MiniWord> list = new ArrayList<>();

        List<Word> words = new ArrayList<>(test.getGroup().getWords());
        Collections.shuffle(words); //перемешка листа
        words.forEach(word -> list.add(new MiniWord(word.getName(), word.getTranslations())));
        model.addAttribute("words", list);
        return "test";
    }

    @DeleteMapping("/{id}")
    public String deleteTest(@PathVariable("id") Test test) {
        testService.deleteTest(test);
        return "redirect:/tests";
    }
}
