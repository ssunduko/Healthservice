package com.health.healthlakeservice;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import com.health.healthlakeservice.controller.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.Arrays;
import java.util.List;

@WebServlet("/*")
@CrossOrigin(origins = "*", allowedHeaders = "*")
/**
 * Health Service designed for REST communication
 *
 *  This was adapted from a post "INTRODUCTION TO HAPI FHIR"
 *  by Youri Vermeir
 *  https://ordina-jworks.github.io/ehealth/2021/02/23/hapi-fhir.html
 */
public class HealthRestfulServer extends RestfulServer{

    private final ApplicationContext applicationContext;

    HealthRestfulServer(ApplicationContext context) {
        this.applicationContext = context;
    }

    @Value("${aws.basicauth.accessKey}")
    private String basicauthAccessKey;

    @Value("${aws.basicauth.secretKey}")
    private String basicauthSecretKey;

    @Override
    protected  void  initialize() throws  ServletException {

        CorsConfiguration  config = new CorsConfiguration();
        config.addAllowedHeader("x-fhir-starter");
        config.addAllowedHeader("Origin");
        config.setAllowedOriginPatterns(List.of("*"));
        config.addAllowedHeader("Accept");
        config.addAllowedOrigin("http://localhost:3000");
        //config.addAllowedOrigin("*");
        config.addAllowedHeader("X-Requested-With");
        config.addAllowedHeader("Content-Type");
        config.addExposedHeader("Location");
        config.addExposedHeader("Access-Control-Allow-Origin");
        config.addExposedHeader("Content-Location");
        config.addAllowedHeader("Authorization");
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        CorsInterceptor  interceptor = new CorsInterceptor(config);
        registerInterceptor(interceptor);

        OpenApiInterceptor openApiInterceptor = new OpenApiInterceptor();
        registerInterceptor(openApiInterceptor);


        BasicAuthInterceptor authInterceptor = new BasicAuthInterceptor(basicauthAccessKey, basicauthSecretKey);

        IGenericClient genericClient = FhirContext.forR4().newRestfulGenericClient("http://localhost:8080/");
        genericClient.registerInterceptor(authInterceptor);

        super.initialize();
        setFhirContext(FhirContext.forR4());

        registerProvider(applicationContext.getBean(PatientManager.class));
        registerProvider(applicationContext.getBean(ObservationManager.class));
        registerProvider(applicationContext.getBean(ImmunizationManager.class));
        registerProvider(applicationContext.getBean(EncounterManager.class));
        registerProvider(applicationContext.getBean(PractitionerManager.class));
        registerProvider(applicationContext.getBean(OrganizationManager.class));
        registerProvider(applicationContext.getBean(CompositionManager.class));
        registerProvider(applicationContext.getBean(CarePlanManager.class));
        registerProvider(applicationContext.getBean(ConditionManager.class));
        registerProvider(applicationContext.getBean(MedicationAdministrationManager.class));
        registerProvider(applicationContext.getBean(AllergyIntoleranceManager.class));
        registerProvider(applicationContext.getBean(CareTeamManager.class));
        registerProvider(applicationContext.getBean(DocumentReferenceManager.class));
    }
}