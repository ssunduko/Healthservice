package com.health.healthlakeservice.service;

import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.model.*;
import com.amazonaws.services.comprehendmedical.AWSComprehendMedical;
import com.amazonaws.services.comprehendmedical.model.DetectEntitiesV2Request;
import com.amazonaws.services.comprehendmedical.model.DetectEntitiesV2Result;
import com.amazonaws.services.comprehendmedical.model.Entity;
import com.google.gson.Gson;
import com.health.healthlakeservice.LanguageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
/**
 * NLP Service
 */
public class ComprehensionService {

    @Autowired
    private AWSComprehendMedical awsComprehendMedical;

    @Autowired
    private AmazonComprehend amazonComprehend;

    /**
     * Medical NLP
     * @param text
     * @return
     */
    public String comprehendMedical(String text) {

        DetectEntitiesV2Request request = new DetectEntitiesV2Request().withText(text);

        DetectEntitiesV2Result result = awsComprehendMedical.detectEntitiesV2(request);
        List<Entity> entityList = result.getEntities();

        Gson gson = new Gson();
        return gson.toJson(entityList);

    }

    /**
     * NLP Phrase extraction
     * @param text
     * @param languageEnum
     * @return
     */
    public String phrase(String text, LanguageEnum languageEnum) {

        DetectKeyPhrasesRequest request = new DetectKeyPhrasesRequest().withText(text)
                .withLanguageCode(languageEnum.getLangCode());

        DetectKeyPhrasesResult result = amazonComprehend.detectKeyPhrases(request);

        List<KeyPhrase> phraseList = result.getKeyPhrases();

        Gson gson = new Gson();
        return gson.toJson(phraseList);
    }

    /**
     * NLP Sentiment analysis
     * @param text
     * @param languageEnum
     * @return
     */
    public String emotionalize(String text, LanguageEnum languageEnum) {

        DetectSentimentRequest request = new DetectSentimentRequest()
                .withText(text)
                .withLanguageCode(languageEnum.getLangCode());

        DetectSentimentResult result = amazonComprehend.detectSentiment(request);

        return result.getSentiment();
    }

}
