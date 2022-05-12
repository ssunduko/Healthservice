package com.health.healthlakeservice;

import java.io.IOException;
import java.util.Properties;

import com.amazonaws.auth.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
/**
 * Property configuration
 *
 *  This was adapted from a post "Spring Events"
 *  by Eugen Paraschiv
 *  https://www.baeldung.com/spring-events
 */
public class PropertiesConfig implements ApplicationListener<ApplicationPreparedEvent> {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String AWS_REGION = "us-east-1";
    private static final String AWS_DYNAMO_SECRET_NAME = "prod/healthservice/dynamodb";
    private static final String AWS_DYNAMO_KEY = "aws.dynamodb.accessKey";
    private static final String AWS_DYNAMO_SECRET = "aws.dynamodb.secretKey";
    private static final String AWS_HEALTH_SECRET_NAME = "prod/healthservice/healthlake";
    private static final String AWS_HEALTH_KEY = "aws.healthlake.accessKey";
    private static final String AWS_HEALTH_SECRET = "aws.healthlake.secretKey";
    private static final String AWS_BASICAUTH_SECRET_NAME = "prod/healthservice/basicauth";
    private static final String AWS_BASICAUTH_KEY = "spring.security.user.name";
    private static final String AWS_BASICAUTH_SECRET = "spring.security.user.password";

    @SneakyThrows
    @Override
    /**
     * Extract all variables
     */
    public void onApplicationEvent(ApplicationPreparedEvent event) {

        ConfigurableEnvironment environment = event.
                getApplicationContext().getEnvironment();

        Properties props = new Properties();

        props.put(AWS_DYNAMO_KEY, getString(getSecret(AWS_DYNAMO_SECRET_NAME), AWS_DYNAMO_KEY));
        props.put(AWS_DYNAMO_SECRET, getString(getSecret(AWS_DYNAMO_SECRET_NAME), AWS_DYNAMO_SECRET));

        props.put(AWS_HEALTH_KEY, getString(getSecret(AWS_HEALTH_SECRET_NAME), AWS_HEALTH_KEY));
        props.put(AWS_HEALTH_SECRET, getString(getSecret(AWS_HEALTH_SECRET_NAME), AWS_HEALTH_SECRET));

        props.put(AWS_BASICAUTH_KEY, getString(getSecret(AWS_BASICAUTH_SECRET_NAME), AWS_BASICAUTH_KEY));
        props.put(AWS_BASICAUTH_SECRET, getString(getSecret(AWS_BASICAUTH_SECRET_NAME), AWS_BASICAUTH_SECRET));

        environment.getPropertySources().addFirst
                (new PropertiesPropertySource("aws.secret.manager", props));
    }

    /**
     * Get specific secret
     * @param secretName
     * @return
     */
    private String getSecret(String secretName) {

        AWSCredentialsProvider awsCredentialsProvider = new DefaultAWSCredentialsProviderChain();

        AWSSecretsManager client = AWSSecretsManagerClientBuilder
                .standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(AWS_REGION)
                .build();

        String secret = null;
        GetSecretValueRequest getSecretValueRequest = new
                GetSecretValueRequest().withSecretId(secretName);
        GetSecretValueResult getSecretValueResult;

        getSecretValueResult = client.getSecretValue(getSecretValueRequest);

        if (getSecretValueResult != null && getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
        }

        return secret;
    }

    private String getString(String json, String path) throws JsonProcessingException {
        JsonNode root = mapper.readTree(json);
        return root.path(path).asText();
    }
}
