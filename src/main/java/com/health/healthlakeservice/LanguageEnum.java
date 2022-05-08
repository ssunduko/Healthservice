package com.health.healthlakeservice;

import lombok.Getter;

@Getter
/**
 * List of all supported languages
 */
public enum LanguageEnum {
    ARABIC("ar"),
    CHINESE("zh"),
    ENGLISH("en"),
    RUSSIAN("ru"),
    FRENCH("fr"),
    GERMAN("de"),
    PORTUGUESE("pt"),
    SPANISH("es");

    private final String langCode;

    LanguageEnum(String langCode) {
        this.langCode = langCode;
    }
}
