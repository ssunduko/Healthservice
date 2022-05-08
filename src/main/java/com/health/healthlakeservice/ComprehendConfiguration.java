package com.health.healthlakeservice;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehendmedical.AWSComprehendMedical;
import com.amazonaws.services.comprehendmedical.AWSComprehendMedicalClient;
import com.amazonaws.services.comprehend.AmazonComprehendClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * AWS Medical Comprehend configuration
 */
public class ComprehendConfiguration {

    @Value("${aws.region}")
    private String region;

    @Bean
    /**
     * Get AWS Comprehend Medical
     */
    public AWSComprehendMedical comprehendMedicalClient() {

        AWSCredentialsProvider awsCredentialsProvider = new DefaultAWSCredentialsProviderChain();

        return AWSComprehendMedicalClient.builder()
                .withCredentials(awsCredentialsProvider)
                .withRegion(region)
                .build();
    }

    @Bean
    /**
     * Get AWS Comprehend Medical client
     */
    public AmazonComprehend comprehendClient(){

        AWSCredentialsProvider awsCredentialsProvider = new DefaultAWSCredentialsProviderChain();

        return AmazonComprehendClient.builder()
                .withCredentials(awsCredentialsProvider)
                .withRegion(region)
                .build();
    }
}
