package com.health.healthlakeservice.controller;

import com.health.healthlakeservice.LanguageEnum;
import com.health.healthlakeservice.service.ComprehensionService;
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
 * NLP Medical Service Controller
 */
public class ComprehensionManager {

    @Autowired
    ComprehensionService comprehensionService;

    @PostMapping("/understand")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * NLP Medical Comprehend endpoint
     */
    public ResponseEntity<String> understand(@RequestBody String text) throws IOException {
        return new ResponseEntity<>(comprehensionService.comprehendMedical(text),HttpStatus.OK);
    }

    @PostMapping("/emotionalize")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * NLP Medical Sentiment Analysis endpoint
     */
    public ResponseEntity<String> emotionalize(@RequestBody String text, @RequestParam(name = "sourceLanguage",
            defaultValue = "ENGLISH", required = false) String sourceLanguage) throws IOException {

        return new ResponseEntity<>(comprehensionService.emotionalize(text, LanguageEnum.valueOf(sourceLanguage) ),HttpStatus.OK);
    }

    @PostMapping("/phrase")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * NLP Phrase Extraction endpoint
     */
    public ResponseEntity<String> phrase(@RequestBody String text, @RequestParam(name = "sourceLanguage",
            defaultValue = "ENGLISH", required = false) String sourceLanguage) throws IOException {
        return new ResponseEntity<>(comprehensionService.phrase(text, LanguageEnum.valueOf(sourceLanguage) ),HttpStatus.OK);
    }
}
