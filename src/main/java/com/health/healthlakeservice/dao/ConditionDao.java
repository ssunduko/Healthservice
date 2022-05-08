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
import org.hl7.fhir.r4.model.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
/**
 * FHIR Condition Resource Dao
 */
public class ConditionDao {
    private final static String conditionEndPoint = "/r4/Condition/";

    @Value("${aws.healthlake.endpoint}")
    private String healthlakeEndpoint;

    @Value("${aws.healthlake.datastore}")
    private String dataStore;

    @Autowired
    private HttpClientBuilder httpClientBuilder;

    /**
     * FHIR Condition resource read
     * @param conditionId
     * @return
     * @throws IOException
     */
    public Condition get(String conditionId) throws IOException {

        CloseableHttpResponse response = null;
        Condition condition = null;

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + conditionEndPoint + conditionId);

        response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);

            FhirContext fhirContext = FhirContext.forR4();

            IParser parser = fhirContext.newJsonParser();
            condition = parser.parseResource(Condition.class, result);

            String serialized = parser.encodeResourceToString(condition);
        }
        return condition;
    }

    /**
     * FHIR Condition resource search
     * @return
     * @throws IOException
     */
    public IBaseBundle search() throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + conditionEndPoint);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Condition resource search
     * @param patient
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle findByPatient(String patient, String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet;
        if(nextPage != null) {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + conditionEndPoint +
                    "?subject=" + patient +
                    "&page=" + nextPage);
        } else {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + conditionEndPoint +
                    "?subject=" + patient);
        }

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Condition resource search
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle search(String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + conditionEndPoint+"?&page=" + nextPage);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Condition resource delete
     * @param conditionId
     * @throws IOException
     */
    public void delete(String conditionId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpDelete httpDelete = new HttpDelete(healthlakeEndpoint + dataStore + conditionEndPoint + conditionId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        httpClient.execute(httpDelete);
        httpClient.close();
    }

    /**
     * FHIR Condition resource save
     * @param condition
     * @return
     * @throws IOException
     */
    public Condition save(Condition condition) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedEncounter = parser.encodeResourceToString(condition);

        HttpPost httpPost = new HttpPost(healthlakeEndpoint + dataStore + conditionEndPoint);

        StringEntity entity = new StringEntity(serializedEncounter);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Condition.class, result);
    }

    /**
     * FHIR Condition resource update
     * @param conditionId
     * @param condition
     * @return
     * @throws IOException
     */
    public Condition update(String conditionId, Condition condition) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedCondition = parser.encodeResourceToString(condition);

        HttpPut httpPut = new HttpPut(healthlakeEndpoint + dataStore + conditionEndPoint + conditionId);

        StringEntity entity = new StringEntity(serializedCondition);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPut);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Condition.class, result);
    }
}
