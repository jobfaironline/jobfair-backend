package org.capstone.job_fair.services;

import org.capstone.job_fair.models.entities.attendant.GenderEntity;


public interface GenderService {
    GenderEntity findById(int id);
}
