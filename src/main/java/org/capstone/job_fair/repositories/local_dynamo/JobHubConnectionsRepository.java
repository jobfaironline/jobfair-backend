package org.capstone.job_fair.repositories.local_dynamo;

import org.capstone.job_fair.models.entities.dynamoDB.JobhubConnectionsEntity;
import org.capstone.job_fair.models.entities.dynamoDB.NotificationMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobHubConnectionsRepository  extends JpaRepository<JobhubConnectionsEntity, String> {
}
