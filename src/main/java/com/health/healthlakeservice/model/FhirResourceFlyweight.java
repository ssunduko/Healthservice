package com.health.healthlakeservice.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IDomainResource;
import lombok.Getter;
import lombok.NoArgsConstructor;;


@DynamoDBDocument
@Getter
@NoArgsConstructor
/**
 * FHIR Resource wrapper
 */
public class FhirResourceFlyweight {

    private String resourceId;
    private String resourceType;

    public FhirResourceFlyweight(IDomainResource domainResource) {
        resourceId = domainResource.getId();
        resourceType = domainResource.fhirType();
    }

    @DynamoDBAttribute(attributeName="resourceId")
    public String getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(String id) {
        this.resourceId = id;
    }

    @DynamoDBAttribute(attributeName="resourceType")
    public String getResourceType() {
        return this.resourceType;
    }

    public void setResourceType(String type) {
        this.resourceType = type;
    }

    @Override
    public String toString() {
        return "FhirResourceFlyweight{" +
                "ResourceId='" + resourceId + '\'' +
                ", ResourceType=" + resourceType +
                '}';
    }
}
