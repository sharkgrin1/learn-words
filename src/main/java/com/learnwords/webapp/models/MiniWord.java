package com.learnwords.webapp.models;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class MiniWord {
    private String name;
    private Set<String> translations;

    public MiniWord(String name, Set<String> translations) {
        this.name = name;
        this.translations = translations;
    }
}
