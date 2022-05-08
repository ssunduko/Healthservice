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
import org.hl7.fhir.r4.model.Immunization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
/**
 * FHIR Immunization Administration Resource Dao
 */
public class ImmunizationDao {
    private final static String immunizationEndPoint = "/r4/Immunization/";

    @Value("${aws.healthlake.endpoint}")
    private String healthlakeEndpoint;

    @Value("${aws.healthlake.datastore}")
    private String dataStore;

    @Autowired
    private HttpClientBuilder httpClientBuilder;

    /**
     * FHIR Immunization resource read
     * @param observationId
     * @return
     */
    public Immunization get(String observationId) throws IOException {

        CloseableHttpResponse response;
        Immunization observation = null;

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + immunizationEndPoint + observationId);

        response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String result = EntityUtils.toString(entity);

            FhirContext ctx = FhirContext.forR4();

            IParser parser = ctx.newJsonParser();
            observation = parser.parseResource(Immunization.class, result);
        }

        return observation;
    }

    /**
     * FHIR Immunization resource search
     * @return
     * @throws IOException
     */
    public IBaseBundle search() throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + immunizationEndPoint);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Immunization resource search
     * @param patient
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle findByPatient(String patient, String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet;
        if(nextPage != null) {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + immunizationEndPoint +
                    "?patient=" + patient +
                    "&page=" + nextPage);
        } else {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + immunizationEndPoint +
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
     * FHIR Immunization resource delete
     * @param immunizationId
     * @throws IOException
     */
    public void delete(String immunizationId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpDelete httpDelete = new HttpDelete(healthlakeEndpoint + dataStore + immunizationEndPoint + immunizationId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = httpClient.execute(httpDelete);
        httpClient.close();
    }

    /**
     * FHIR Immunization resource save
     * @param immunization
     * @return
     * @throws IOException
     */
    public Immunization save(Immunization immunization) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedImmunization = parser.encodeResourceToString(immunization);
        System.out.println("Request " + serializedImmunization);

        HttpPost httpPost = new HttpPost(healthlakeEndpoint + dataStore + immunizationEndPoint);

        StringEntity entity = new StringEntity(serializedImmunization);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Immunization.class, result);
    }

    /**
     * FHIR Immunization resource update
     * @param immunizationId
     * @param immunization
     * @return
     * @throws IOException
     */
    public Immunization update(String immunizationId, Immunization immunization) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedImmunization = parser.encodeResourceToString(immunization);

        HttpPut httpPut = new HttpPut(healthlakeEndpoint + dataStore + immunizationEndPoint + immunizationId);

        StringEntity entity = new StringEntity(serializedImmunization);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPut);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Immunization.class, result);
    }
}
