package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface LayoutService {
    List<LayoutDTO> getAllTemplateLayout();

    List<LayoutDTO> getCompanyLayout(String companyId);

    Optional<LayoutDTO> findByIdAndCompanyId(String id, String companyId);

    LayoutDTO createNew(LayoutDTO dto);

    LayoutDTO update(LayoutDTO dto);

    void validateAndGenerateBoothSlot(MultipartFile file, String layoutId);

    Optional<LayoutDTO> getByJobFairId(String jobFairId);

    Optional<LayoutDTO> getByJobFairIdWithAvailableBoothSlot(String jobFairId);

    void pickJobFairLayout(String jobFairId, String layoutId, String companyId);

}
