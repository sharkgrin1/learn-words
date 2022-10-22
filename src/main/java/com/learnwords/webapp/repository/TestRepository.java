package com.learnwords.webapp.repository;

import com.learnwords.webapp.models.Test;
import com.learnwords.webapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends CrudRepository<Test, Long> {
    Page<Test> findByTestAuthor(User user, Pageable pageable);
}
