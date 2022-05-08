package com.health.healthlakeservice.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
/**
 * FHIR Organization Resource Dao
 */
public class OrganizationDao {

    private final static String organizationEndPoint = "/r4/Organization/";

    @Value("${aws.healthlake.endpoint}")
    private String healthlakeEndpoint;

    @Value("${aws.healthlake.datastore}")
    private String dataStore;

    @Autowired
    private HttpClientBuilder httpClientBuilder;

    /**
     * FHIR Organization resource read
     * @param organizationId
     * @return
     */
    public Organization get(String organizationId) throws IOException {

        CloseableHttpResponse response;
        Organization organization = null;

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(healthlakeEndpoint + dataStore + organizationEndPoint + organizationId);

        response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);

            FhirContext ctx = FhirContext.forR4();

            IParser parser = ctx.newJsonParser();
            organization = parser.parseResource(Organization.class, result);

        }

        return organization;
    }

    /**
     * FHIR Organization resource delete
     * @param organizationId
     * @throws IOException
     */
    public void delete(String organizationId) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpDelete httpDelete = new HttpDelete(healthlakeEndpoint + dataStore + organizationEndPoint + organizationId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        httpClient.execute(httpDelete);
        httpClient.close();
    }

    /**
     * FHIR Organization resource save
     * @param organization
     * @return
     * @throws IOException
     */
    public Organization save(Organization organization) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedOrganization = parser.encodeResourceToString(organization);

        HttpPost httpPost = new HttpPost(healthlakeEndpoint + dataStore + organizationEndPoint);

        StringEntity entity = new StringEntity(serializedOrganization);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Organization.class, result);
    }

    /**
     * FHIR Organization resource update
     * @param organizationId
     * @param organization
     * @return
     * @throws IOException
     */
    public Organization update(String organizationId, Organization organization) throws IOException {

        CloseableHttpClient httpClient = httpClientBuilder.build();

        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();

        String serializedOrganization = parser.encodeResourceToString(organization);

        HttpPut httpPut = new HttpPut(healthlakeEndpoint + dataStore + organizationEndPoint + organizationId);

        StringEntity entity = new StringEntity(serializedOrganization);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPut);
        httpClient.close();

        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);

        return parser.parseResource(Organization.class, result);
    }
}
