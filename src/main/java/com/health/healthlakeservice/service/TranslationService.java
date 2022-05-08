package com.health.healthlakeservice.service;

import com.health.healthlakeservice.LanguageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;


@Service
/**
 * Translation Service
 */
public class TranslationService {

    @Autowired
    private AmazonTranslate translateClient;

    /**
     * Translate from source to destination language
     * @param text
     * @param sourceLang
     * @param targetLang
     * @return
     */
    public String translate(String text, LanguageEnum sourceLang, LanguageEnum targetLang) {

        TranslateTextRequest request = new TranslateTextRequest()
                .withText(text)
                .withSourceLanguageCode(sourceLang.getLangCode())
                .withTargetLanguageCode(targetLang.getLangCode());

        TranslateTextResult result = translateClient.translateText(request);
        return result.getTranslatedText();
    }
}
