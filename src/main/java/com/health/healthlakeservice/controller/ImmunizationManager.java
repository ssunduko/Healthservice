package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.ImmunizationDao;
import io.swagger.annotations.Api;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of Immunizations.",
        tags = {"immunization"})
/**
 * Immunization Controller
 */
public class ImmunizationManager implements IResourceProvider {
    
    @Autowired
    private ImmunizationDao immunizationDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Immunization.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Immunization search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= Composition.SP_PATIENT) StringParam patient,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(patient != null){
            if(page != null){
                return immunizationDao.findByPatient(patient.getValue(), page.getValue());
            }
            return immunizationDao.findByPatient(patient.getValue(), null);
        }

        return immunizationDao.search();
    }
    
    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Immunization read endpoint
     */
    public Immunization read(@IdParam IdType id) throws ParseException, IOException {

        Immunization observation = immunizationDao.get(id.getIdPart());
        if (observation == null) {
            throw new ResourceNotFoundException(id);
        }
        return observation;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Immunization create endpoint
     */
    public MethodOutcome create(@ResourceParam Immunization immunization) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(immunizationDao.save(immunization));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Immunization update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam Immunization immunization) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(immunizationDao.update(id.getIdPart(), immunization));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Immunization delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        immunizationDao.delete(id.getIdPart());
    }
}
