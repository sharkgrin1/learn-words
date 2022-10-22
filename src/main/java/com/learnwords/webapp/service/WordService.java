package com.learnwords.webapp.service;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.User;
import com.learnwords.webapp.models.Word;
import com.learnwords.webapp.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WordService {
    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private GroupService groupService;

    public boolean saveWord(Group newGroup, Word word, User user) {
        Word sameWord = wordRepository.findByNameAndWordAuthor(word.getName(), user);

        if (sameWord != null)
            return false;

        word.setWordAuthor(user);

        for (String name : word.getGroupNames()) {
            Group group = groupService.getUserGroupByName(name, user);
            word.addGroup(group);
        }

        if (newGroup != null && groupService.getUserGroupByName(newGroup.getNameGroup(), user) == null) {
            newGroup.setGroupAuthor(user);
            word.addGroup(newGroup);
        }


        wordRepository.save(word);
        return true;
    }

    public Page<Word> allUserWords(User user, Pageable pageable) {
        return wordRepository.findByWordAuthor(user, pageable);
    }

    public List<Word> allUserWords(User user) {
        return wordRepository.findByWordAuthor(user);
    }

    public void deleteWord(Long id) {
        Word word = findById(id);

        Set<Group> iterationSet = new HashSet<>(word.getGroups());

        iterationSet.forEach(word::removeGroup);

        wordRepository.delete(word);
    }

    public boolean updateWord(Word word, User user) {
        Word oldValue = findById(word.getId());

        Word sameWord = wordRepository.findByNameAndWordAuthor(word.getName(), user);

        if (sameWord != null && !word.getName().equals(oldValue.getName()))
            return false;

        word.setWordAuthor(user);

        word.setGroups(oldValue.getGroups());

        //удаление тех, которых в списке названий групп нет
        Set<Group> iterationSet = new HashSet<>(word.getGroups());

        iterationSet.stream()
                .filter(group -> !word.getGroupNames().contains(group.getNameGroup()))
                .forEach(word::removeGroup);

        //добавление всех из списка названий групп
        for (String name : word.getGroupNames()) {
            Group group = groupService.getUserGroupByName(name, user);
            word.addGroup(group);
        }

        wordRepository.save(word);
        return true;
    }

    public Page<Word> search(String search, List<String> groupNames, User user, Pageable pageable) {
        return wordRepository.findByNameAndTranslationAndGroupAndWordAuthor(search, groupNames, user, pageable);
    }

    public Page<Word> search(String search, User user, Pageable pageable) {
        return wordRepository.findByNameAndTranslationAndAuthor(search, user, pageable);
    }

    public Page<Word> search(String search, User user, Group group, Pageable pageable) {
        return wordRepository.findByNameAndTranslationAndCurrentGroupAndWordAuthor(search, user, group, pageable);
    }

    public List<String> getNames(List<Word> words) {
        List<String> names = new ArrayList<>();
        words.forEach(word->names.add(word.getName()));
        return names;
    }

    public Page<Word> getUserWordsByGroup(Group group, User user, Pageable pageable) {
        return wordRepository.findByGroupsIsContainingAndWordAuthor(group, user, pageable);
    }

    public Word findById(Long id) {
        return wordRepository.findById(id).get();
    }
}
