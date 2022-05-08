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
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
/**
 * FHIR Allergy Intolerance Resource Dao
 */
public class AllergyIntoleranceDao {
    private final static String allergyIntoleranceEndPoint = "/r4/AllergyIntolerance/";

    @Value("${aws.healthlake.endpoint}")
    private String healthlakeEndpoint;

    @Value("${aws.healthlake.datastore}")
    private String dataStore;

    @Autowired
    private HttpClientBuilder httpClientBuilder;

    /**
     * FHIR Allergy Intolerance resource read
     * @param allergyIntoleranceId
     * @return
     * @throws IOException
     */
    public AllergyIntolerance get(String allergyIntoleranceId) throws IOException {

        CloseableHttpResponse response;
        AllergyIntolerance allergyIntolerance = null;

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + allergyIntoleranceEndPoint + allergyIntoleranceId);

        response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String result = EntityUtils.toString(entity);

            FhirContext fhirContext = FhirContext.forR4();

            IParser parser = fhirContext.newJsonParser();
            allergyIntolerance = parser.parseResource(AllergyIntolerance.class, result);;
        }
        return allergyIntolerance;
    }

    /**
     * FHIR Allergy Intolerance resource search
     * @return
     * @throws IOException
     */
    public IBaseBundle search() throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + allergyIntoleranceEndPoint);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Allergy Intolerance resource search
     * @param patient
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle findByPatient(String patient, String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet;
        if(nextPage != null) {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + allergyIntoleranceEndPoint +
                    "?patient=" + patient +
                    "&page=" + nextPage);
        } else {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + allergyIntoleranceEndPoint +
                    "?patient=" + patient);
        }

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);

    }

    /**
     * FHIR Allergy Intolerance resource delete
     * @param allergyIntoleranceId
     * @throws IOException
     */
    public void delete(String allergyIntoleranceId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpDelete httpDelete = new HttpDelete(healthlakeEndpoint + dataStore + allergyIntoleranceEndPoint + allergyIntoleranceId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        httpClient.execute(httpDelete);
        httpClient.close();
    }

    /**
     * FHIR Allergy Intolerance resource save
     * @param allergyIntolerance
     * @return
     * @throws IOException
     */
    public AllergyIntolerance save(AllergyIntolerance allergyIntolerance) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedEncounter = parser.encodeResourceToString(allergyIntolerance);

        HttpPost httpPost = new HttpPost(healthlakeEndpoint + dataStore + allergyIntoleranceEndPoint);

        StringEntity entity = new StringEntity(serializedEncounter);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(AllergyIntolerance.class, result);
    }

    /**
     * FHIR Allergy Intolerance resource update
     * @param allergyIntoleranceId
     * @param allergyIntolerance
     * @return
     * @throws IOException
     */
    public AllergyIntolerance update(String allergyIntoleranceId, AllergyIntolerance allergyIntolerance) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedAllergyIntolerance = parser.encodeResourceToString(allergyIntolerance);

        HttpPut httpPut = new HttpPut(healthlakeEndpoint + dataStore + allergyIntoleranceEndPoint + allergyIntoleranceId);

        StringEntity entity = new StringEntity(serializedAllergyIntolerance);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPut);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(AllergyIntolerance.class, result);
    }
}
