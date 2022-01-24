package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.company.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.SUB_CATEGORY)
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Integer> {

}