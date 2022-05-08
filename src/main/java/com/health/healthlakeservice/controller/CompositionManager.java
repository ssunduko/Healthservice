package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.CompositionDao;
import io.swagger.annotations.Api;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of Compositions.",
        tags = {"composition"})
/**
 * Composition Controller
 */
public class CompositionManager implements IResourceProvider {

    @Autowired
    private CompositionDao compositionDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Composition.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Composition search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= Composition.SP_PATIENT) StringParam patient,
                              @OptionalParam(name= Composition.SP_AUTHOR) StringParam author,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(patient != null){
            if(page != null){
                return compositionDao.findByPatient(patient.getValue(), page.getValue());
            }
            return compositionDao.findByPatient(patient.getValue(), null);
        } else if (author != null){
            if(page != null){
                return compositionDao.findByAuthor(author.getValue(), page.getValue());
            }
            return compositionDao.findByAuthor(author.getValue(), null);
        }

        return compositionDao.search();
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Composition search endpoint
     */
    public IBaseBundle search(@OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(page != null){
            compositionDao.search(page.getValue());
        }
        return compositionDao.search();
    }

    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Composition read endpoint
     */
    public Composition read(@IdParam IdType id) throws IOException {

        Composition composition = compositionDao.get(id.getIdPart());
        if (composition == null) {
            throw new ResourceNotFoundException(id);
        }
        return composition;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Composition create endpoint
     */
    public MethodOutcome create(@ResourceParam Composition composition) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(compositionDao.save(composition));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Composition update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam Composition composition) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(compositionDao.update(id.getIdPart(), composition));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Composition delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        compositionDao.delete(id.getIdPart());
    }
}
