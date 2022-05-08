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
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
/**
 * FHIR Medication Administration Resource Dao
 */
public class MedicationAdministrationDao {

    private final static String medicationAdministrationEndPoint = "/r4/MedicationAdministration/";

    @Value("${aws.healthlake.endpoint}")
    private String healthlakeEndpoint;

    @Value("${aws.healthlake.datastore}")
    private String dataStore;

    @Autowired
    private HttpClientBuilder httpClientBuilder;

    /**
     * FHIR Medication Administration resource read
     * @param medicationAdministrationId
     * @return
     * @throws IOException
     */
    public MedicationAdministration get(String medicationAdministrationId) throws IOException {

        CloseableHttpResponse response = null;
        MedicationAdministration medicationAdministration = null;

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + medicationAdministrationEndPoint + medicationAdministrationId);

        response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);

            FhirContext fhirContext = FhirContext.forR4();

            IParser parser = fhirContext.newJsonParser();
            medicationAdministration = parser.parseResource(MedicationAdministration.class, result);
        }

        return medicationAdministration;
    }

    /**
     * FHIR Medication Administration resource search
     * @return
     * @throws IOException
     */
    public IBaseBundle search() throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + medicationAdministrationEndPoint);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Medication Administration resource search
     * @param patient
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle findByPatient(String patient, String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet;
        if(nextPage != null) {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + medicationAdministrationEndPoint +
                    "?subject=" + patient +
                    "&page=" + nextPage);
        } else {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + medicationAdministrationEndPoint +
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
     * FHIR Medication Administration resource delete
     * @param medicationAdministrationId
     * @throws IOException
     */
    public void delete(String medicationAdministrationId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpDelete httpDelete = new HttpDelete(healthlakeEndpoint + dataStore + medicationAdministrationEndPoint + medicationAdministrationId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        httpClient.execute(httpDelete);
        httpClient.close();
    }

    /**
     * FHIR Medication Administration resource save
     * @param medicationAdministration
     * @return
     * @throws IOException
     */
    public MedicationAdministration save(MedicationAdministration medicationAdministration) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedEncounter = parser.encodeResourceToString(medicationAdministration);

        HttpPost httpPost = new HttpPost(healthlakeEndpoint + dataStore + medicationAdministrationEndPoint);

        StringEntity entity = new StringEntity(serializedEncounter);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(MedicationAdministration.class, result);
    }

    /**
     * FHIR Medication Administration resource update
     * @param medicationAdministrationId
     * @param medicationAdministration
     * @return
     * @throws IOException
     */
    public MedicationAdministration update(String medicationAdministrationId, MedicationAdministration medicationAdministration) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedMedicationAdministration = parser.encodeResourceToString(medicationAdministration);

        HttpPut httpPut = new HttpPut(healthlakeEndpoint + dataStore + medicationAdministrationEndPoint + medicationAdministrationId);

        StringEntity entity = new StringEntity(serializedMedicationAdministration);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPut);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);
        return parser.parseResource(MedicationAdministration.class, result);
    }
}
