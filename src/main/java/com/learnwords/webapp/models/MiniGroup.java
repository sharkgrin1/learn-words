package com.learnwords.webapp.models;

import lombok.Data;

@Data
public class MiniGroup {
    private String nameGroup;
    private int countWords;

    public MiniGroup(String nameGroup, int countWords) {
        this.nameGroup = nameGroup;
        this.countWords = countWords;
    }
}
