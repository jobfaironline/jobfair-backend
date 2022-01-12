package org.capstone.job_fair.services.interfaces.account;



import org.capstone.job_fair.models.entities.account.GenderEntity;

import java.util.Optional;

public interface GenderService {
    Optional<GenderEntity> findById(int id);
}
