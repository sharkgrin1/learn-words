package com.learnwords.webapp.controllers;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.User;
import com.learnwords.webapp.models.Word;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/words")
public class WordsController {
    @Autowired
    private WordService wordService;

    @Autowired
    private GroupService groupService;

    @GetMapping
    public String allWords(@AuthenticationPrincipal User user,
                           @ModelAttribute("word") Word word,
                           @ModelAttribute("newGroup") Group newGroup,
                           Model model,
                           @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                           @RequestParam(name = "searchWord", required = false) String search,
                           @RequestParam(name = "searchGroup", required = false) List<String> searchGroup,
                           @RequestParam(name = "switch", defaultValue = "off") boolean isWords) {

        model.addAttribute("isWords", isWords);

        List<Group> groupList = groupService.allUserGroups(user);
        model.addAttribute("groupList", groupList);

        Page<Word> words = wordService.allUserWords(user, pageable);

        //поиск
        if (searchGroup != null && !searchGroup.isEmpty()) {
            model.addAttribute("words", wordService.search(search, searchGroup, user, pageable));
            model.addAttribute("searchWord", search);
            model.addAttribute("searchGroup", searchGroup);
        } else if (StringUtils.hasText(search)) {
            model.addAttribute("words", wordService.search(search, user, pageable));
            model.addAttribute("searchWord", search);
        } else model.addAttribute("words", words);
        //поиск

        model.addAttribute("wordNames", wordService.getNames(wordService.allUserWords(user)));
        model.addAttribute("groupNames", groupService.getNames(groupList));

        return "words";
    }

    @PostMapping
    public String addWord(@AuthenticationPrincipal User user,
                          @ModelAttribute("word") Word word,
                          @ModelAttribute("newGroup") Group newGroup,
                          @RequestParam(name = "switch") boolean isWords,
                          @RequestParam(name = "groupId", required = false) Long groupId,
                          RedirectAttributes redirectAttributes) {
        if (StringUtils.hasText(newGroup.getNameGroup()))
            wordService.saveWord(newGroup, word, user);
        else wordService.saveWord(null, word, user);

        redirectAttributes.addAttribute("switch", isWords);

        if (groupId!=null)
            return "redirect:/groups/"+groupId;

        return "redirect:/words";
    }

    @DeleteMapping("/{id}")
    public String deleteWord(@PathVariable("id") Long id,
                             @RequestParam(name = "switch") boolean isWords,
                             @RequestParam(name = "groupId", required = false) Long groupId,
                             RedirectAttributes redirectAttributes) {
        wordService.deleteWord(id);
        redirectAttributes.addAttribute("switch", isWords);
        if (groupId!=null)
            return "redirect:/groups/"+groupId;
        return "redirect:/words";
    }

    @PutMapping
    public String updateWord(@AuthenticationPrincipal User user,
                             @ModelAttribute("word") Word word,
                             @RequestParam(name = "switch") boolean isWords,
                             @RequestParam(name = "groupId", required = false) Long groupId,
                             RedirectAttributes redirectAttributes) {
        wordService.updateWord(word, user);
        redirectAttributes.addAttribute("switch", isWords);
        if (groupId!=null)
            return "redirect:/groups/"+groupId;
        return "redirect:/words";
    }

}
