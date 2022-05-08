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
import org.hl7.fhir.r4.model.DocumentReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
/**
 * FHIR Document Reference Resource Dao
 */
public class DocumentReferenceDao {

    private final static String documentReferenceEndPoint = "/r4/DocumentReference/";

    @Value("${aws.healthlake.endpoint}")
    private String healthlakeEndpoint;

    @Value("${aws.healthlake.datastore}")
    private String dataStore;

    @Autowired
    private HttpClientBuilder httpClientBuilder;

    /**
     * FHIR Document Reference resource read
     * @param documentReferenceId
     * @return
     * @throws IOException
     */
    public DocumentReference get(String documentReferenceId) throws IOException {

        CloseableHttpResponse response;
        DocumentReference documentReference = null;

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + documentReferenceEndPoint + documentReferenceId);

        response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);

            FhirContext fhirContext = FhirContext.forR4();

            IParser parser = fhirContext.newJsonParser();
            documentReference = parser.parseResource(DocumentReference.class, result);
        }

        return documentReference;
    }

    /**
     * FHIR Document Reference resource search
     * @return
     * @throws IOException
     */
    public IBaseBundle search() throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + documentReferenceEndPoint);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Document Reference resource search
     * @param patient
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle findByPatient(String patient, String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet;
        if(nextPage != null) {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + documentReferenceEndPoint +
                    "?subject=" + patient +
                    "&page=" + nextPage);
        } else {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + documentReferenceEndPoint +
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
     * FHIR Document Reference resource search
     * @param encounter
     * @param nextPage
     * @return
     * @throws IOException
     */
    public IBaseBundle findByEncounter(String encounter, String nextPage) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet;
        if(nextPage != null) {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + documentReferenceEndPoint +
                    "?encounter=" + encounter +
                    "&page=" + nextPage);
        } else {
            httpGet = new HttpGet(healthlakeEndpoint + dataStore + documentReferenceEndPoint +
                    "?encounter=" + encounter);
        }

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entities = response.getEntity();

        String result = EntityUtils.toString(entities);

        FhirContext fhirContext = FhirContext.forR4();

        IParser parser = fhirContext.newJsonParser();
        return parser.parseResource(Bundle.class,result);
    }

    /**
     * FHIR Document Reference resource delete
     * @param documentReferenceId
     * @throws IOException
     */
    public void delete(String documentReferenceId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpDelete httpDelete = new HttpDelete(healthlakeEndpoint + dataStore + documentReferenceEndPoint + documentReferenceId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        httpClient.execute(httpDelete);
        httpClient.close();
    }

    /**
     * FHIR Document Reference resource ave
     * @param documentReference
     * @return
     * @throws IOException
     */
    public DocumentReference save(DocumentReference documentReference) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedDocumentReference = parser.encodeResourceToString(documentReference);

        HttpPost httpPost = new HttpPost(healthlakeEndpoint + dataStore + documentReferenceEndPoint);

        StringEntity entity = new StringEntity(serializedDocumentReference);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(DocumentReference.class, result);
    }

    /**
     * FHIR Document Reference resource update
     * @param documentReferenceId
     * @param documentReference
     * @return
     * @throws IOException
     */
    public DocumentReference update(String documentReferenceId, DocumentReference documentReference) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedDocumentReference = parser.encodeResourceToString(documentReference);

        HttpPut httpPut = new HttpPut(healthlakeEndpoint + dataStore + documentReferenceEndPoint + documentReferenceId);

        StringEntity entity = new StringEntity(serializedDocumentReference);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPut);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(DocumentReference.class, result);
    }
}
