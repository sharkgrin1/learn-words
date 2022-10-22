package com.learnwords.webapp.models;

public enum Language {
    DEFAULT("Другой язык"), FRENCH("Французский"), SPANISH("Испанский"), RUSSIAN("Русский"), ARABIC("Арабский"),
    PORTUGUESE("Португальский"), GERMAN("Немецкий"), ENGLISH("Английский"), CHINESE("Китайский");
    private final String rus;

    Language(String rus) {
        this.rus = rus;
    }

    @Override
    public String toString() {
        return rus;
    }
}
