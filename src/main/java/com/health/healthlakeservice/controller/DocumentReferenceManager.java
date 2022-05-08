package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.DocumentReferenceDao;
import io.swagger.annotations.Api;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of DocumentReferences.",
        tags = {"documentReference"})
/**
 * Document Reference Controller
 */
public class DocumentReferenceManager implements IResourceProvider {

    @Autowired
    private DocumentReferenceDao documentReferenceDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return DocumentReference.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Document Reference search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= Composition.SP_PATIENT) StringParam patient,
                              @OptionalParam(name= Composition.SP_ENCOUNTER) StringParam encounter,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(patient != null){
            if(page != null){
                return documentReferenceDao.findByPatient(patient.getValue(), page.getValue());
            }
            return documentReferenceDao.findByPatient(patient.getValue(), null);
        } else if (encounter != null){
            if(page != null){
                return documentReferenceDao.findByEncounter(encounter.getValue(), page.getValue());
            }
            return documentReferenceDao.findByEncounter(encounter.getValue(), null);
        }

        return documentReferenceDao.search();
    }

    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Document Reference read endpoint
     */
    public DocumentReference read(@IdParam IdType id) throws IOException {

        DocumentReference documentReference = documentReferenceDao.get(id.getIdPart());
        if (documentReference == null) {
            throw new ResourceNotFoundException(id);
        }
        return documentReference;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Document Reference create endpoint
     */
    public MethodOutcome create(@ResourceParam DocumentReference documentReference) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(documentReferenceDao.save(documentReference));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Document Reference update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam DocumentReference documentReference) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(documentReferenceDao.update(id.getIdPart(), documentReference));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Document Reference delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        documentReferenceDao.delete(id.getIdPart());
    }
}
