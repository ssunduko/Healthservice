package com.health.healthlakeservice;

import com.amazonaws.auth.*;
import com.amazonaws.services.healthlake.AmazonHealthLakeClient;
import com.health.healthlakeservice.aop.AWSRequestSigningApacheInterceptor;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * AWS Health Lake configuration
 *
 * This was adapted from a post "INTRODUCTION TO HAPI FHIR"
 * by Youri Vermeir
 * https://ordina-jworks.github.io/ehealth/2021/02/23/hapi-fhir.html
 */
public class HealthLakeConfiguration {

    @Value("${aws.service.name}")
    private String awsServiceName;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.healthlake.accessKey}")
    private String healthlakeAccessKey;

    @Value("${aws.healthlake.secretKey}")
    private String healthlakeSecretKey;

    @Bean
    /**
     * Build AWS Health Lake client connection
     */
    public HttpClientBuilder healthLakeClient() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(healthlakeAccessKey,
                healthlakeSecretKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

        AmazonHealthLakeClient.builder()
                .withRegion(awsRegion).withCredentials(awsCredentialsProvider);

        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(awsServiceName);
        signer.setRegionName(awsRegion);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        AWSRequestSigningApacheInterceptor apacheInterceptor =
                new AWSRequestSigningApacheInterceptor(awsServiceName, signer, awsCredentialsProvider);
        return httpClientBuilder.addInterceptorLast(apacheInterceptor);
    }
}


