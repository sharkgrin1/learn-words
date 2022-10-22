package com.learnwords.webapp.service;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.Test;
import com.learnwords.webapp.models.User;
import com.learnwords.webapp.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private GroupService groupService;

    public Page<Test> allUserTests(User user, Pageable pageable) {
        return testRepository.findByTestAuthor(user, pageable);
    }

    public void saveTest(Test test, User user, String group) {
        Group selected = groupService.getUserGroupByName(group, user);
        test.setGroup(selected);
        test.setTestAuthor(user);
        testRepository.save(test);
    }

    public void deleteTest(Test test) {
        testRepository.delete(test);
    }
}
