package com.learnwords.webapp.service;

import com.learnwords.webapp.models.*;
import com.learnwords.webapp.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private WordService wordService;

    public void saveGroup(Group group, User user) {
        Group sameGroup = getUserGroupByName(group.getNameGroup(), user);

        if (sameGroup != null)
            return;

        group.setGroupAuthor(user);

        groupRepository.save(group);
    }

    public List<Group> allUserGroups(User user) {
        return groupRepository.findByGroupAuthor(user);
    }

    public Page<Group> allUserGroups(User user, Pageable pageable) {
        return groupRepository.findByGroupAuthor(user, pageable);
    }

    public Page<Group> allSubscriptionsUser(User user, Pageable pageable) {
        return groupRepository.findBySubscribersGroupIsContaining(user, pageable);
    }

    public Group getUserGroupByName(String name, User user) {
        return groupRepository.findByNameGroupAndGroupAuthor(name, user);
    }

    public List<String> getNames(List<Group> groups) {
        List<String> names = new ArrayList<>();
        groups.forEach(group -> names.add(group.getNameGroup()));
        return names;
    }

    public void deleteGroup(Group group) {

//        Set<Word> iterationSet = new HashSet<>(group.getWords());
//
//        for (Word word : iterationSet) {
//            word.removeGroup(group);
//        }

        groupRepository.delete(group);
    }

    public void updateGroup(Group group, User user) {
        Group oldValue = groupRepository.findById(group.getId()).get();
        Group sameGroup = getUserGroupByName(group.getNameGroup(), user);

        if (sameGroup != null && !group.getNameGroup().equals(oldValue.getNameGroup()))
            return;

        group.setGroupAuthor(user);

        group.setWords(oldValue.getWords());

        group.setTests(oldValue.getTests());

        if (oldValue.getPrivacy() != group.getPrivacy() && group.getPrivacy() == Privacy.PRIVATE)
            privateGroup(group);

        groupRepository.save(group);
    }

    public Page<Group> search(String search, User user, Pageable pageable) {
        return groupRepository.findByNameGroupContainingAndGroupAuthor(search, user, pageable);
    }

    public Page<Group> searchSubscription(String search, User user, Pageable pageable) {
        return groupRepository.findByNameGroupContainingAndSubscribersGroupIsContaining(search, user, pageable);
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id).get();
    }

    public void deleteWordFromGroup(Group group, Long wordId) {

        Word word = wordService.findById(wordId);

        group.removeWord(word);

        groupRepository.save(group);
    }

    public void privateGroup(Group group) {

        Set<User> iterationSet = new HashSet<>(group.getSubscribersGroup());

        iterationSet.forEach(subscriber -> subscriber.unsubscribeGroup(group));

        group.setPrivacy(Privacy.PRIVATE);

        groupRepository.save(group);
    }

    public List<Group> allPublicGroupsWithLanguageLike(String name, Language language, User user) {
        return groupRepository.findByNameGroupContainingAndLanguageAndPrivacyAndGroupAuthorIsNot(name, language, Privacy.PUBLIC, user);
    }
}
