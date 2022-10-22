package com.learnwords.webapp.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "name", "translations", "groupNames"})
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NotBlank
    @Column(nullable = false)
    protected String name;

    @ElementCollection
    @CollectionTable(
            name = "translation",
            joinColumns = @JoinColumn(name = "word_id"))
    @Column(name = "name")
    protected Set<String> translations = new HashSet<>(); //TODO: сделать проверку на пустой перевод

    @ManyToMany(mappedBy = "words", cascade = CascadeType.PERSIST)
    protected Set<Group> groups = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    protected User wordAuthor;

    @Transient
    protected List<String> groupNames = new ArrayList<>();

    public void addGroup(Group group) {
        this.groups.add(group);
        group.getWords().add(this);
    }

    public void removeGroup(Group group) {
        this.groups.remove(group);
        group.getWords().remove(this);
    }
}
