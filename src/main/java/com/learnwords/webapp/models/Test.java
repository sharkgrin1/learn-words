package com.learnwords.webapp.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@EqualsAndHashCode(of = {"id"})
@ToString(exclude = {"group", "testAuthor"})
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NotBlank
    @Column(nullable = false)
    protected String nameTest;

    @NotBlank
    @Column(nullable = false)
    protected Long countWords;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    protected Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    protected User testAuthor;
}
