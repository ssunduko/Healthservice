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
import org.hl7.fhir.r4.model.Composition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
/**
 * FHIR Composition Resource Dao
 */
public class CompositionDao {

    private final static String compositionEndPoint = "/r4/Composition/";

    @Value("${aws.healthlake.endpoint}")
    private String healthlakeEndpoint;

    @Value("${aws.healthlake.datastore}")
    private String dataStore;

    @Autowired
    private HttpClientBuilder httpClientBuilder;

    /**
     * FHIR Composition resource search
     * @return
     * @throws IOException
     */
    public IBaseBundle search() throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + compositionEndPoint);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Composition resource search
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle search(String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + compositionEndPoint+"?&page=" + nextPage);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Composition resource search
     * @param patient
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle findByPatient(String patient, String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet;
        if(nextPage != null) {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + compositionEndPoint +
                    "?patient=" + patient +
                    "&page=" + nextPage);
        } else {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + compositionEndPoint +
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
     * FHIR Composition resource search
     * @param author
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle findByAuthor(String author, String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet;
        if(nextPage != null) {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + compositionEndPoint +
                    "?author=" + author +
                    "&page=" + nextPage);
        } else {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + compositionEndPoint +
                    "?author=" + author);
        }

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Composition resource get
     * @param compositionId
     * @return
     * @throws IOException
     */
    public Composition get(String compositionId) throws IOException {

        CloseableHttpResponse response;
        Composition composition = null;

        CloseableHttpClient httpClient = httpClientBuilder.build();

        System.out.println("Request " + healthlakeEndpoint + dataStore + compositionEndPoint + compositionId);

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + compositionEndPoint + compositionId);

        response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);

            FhirContext fhirContext = FhirContext.forR4();

            IParser parser = fhirContext.newJsonParser();
            composition = parser.parseResource(Composition.class, result);
        }
        return composition;
    }

    /**
     * FHIR Composition resource delete
     * @param compositionId
     * @throws IOException
     */
    public void delete(String compositionId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpDelete httpDelete = new HttpDelete(healthlakeEndpoint + dataStore + compositionEndPoint + compositionId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        httpClient.execute(httpDelete);
        httpClient.close();
    }

    /**
     * FHIR Composition resource save
     * @param composition
     * @return
     * @throws IOException
     */
    public Composition save(Composition composition) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedEncounter = parser.encodeResourceToString(composition);

        HttpPost httpPost = new HttpPost(healthlakeEndpoint + dataStore + compositionEndPoint);

        StringEntity entity = new StringEntity(serializedEncounter);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Composition.class, result);
    }

    /**
     * FHIR Composition resource update
     * @param compositionId
     * @param composition
     * @return
     * @throws IOException
     */
    public Composition update(String compositionId, Composition composition) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedComposition = parser.encodeResourceToString(composition);

        HttpPut httpPut = new HttpPut(healthlakeEndpoint + dataStore + compositionEndPoint + compositionId);

        StringEntity entity = new StringEntity(serializedComposition);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPut);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Composition.class, result);
    }
}
