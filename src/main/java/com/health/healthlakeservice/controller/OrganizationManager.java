package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.OrganizationDao;
import io.swagger.annotations.Api;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of Organizations.",
        tags = {"organization"})
/**
 * Organization Controller
 */
public class OrganizationManager implements IResourceProvider {
    
    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Organization.class;
    }

    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Organization search endpoint
     */
    public Organization read(@IdParam IdType id) throws ParseException, IOException {

        Organization organization = organizationDao.get(id.getIdPart());
        if (organization == null) {
            throw new ResourceNotFoundException(id);
        }
        return organization;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Organization create endpoint
     */
    public MethodOutcome create(@ResourceParam Organization organization) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(organizationDao.save(organization));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Organization update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam Organization organization) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(organizationDao.update(id.getIdPart(), organization));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Organization delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        organizationDao.delete(id.getIdPart());
    }
}
