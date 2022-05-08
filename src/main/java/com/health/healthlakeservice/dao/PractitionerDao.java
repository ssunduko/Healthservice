package com.health.healthlakeservice.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
/**
 * FHIR Practitioner Dao
 */
public class PractitionerDao {

    private final static String practitionerEndPoint = "/r4/Practitioner/";

    @Value("${aws.healthlake.endpoint}")
    private String healthlakeEndpoint;

    @Value("${aws.healthlake.datastore}")
    private String dataStore;

    @Autowired
    private HttpClientBuilder httpClientBuilder;

    /**
     * FHIR Practitioner resource search
     * @return
     * @throws IOException
     */
    public IBaseBundle search() throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + practitionerEndPoint);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();
        
        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Practitioner resource search
     * @param identifier
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle findByIdentifier(String identifier, String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet;
        if(nextPage != null) {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + practitionerEndPoint +
                    "?identifier=" + identifier +
                    "&page=" + nextPage);
        } else {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + practitionerEndPoint +
                    "?identifier=" + identifier);
        }

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Practitioner resource read
     * @param practitionerId
     * @return
     * @throws IOException
     */
    public Practitioner get(String practitionerId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + practitionerEndPoint + practitionerId);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        String result = EntityUtils.toString(entity);

        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();

        return parser.parseResource(Practitioner.class, result);
    }

    /**
     * FHIR Practitioner resource delete
     * @param practitionerId
     * @throws IOException
     */
    public void delete(String practitionerId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpDelete httpDelete = new HttpDelete(healthlakeEndpoint + dataStore + practitionerEndPoint + practitionerId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        httpClient.execute(httpDelete);
        httpClient.close();
    }

    /**
     * FHIR Practitioner resource update
     * @param practitioner
     * @return
     * @throws IOException
     */
    public Practitioner save(Practitioner practitioner) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedPractitioner = parser.encodeResourceToString(practitioner);

        HttpPost httpPost = new HttpPost(healthlakeEndpoint + dataStore + practitionerEndPoint);

        StringEntity entity = new StringEntity(serializedPractitioner);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Practitioner.class, result);
    }

    /**
     * FHIR Practitioner resource update
     * @param practitionerId
     * @param practitioner
     * @return
     * @throws IOException
     */
    public Practitioner update(String practitionerId, Practitioner practitioner) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedPractitioner = parser.encodeResourceToString(practitioner);

        HttpPut httpPut = new HttpPut(healthlakeEndpoint + dataStore + practitionerEndPoint + practitionerId);

        StringEntity entity = new StringEntity(serializedPractitioner);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPut);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Practitioner.class, result);
    }
}
