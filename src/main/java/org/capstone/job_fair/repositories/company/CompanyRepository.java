package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedNativeQuery;
import java.util.Optional;


@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {
    Optional<CompanyEntity> findByTaxId(String taxId);

    Integer countByEmail(String email);

    Integer countByTaxId(String taxId);

    Integer countById(String id);

    Integer countByIdAndTaxId(String id, String taxId);

    @Query(value = "SELECT id FROM company WHERE\n" +
            "    id = (SELECT company_id FROM company_registration WHERE\n" +
            "            id = (SELECT company_registration_id FROM `order` WHERE\n" +
            "                id = (SELECT order_id FROM company_booth WHERE id = :id)))", nativeQuery = true)
    Optional<String> findCompanyIdByCompanyBoothID(@Param("id") String companyBoothId);

}