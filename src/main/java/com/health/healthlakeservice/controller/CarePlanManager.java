package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.CarePlanDao;
import io.swagger.annotations.Api;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of CarePlans.",
        tags = {"carePlan"})
/**
 * Care Plan Controller
 */
public class CarePlanManager implements IResourceProvider {

    @Autowired
    private CarePlanDao carePlanDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return CarePlan.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Care Plan search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= Composition.SP_PATIENT) StringParam patient,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(patient != null){
            if(page != null){
                return carePlanDao.findByPatient(patient.getValue(), page.getValue());
            }
            return carePlanDao.findByPatient(patient.getValue(), null);
        }

        return carePlanDao.search();
    }
    
    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Care Plan read endpoint
     */
    public CarePlan read(@IdParam IdType id) throws IOException {

        CarePlan carePlan = carePlanDao.get(id.getIdPart());
        if (carePlan == null) {
            throw new ResourceNotFoundException(id);
        }
        return carePlan;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Care Plan create endpoint
     */
    public MethodOutcome create(@ResourceParam CarePlan carePlan) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(carePlanDao.save(carePlan));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Care Plan update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam CarePlan carePlan) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(carePlanDao.update(id.getIdPart(), carePlan));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Care Plan delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        carePlanDao.delete(id.getIdPart());
    }
}
