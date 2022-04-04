package org.capstone.job_fair.repositories.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRegistrationAdminRepository extends JpaRepository<CompanyRegistrationAdminEntity, String> {
    @Query(value = "select cr.id, cr.create_date AS createDate, cr.description, cr.job_fair_id AS jobFairId, cr.company_id AS companyId" +
            ", cr.status, cr.cancel_reason as cancelReason, " +
            "cr.admin_message as adminMessage, cr.authorizer_id AS authorizerId, cr.creator_id as creatorId,\n" +
            "       jf.name as jobfairName, c.name as companyName\n" +
            "from company_registration cr\n" +
            "join job_fair jf on cr.job_fair_id = jf.id\n" +
            "join company c on cr.company_id = c.id\n" +
            "\n" +
            "where jf.name LIKE :jobfairName AND c.name LIKE :companyName AND find_in_set(cr.status, :statuses) " +
            "ORDER BY\n" +
            "   CASE WHEN :sortDirection='ASC' then :sortBy end ASC,\n" +
            "   CASE WHEN :sortDirection='DESC' then :sortBy end DESC", nativeQuery = true)
    Page<CompanyRegistrationAdminEntity> findAllByCompanyNameAndJobFairNameAndStatusIn(@Param("companyName") String companyName,
                                                                                       @Param("jobfairName") String jobfairName,
                                                                                       @Param("statuses") String statuses,
                                                                                       @Param("sortBy") String sortBy,
                                                                                       @Param("sortDirection") String sortDirection,
                                                                                       Pageable pageable);
}
