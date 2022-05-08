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
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
/**
 * FHIR Patient Resource Dao
 */
public class PatientDao {

    private final static String patientEndPoint = "/r4/Patient/";

    @Value("${aws.healthlake.endpoint}")
    private String healthlakeEndpoint;

    @Value("${aws.healthlake.datastore}")
    private String dataStore;

    @Autowired
    private HttpClientBuilder httpClientBuilder;

    /**
     * FHIR Patient resource search
     * @return
     * @throws IOException
     */
    public IBaseBundle search() throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + patientEndPoint);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);

    }

    /**
     * FHIR Patient resource search
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle search(String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + patientEndPoint+"?&page=" + nextPage);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Patient resource search
     * @param organization
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle findByOrganization(String organization, String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet;
        if(nextPage != null) {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + patientEndPoint +
                    "?organization=" + organization +
                    "&page=" + nextPage);
        } else {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + patientEndPoint +
                    "?organization=" + organization);
        }

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Patient resource update
     * @param patient
     * @return
     * @throws IOException
     */
    public Patient update(String patientId, Patient patient) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedPatient = parser.encodeResourceToString(patient);

        HttpPut httpPut = new HttpPut(healthlakeEndpoint + dataStore + patientEndPoint + patientId);

        StringEntity entity = new StringEntity(serializedPatient);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPut);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Patient.class, result);
    }

    /**
     * FHIR Patient resource save
     * @param patient
     * @return
     * @throws IOException
     */
    public Patient save(Patient patient) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedPatient = parser.encodeResourceToString(patient);

        HttpPost httpPost = new HttpPost(healthlakeEndpoint + dataStore + patientEndPoint);

        StringEntity entity = new StringEntity(serializedPatient);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Patient.class, result);
    }

    /**
     * FHIR Patient resource read
     * @param patientId
     * @return
     */
    public Patient get(String patientId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + patientEndPoint + patientId);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        String result = EntityUtils.toString(entity);

        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();

        return parser.parseResource(Patient.class, result);
    }

    /**
     * FHIR Patient resource delete
     * @param patientId
     * @throws IOException
     */
    public void delete(String patientId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpDelete httpDelete = new HttpDelete(healthlakeEndpoint + dataStore + patientEndPoint + patientId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        httpClient.execute(httpDelete);
        httpClient.close();
    }
}