package com.learnwords.webapp.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@Data
@EqualsAndHashCode(of = {"id", "nameGroup"})
@ToString(exclude = {"subscribersGroup", "words", "groupAuthor"})
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NotBlank
    @Column(nullable = false)
    protected String nameGroup;

    protected String description;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected Privacy privacy;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected Language language;

    @ManyToMany
    @JoinTable(
            name = "group_subscribers",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    protected Set<User> subscribersGroup = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "group_word",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    protected Set<Word> words = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    protected User groupAuthor;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    protected Set<Test> tests;

    public void removeWord(Word word) {
        this.words.remove(word);
        word.getGroups().remove(this);
    }
}
