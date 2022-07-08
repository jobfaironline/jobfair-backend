package org.capstone.job_fair.services.interfaces.matching_point;

import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import reactor.core.publisher.Mono;

public interface MatchingPointService {
    Mono<ApplicationEntity> calculateFromApplication(String applicationId);

    Mono<Void> calculateBetweenCVAndBoothJobPosition(String cvId, String jobPositionId);

    Mono<Void> calculateBetweenProfileAndBoothJobPosition(String attendantId, String jobPositonId);
}
