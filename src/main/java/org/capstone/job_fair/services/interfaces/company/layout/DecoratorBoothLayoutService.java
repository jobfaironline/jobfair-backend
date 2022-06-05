package org.capstone.job_fair.services.interfaces.company.layout;

import org.capstone.job_fair.models.dtos.company.layout.DecoratorBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.company.layout.DecoratorBoothLayoutVideoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface DecoratorBoothLayoutService {
    List<DecoratorBoothLayoutDTO> getLayoutsByCompanyEmployeeId(String employeeId);

    Optional<DecoratorBoothLayoutDTO> getById(String id);

    DecoratorBoothLayoutDTO create(String companyEmployeeId, String name, MultipartFile file);

    DecoratorBoothLayoutVideoDTO createNewVideoWithFile(String layoutId, String itemName);

    DecoratorBoothLayoutVideoDTO createNewVideoWithUrl(String layoutId, String itemName, String url);
}
