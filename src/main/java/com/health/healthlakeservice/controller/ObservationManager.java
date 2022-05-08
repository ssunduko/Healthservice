package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.ObservationDao;
import io.swagger.annotations.Api;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of Observations.",
        tags = {"observation"})
/**
 * Observation Controller
 */
public class ObservationManager implements IResourceProvider {

    @Autowired
    private ObservationDao observationDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Observation.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Observation search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= Composition.SP_PATIENT) StringParam patient,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(patient != null){
            if(page != null){
                return observationDao.findByPatient(patient.getValue(), page.getValue());
            }
            return observationDao.findByPatient(patient.getValue(), null);
        }

        return observationDao.search();
    }

    @Read()
    /**
     * Observation read endpoint
     */
    public Observation read(@IdParam IdType id) throws ParseException, IOException {

        Observation observation = observationDao.get(id.getIdPart());
        if (observation == null) {
            throw new ResourceNotFoundException(id);
        }
        return observation;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Observation create endpoint
     */
    public MethodOutcome create(@ResourceParam Observation observation) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(observationDao.save(observation));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Observation update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam Observation observation) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(observationDao.update(id.getIdPart(), observation));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Observation delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        observationDao.delete(id.getIdPart());
    }
}
