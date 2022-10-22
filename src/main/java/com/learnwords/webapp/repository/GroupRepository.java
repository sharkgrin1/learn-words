package com.learnwords.webapp.repository;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.Language;
import com.learnwords.webapp.models.Privacy;
import com.learnwords.webapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Long> {
    List<Group> findByGroupAuthor(User user);

    Page<Group> findByGroupAuthor(User user, Pageable pageable);

    Page<Group> findBySubscribersGroupIsContaining(User user, Pageable pageable);

    Group findByNameGroupAndGroupAuthor(String name, User user);

    Page<Group> findByNameGroupContainingAndGroupAuthor(String name, User user, Pageable pageable);

    Page<Group> findByNameGroupContainingAndSubscribersGroupIsContaining(String name, User user, Pageable pageable);

    List<Group> findByPrivacyAndGroupAuthor(Privacy privacy, User groupAuthor);

    List<Group> findByNameGroupContainingAndLanguageAndPrivacyAndGroupAuthorIsNot(String name, Language language, Privacy privacy, User user);
}
