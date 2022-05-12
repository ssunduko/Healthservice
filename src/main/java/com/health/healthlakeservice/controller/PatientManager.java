package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.PatientDao;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "capstone")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of Patients.",
        tags = {"patient"})
/**
 * Patient Controller
 * This was adapted from a post "INTRODUCTION TO HAPI FHIR"
 * by Youri Vermeir
 * https://ordina-jworks.github.io/ehealth/2021/02/23/hapi-fhir.html
 */
public class PatientManager implements IResourceProvider {

    @Autowired
    private PatientDao patientDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Patient.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Patient search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= Patient.SP_ORGANIZATION) StringParam organization,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(organization != null){
            if(page != null){
                return patientDao.findByOrganization(organization.getValue(), page.getValue());
            }
            return patientDao.findByOrganization(organization.getValue(), null);
        }
        return patientDao.search();
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Patient search endpoint
     */
    public IBaseBundle search(@OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(page != null){
            patientDao.search(page.getValue());
        }
        return patientDao.search();
    }

    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Patient read endpoint
     */
    public Patient read(@IdParam IdType id) throws ParseException, IOException {

        Patient patient = patientDao.get(id.getIdPart());
        if (patient == null) {
            throw new ResourceNotFoundException(id);
        }
        return patient;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Patient create endpoint
     */
    public MethodOutcome create(@ResourceParam Patient patient) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(patientDao.save(patient));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Patient update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam Patient patient) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(patientDao.update(id.getIdPart(), patient));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Patient delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        patientDao.delete(id.getIdPart());
    }
}
