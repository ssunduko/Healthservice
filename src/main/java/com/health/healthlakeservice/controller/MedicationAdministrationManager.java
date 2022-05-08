package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.MedicationAdministrationDao;
import io.swagger.annotations.Api;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of MedicationAdministrations.",
        tags = {"medicationAdministration"})
/**
 * Medication Administration Controller
 */
public class MedicationAdministrationManager implements IResourceProvider {

    @Autowired
    private MedicationAdministrationDao medicationAdministrationDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return MedicationAdministration.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Medication Administration search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= Composition.SP_PATIENT) StringParam patient,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(patient != null){
            if(page != null){
                return medicationAdministrationDao.findByPatient(patient.getValue(), page.getValue());
            }
            return medicationAdministrationDao.findByPatient(patient.getValue(), null);
        }

        return medicationAdministrationDao.search();
    }
    
    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Medication Administration read endpoint
     */
    public MedicationAdministration read(@IdParam IdType id) throws IOException {

        MedicationAdministration medicationAdministration = medicationAdministrationDao.get(id.getIdPart());
        if (medicationAdministration == null) {
            throw new ResourceNotFoundException(id);
        }
        return medicationAdministration;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Medication Administration create endpoint
     */
    public MethodOutcome create(@ResourceParam MedicationAdministration medicationAdministration) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(medicationAdministrationDao.save(medicationAdministration));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Medication Administration update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam MedicationAdministration medicationAdministration) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(medicationAdministrationDao.update(id.getIdPart(), medicationAdministration));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Medication Administration delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        medicationAdministrationDao.delete(id.getIdPart());
    }
}
