package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.ConditionDao;
import io.swagger.annotations.Api;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of Conditions.",
        tags = {"condition"})
/**
 * Condition Controller
 */
public class ConditionManager implements IResourceProvider {
    
    @Autowired
    private ConditionDao conditionDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Condition.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Condition search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= Composition.SP_PATIENT) StringParam patient,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(patient != null){
            if(page != null){
                return conditionDao.findByPatient(patient.getValue(), page.getValue());
            }
            return conditionDao.findByPatient(patient.getValue(), null);
        }
        return conditionDao.search();
    }
    
    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Condition read endpoint
     */
    public Condition read(@IdParam IdType id) throws IOException {

        Condition condition = conditionDao.get(id.getIdPart());
        if (condition == null) {
            throw new ResourceNotFoundException(id);
        }
        return condition;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Condition create endpoint
     */
    public MethodOutcome create(@ResourceParam Condition condition) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(conditionDao.save(condition));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Condition update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam Condition condition) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(conditionDao.update(id.getIdPart(), condition));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Condition delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        conditionDao.delete(id.getIdPart());
    }
}
