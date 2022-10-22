package com.learnwords.webapp.repository;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.User;
import com.learnwords.webapp.models.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WordRepository extends CrudRepository<Word, Long> {
    Page<Word> findByWordAuthor(User user, Pageable pageable);

    List<Word> findByWordAuthor(User user);

    Word findByNameAndWordAuthor(String name, User user);

    @Query("SELECT distinct w FROM Word w INNER JOIN w.translations tr JOIN w.groups g WHERE " +
            "(w.name LIKE %:name% OR tr LIKE %:name%) " +
            "AND g.nameGroup IN :groupList " +
            "AND w.wordAuthor=:author")
    Page<Word> findByNameAndTranslationAndGroupAndWordAuthor(String name, List<String> groupList, User author, Pageable pageable);

    @Query("SELECT distinct w FROM Word w INNER JOIN w.translations tr WHERE " +
            "(w.name LIKE %:name% OR tr LIKE %:name%) " +
            "AND w.wordAuthor=:author")
    Page<Word> findByNameAndTranslationAndAuthor(String name, User author, Pageable pageable);

    Page<Word> findByGroupsIsContainingAndWordAuthor(Group group, User user, Pageable pageable);

    @Query("SELECT distinct w FROM Word w " +
            "INNER JOIN w.translations tr " +
            "INNER JOIN w.groups g WHERE " +
            "(w.name LIKE %:name% OR tr LIKE %:name%) " +
            "AND g=:group " +
            "AND w.wordAuthor=:author")
    Page<Word> findByNameAndTranslationAndCurrentGroupAndWordAuthor(String name, User author, Group group, Pageable pageable);
}
