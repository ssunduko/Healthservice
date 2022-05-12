package com.health.healthlakeservice;

import lombok.Getter;

@Getter
/**
 * List of all supported languages
 */
public enum LanguageEnum {

    GERMAN("de"),
    PORTUGUESE("pt"),
    SPANISH("es"),
    ARABIC("ar"),
    CHINESE("zh"),
    ENGLISH("en"),
    RUSSIAN("ru"),
    FRENCH("fr");

    private final String langCode;

    LanguageEnum(String langCode) {
        this.langCode = langCode;
    }
}
