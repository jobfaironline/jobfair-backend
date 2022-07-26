package org.capstone.job_fair.repositories.local_dynamo;

import org.capstone.job_fair.models.entities.dynamoDB.NotificationMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationMessageRepository extends JpaRepository<NotificationMessageEntity, String> {
    List<NotificationMessageEntity> findByUserId(String userId);
}
