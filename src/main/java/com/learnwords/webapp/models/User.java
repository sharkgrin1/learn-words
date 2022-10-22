package com.learnwords.webapp.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(of = {"id", "username"})
@ToString(exclude = {"userWords", "userGroups", "subscriptionsGroup"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    protected String username;

    @NotBlank
    @Column(nullable = false)
    protected String password;

    @Transient
    protected String password2;

    @Email
    @NotBlank
    protected String email;

    protected String activationCode;

    protected boolean isActive;

    @NotBlank
    @Column(nullable = false, length = 300)
    protected String avatar;

    @OneToMany(mappedBy = "wordAuthor",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    protected Set<Word> userWords = new HashSet<>();

    @OneToMany(mappedBy = "groupAuthor",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    protected Set<Group> userGroups = new HashSet<>();

    @ManyToMany(mappedBy = "subscribersGroup",
            fetch = FetchType.LAZY)
    protected Set<Group> subscriptionsGroup = new HashSet<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    protected Set<Role> roles;

    @OneToMany(mappedBy = "testAuthor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    protected Set<Test> tests;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //TODO: Сделать блокировку пользователя админом
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() { //TODO: Подтвердил код или нет
        return isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public void subscribeGroup(Group group) {
        this.subscriptionsGroup.add(group);
        group.getSubscribersGroup().add(this);
    }

    public void unsubscribeGroup(Group group) {
        this.subscriptionsGroup.remove(group);
        group.getSubscribersGroup().remove(this);
    }
}
