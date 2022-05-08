package com.health.healthlakeservice.dao;

import com.health.healthlakeservice.model.HealthcareProject;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
/**
 * Spring Boot Dao implementation
 */
public interface ProjectDao extends PagingAndSortingRepository<HealthcareProject, String> {
}
