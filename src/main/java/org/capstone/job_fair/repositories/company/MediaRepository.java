package org.capstone.job_fair.repositories.company;

import io.swagger.annotations.Api;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.company.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;


@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.MEDIA)
public interface MediaRepository extends JpaRepository<MediaEntity, String> {

    @Override
    @RestResource(exported = false)
    @ApiIgnore
    void deleteById(String s);

    @Override
    @RestResource(exported = false)
    @ApiIgnore
    void delete(MediaEntity entity);
}
