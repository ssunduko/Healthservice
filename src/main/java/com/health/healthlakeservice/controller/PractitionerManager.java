package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.PractitionerDao;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "capstone")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of Practitioners.",
        tags = {"practitioner"})
/**
 * Practitioner Controller
 */
public class PractitionerManager implements IResourceProvider {
    
    @Autowired
    private PractitionerDao practitionerDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Practitioner.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Practitioner search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= Practitioner.SP_IDENTIFIER) StringParam identifier,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(identifier != null){
            if(page != null){
                return practitionerDao.findByIdentifier(identifier.getValue(), page.getValue());
            }
            return practitionerDao.findByIdentifier(identifier.getValue(), null);
        }
        return practitionerDao.search();
    }

    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Practitioner read endpoint
     */
    public Practitioner read(@IdParam IdType id) throws ParseException, IOException {

        Practitioner practitioner = practitionerDao.get(id.getIdPart());
        if (practitioner == null) {
            throw new ResourceNotFoundException(id);
        }
        return practitioner;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Practitioner create endpoint
     */
    public MethodOutcome create(@ResourceParam Practitioner practitioner) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(practitionerDao.save(practitioner));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Practitioner update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam Practitioner practitioner) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(practitionerDao.update(id.getIdPart(), practitioner));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Practitioner delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        practitionerDao.delete(id.getIdPart());
    }
}
