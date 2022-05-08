package com.health.healthlakeservice;

import com.amazonaws.auth.*;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClientBuilder;;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * Translation service configuration
 */
public class TranslateConfiguration {

    @Value("${aws.region}")
    private String region;

    @Bean
    /**
     * Instantiate AWS Translate Service
     */
    public AmazonTranslate translateClient() {

        AWSCredentialsProvider awsCredentialsProvider = new DefaultAWSCredentialsProviderChain();

        return AmazonTranslateClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(awsCredentialsProvider)
                .build();
    }

}
