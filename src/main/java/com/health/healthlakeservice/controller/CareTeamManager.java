package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.dao.CareTeamDao;
import io.swagger.annotations.Api;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.ParseException;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of CareTeams.",
        tags = {"careTeam"})
/**
 * Care Team Controller
 */
public class CareTeamManager implements IResourceProvider {

    @Autowired
    private CareTeamDao careTeamDao;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return CareTeam.class;
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Care Team search endpoint
     */
    public IBaseBundle search(@OptionalParam(name= CareTeam.SP_PATIENT) StringParam patient,
                              @OptionalParam(name= CareTeam.SP_ENCOUNTER) StringParam encounter,
                              @OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(patient != null){
            if(page != null){
                return careTeamDao.findByPatient(patient.getValue(), page.getValue());
            }
            return careTeamDao.findByPatient(patient.getValue(), null);
        } else if (encounter != null){
            return careTeamDao.findByEncounter(encounter.getValue());
        }

        return careTeamDao.search();
    }

    @Search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Care Team search endpoint
     */
    public IBaseBundle search(@OptionalParam(name="page") TokenParam page) throws ParseException, IOException {

        if(page != null){
            careTeamDao.search(page.getValue());
        }
        return careTeamDao.search();
    }

    @Read()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Care Team read endpoint
     */
    public CareTeam read(@IdParam IdType id) throws IOException {

        CareTeam careTeam = careTeamDao.get(id.getIdPart());
        if (careTeam == null) {
            throw new ResourceNotFoundException(id);
        }
        return careTeam;
    }

    @Create()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Care Team create endpoint
     */
    public MethodOutcome create(@ResourceParam CareTeam careTeam) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(careTeamDao.save(careTeam));
    }

    @Update()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Care Team update endpoint
     */
    public MethodOutcome update(@IdParam IdType id, @ResourceParam CareTeam careTeam) throws IOException {

        MethodOutcome outcome = new MethodOutcome();
        return outcome.setResource(careTeamDao.update(id.getIdPart(), careTeam));
    }

    @Delete()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * Care Team delete endpoint
     */
    public void delete(@IdParam IdType id) throws IOException {

        careTeamDao.delete(id.getIdPart());
    }
}
