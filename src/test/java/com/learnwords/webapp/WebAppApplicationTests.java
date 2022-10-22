package com.learnwords.webapp;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.Word;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class WebAppApplicationTests {

    @Test
    void contextLoads() {
        Word word = new Word();
        word.setId(1L);
        word.setGroupNames(Arrays.asList("lola","lila","popa"));
        Set<Group> groups = new HashSet<>();
        Group group = new Group();
        group.setId(1L);
        group.setNameGroup("lola");
        Group group1 = new Group();
        group1.setId(2L);
        group1.setNameGroup("dod");
        Group group2 = new Group();
        group2.setNameGroup("popa");
        group2.setId(3L);
        groups.add(group);
        groups.add(group1);
        groups.add(group2);
        word.setGroups(groups);

        word.getGroups().stream().filter(gr -> !word.getGroupNames().contains(gr.getNameGroup())).forEach(System.out::println);
        for (Group gr : word.getGroups()) {
            if (!word.getGroupNames().contains(gr.getNameGroup()))
                System.out.println(gr);
        }

    }

}
