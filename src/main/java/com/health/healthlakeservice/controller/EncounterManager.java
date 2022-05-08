package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.EncounterDao;
import io.swagger.annotations.Api;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Encounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of Encounters.",
        tags = {"encounter"})
/**
 * Encounter Controller
 */
public class EncounterManager implements IResourceProvider {

    @Autowired
    private EncounterDao encounterDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Encounter.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Encounter search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= Composition.SP_PATIENT) StringParam patient,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(patient != null){
            if(page != null){
                return encounterDao.findByPatient(patient.getValue(), page.getValue());
            }
            return encounterDao.findByPatient(patient.getValue(), null);
        }

        return encounterDao.search();
    }
    
    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Encounter read endpoint
     */
    public Encounter read(@IdParam IdType id) throws IOException {

        Encounter encounter = encounterDao.get(id.getIdPart());
        if (encounter == null) {
            throw new ResourceNotFoundException(id);
        }
        return encounter;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Encounter create endpoint
     */
    public MethodOutcome create(@ResourceParam Encounter encounter) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(encounterDao.save(encounter));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Encounter update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam Encounter encounter) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(encounterDao.update(id.getIdPart(), encounter));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Encounter delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        encounterDao.delete(id.getIdPart());
    }
}
