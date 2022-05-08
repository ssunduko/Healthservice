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
import org.hl7.fhir.r4.model.CarePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
/**
 * FHIR Care Plan Resource Dao
 */
public class CarePlanDao {

    private final static String carePlanEndPoint = "/r4/CarePlan/";

    @Value("${aws.healthlake.endpoint}")
    private String healthlakeEndpoint;

    @Value("${aws.healthlake.datastore}")
    private String dataStore;

    @Autowired
    private HttpClientBuilder httpClientBuilder;

    /**
     * FHIR Care Plan resource read
     * @param carePlanId
     * @return
     * @throws IOException
     */
    public CarePlan get(String carePlanId) throws IOException {

        CloseableHttpResponse response;
        CarePlan carePlan = null;

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + carePlanEndPoint + carePlanId);

        response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);

            FhirContext fhirContext = FhirContext.forR4();

            IParser parser = fhirContext.newJsonParser();
            carePlan = parser.parseResource(CarePlan.class, result);
        }

        return carePlan;
    }

    /**
     * FHIR Care Plan resource search
     * @return
     * @throws IOException
     */
    public IBaseBundle search() throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + carePlanEndPoint);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Care Plan resource search
     * @param patient
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle findByPatient(String patient, String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet;
        if(nextPage != null) {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + carePlanEndPoint +
                    "?subject=" + patient +
                    "&page=" + nextPage);
        } else {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + carePlanEndPoint +
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
     * FHIR Care Plan resource delete
     * @param carePlanId
     * @throws IOException
     */
    public void delete(String carePlanId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpDelete httpDelete = new HttpDelete(healthlakeEndpoint + dataStore + carePlanEndPoint + carePlanId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        httpClient.execute(httpDelete);
        httpClient.close();
    }

    /**
     * FHIR Care Plan resource save
     * @param carePlan
     * @return
     * @throws IOException
     */
    public CarePlan save(CarePlan carePlan) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedEncounter = parser.encodeResourceToString(carePlan);

        HttpPost httpPost = new HttpPost(healthlakeEndpoint + dataStore + carePlanEndPoint);

        StringEntity entity = new StringEntity(serializedEncounter);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);
        System.out.println("Response " + result);

        return parser.parseResource(CarePlan.class, result);
    }

    public CarePlan update(String carePlanId, CarePlan carePlan) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedCarePlan = parser.encodeResourceToString(carePlan);
        System.out.println("Request " + serializedCarePlan);

        HttpPut httpPut = new HttpPut(healthlakeEndpoint + dataStore + carePlanEndPoint + carePlanId);

        StringEntity entity = new StringEntity(serializedCarePlan);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPut);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);
        System.out.println("Response " + result);

        return parser.parseResource(CarePlan.class, result);
    }
}
