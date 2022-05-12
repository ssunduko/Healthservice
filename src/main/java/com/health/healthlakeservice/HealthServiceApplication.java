package com.health.healthlakeservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@ConfigurationPropertiesScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, ElasticsearchRestClientAutoConfiguration.class })
/**
 * Health service Spring Boot application
 *
 * This was adapted from a post "INTRODUCTION TO HAPI FHIR"
 * by Youri Vermeir
 * https://ordina-jworks.github.io/ehealth/2021/02/23/hapi-fhir.html
 */
public class HealthServiceApplication {

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {

        SpringApplication.run(HealthServiceApplication.class, args);
    }

    @Bean
    /**
     * Bean registration
     */
    public ServletRegistrationBean<HealthRestfulServer> ServletRegistrationBean() {
        return new ServletRegistrationBean<>(new HealthRestfulServer(applicationContext),"/fhir/*");
    }

}
