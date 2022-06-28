package org.capstone.job_fair.services.interfaces.account;


import org.capstone.job_fair.models.dtos.account.GenderDTO;

import java.util.List;
import java.util.Optional;

public interface GenderService {
    Optional<GenderDTO> findById(int id);

    List<GenderDTO> getAll();

    GenderDTO createGender(GenderDTO dto);

    GenderDTO deleteGender(int id);

    GenderDTO updateGender(GenderDTO dto);


}
