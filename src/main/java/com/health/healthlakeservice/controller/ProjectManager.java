package com.health.healthlakeservice.controller;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.health.healthlakeservice.model.FhirResourceFlyweight;
import com.health.healthlakeservice.model.HealthcareProject;
import com.health.healthlakeservice.dao.ProjectDao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/")
@SecurityRequirement(name = "capstone")
/**
 * Project Controller
 */
public class ProjectManager {

    @Autowired
    private ProjectDao projectDao;


    @Operation(security = @SecurityRequirement(name = "capstone"))
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(path = "/Project")
    /**
     * Project create endpoint
     */
    public HealthcareProject addProject(@RequestBody HealthcareProject healthcareProject) {

        return projectDao.save(healthcareProject);

    }
    @Operation(security = @SecurityRequirement(name = "capstone"))
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/Project/{id}")
    /**
     * Project read endpoint
     */
    public ResponseEntity<HealthcareProject> getProject(@PathVariable(value = "id") String projectId)
            throws ResourceNotFoundException {

        Optional<HealthcareProject> project = projectDao.findById(projectId);
        return ResponseEntity.ok().body(project.get());
    }

    @Operation(security = @SecurityRequirement(name = "capstone"))
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/Project")
    /**
     * Project search endpoint
     */
    public List<HealthcareProject> search() {

        Iterable<HealthcareProject> iterable = projectDao.findAll();
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Operation(security = @SecurityRequirement(name = "capstone"))
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/Project/Resource/{id}")
    /**
     * Project update endpoint
     */
    public HealthcareProject updateProject(@RequestBody FhirResourceFlyweight fhirResource,
                                                    @PathVariable(value = "id") String projectId) {

        Optional<HealthcareProject> project = projectDao.findById(projectId);

        if(project.isPresent()){
            HealthcareProject tempProject = project.get();
            tempProject.addResource(fhirResource);
            return projectDao.save(tempProject);
        }

        return null;
    }

    @Operation(security = @SecurityRequirement(name = "capstone"))
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/Project/{id}")
    /**
     * Project update endpoint
     */
    public HealthcareProject updateProject(@RequestBody HealthcareProject healthcareProject,
                                           @PathVariable(value = "id") String projectId) {

        Optional<HealthcareProject> project = projectDao.findById(projectId);

        if(project.isPresent()){
            HealthcareProject tempProject = project.get();
            tempProject.setFhirResources(healthcareProject.getFhirResources());
            return projectDao.save(tempProject);
        }

        return null;
    }

    @Operation(security = @SecurityRequirement(name = "capstone"))
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/Project/{id}")
    /**
     * Project update endpoint
     */
    void deleteEmployee(@PathVariable(value = "id") String projectId) {
        projectDao.deleteById(projectId);
    }
}
