package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.AllergyIntoleranceDao;
import io.swagger.annotations.Api;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of AllergyIntolerances.",
        tags = {"allergyIntolerance"})
/**
 * Allergy Intolerance Resource Controller
 */
public class AllergyIntoleranceManager implements IResourceProvider {
    
    @Autowired
    private AllergyIntoleranceDao allergyIntoleranceDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return AllergyIntolerance.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Allergy Intolerance search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= Composition.SP_PATIENT) StringParam patient,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(patient != null){
            if(page != null){
                return allergyIntoleranceDao.findByPatient(patient.getValue(), page.getValue());
            }
            return allergyIntoleranceDao.findByPatient(patient.getValue(), null);
        }

        return allergyIntoleranceDao.search();
    }
    
    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Allergy Intolerance read endpoint
     */
    public AllergyIntolerance read(@IdParam IdType id) throws IOException {

        AllergyIntolerance allergyIntolerance = allergyIntoleranceDao.get(id.getIdPart());
        if (allergyIntolerance == null) {
            throw new ResourceNotFoundException(id);
        }
        return allergyIntolerance;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Allergy Intolerance create endpoint
     */
    public MethodOutcome create(@ResourceParam AllergyIntolerance allergyIntolerance) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(allergyIntoleranceDao.save(allergyIntolerance));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Allergy Intolerance update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam AllergyIntolerance allergyIntolerance) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(allergyIntoleranceDao.update(id.getIdPart(), allergyIntolerance));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Allergy Intolerance delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        allergyIntoleranceDao.delete(id.getIdPart());
    }
}