package org.capstone.job_fair.repositories.local_dynamo;

import org.capstone.job_fair.models.entities.dynamoDB.NotificationMessageEntity;
import org.capstone.job_fair.models.entities.job_fair.AttendantRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationMessageRepository extends JpaRepository<NotificationMessageEntity, String> {
    List<NotificationMessageEntity> findByUserId(String userId);
}
