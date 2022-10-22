package com.learnwords.webapp.service;

import com.learnwords.webapp.models.Group;
import com.learnwords.webapp.models.Role;
import com.learnwords.webapp.models.User;
import com.learnwords.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean saveUser(User oldValue, User user) {
        User sameUser = userRepository.findByUsername(user.getUsername());
        User currentUser = userRepository.findByUsername(oldValue.getUsername());

        if (sameUser != null && !currentUser.getUsername().equals(user.getUsername()))
            return false;

        if (!user.getPassword().isBlank())
            currentUser.setPassword(passwordEncoder.encode(user.getPassword()));

        if (!user.getAvatar().isBlank())
            currentUser.setAvatar(user.getAvatar());

        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());

        userRepository.save(currentUser);
        return true;
    }

    public boolean saveUser(User user) {
        User sameUser = userRepository.findByUsername(user.getUsername());

        if (sameUser != null)
            return false;

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return true;
    }

    public void updateUser(User user) {
        User fromDb = userRepository.findById(user.getId()).get();

        fromDb.setRoles(user.getRoles());
        fromDb.setActive(user.isActive());
        userRepository.save(fromDb);
    }

    public List<User> allUsers() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().forEach(list::add);
        return list;
    }

    public User userByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Set<User> allUsersLikeUsername(String username) {
        return userRepository.findByUsernameContains(username);
    }

    public void subscribeGroup(Long idUser, Group group) {
        User currentUser = userRepository.findById(idUser).get();
        currentUser.subscribeGroup(group);
        userRepository.save(currentUser);
    }

    public void unsubscribeGroup(Long idUser, Group group) {
        User currentUser = userRepository.findById(idUser).get();
        currentUser.unsubscribeGroup(group);
        userRepository.save(currentUser);
    }
}
