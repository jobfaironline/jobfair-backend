package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.CompanyMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MediaRepository extends JpaRepository<CompanyMediaEntity, String> {

    void deleteById(String s);

    void delete(CompanyMediaEntity entity);
}
