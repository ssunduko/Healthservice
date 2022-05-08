package com.health.healthlakeservice.controller;

import com.health.healthlakeservice.LanguageEnum;
import com.health.healthlakeservice.service.TranslationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@SecurityRequirement(name = "capstone")
/**
 * NLP Translation Service Controller
 */
public class TranslationManager {

    @Autowired
    TranslationService translateService;

    @PostMapping("/translate")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * NLP Translate endpoint
     */
    public ResponseEntity<String> translate(@RequestBody String text,
                                             @RequestParam(name = "sourceLanguage", defaultValue = "ENGLISH", required = false)
            String sourceLanguage, @RequestParam(name = "destinationLanguage", defaultValue = "ENGLISH", required = false) String destinationLanguage) throws IOException {

        return new ResponseEntity<>(translateService.translate(text, LanguageEnum.valueOf(sourceLanguage),
                LanguageEnum.valueOf(destinationLanguage)), HttpStatus.OK);
    }
}
