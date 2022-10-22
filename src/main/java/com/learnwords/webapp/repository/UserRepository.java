package com.learnwords.webapp.repository;

import com.learnwords.webapp.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    Set<User> findByUsernameContains(String username);

}
