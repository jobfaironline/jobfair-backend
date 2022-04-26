package org.capstone.job_fair.services.interfaces.attendant.profile;

import org.capstone.job_fair.models.dtos.attendant.profile.QualificationDTO;

import java.util.List;
import java.util.Optional;

public interface QualificationService {
    Optional<QualificationDTO> findById(int id);

    List<QualificationDTO> getAll();

    QualificationDTO delete(int id);

    QualificationDTO create(QualificationDTO dto);

    QualificationDTO update(QualificationDTO dto);
}
