package org.capstone.job_fair.repositories.local_dynamo;

import org.capstone.job_fair.models.entities.dynamoDB.WaitingRoomVisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingRoomVisitRepository extends JpaRepository<WaitingRoomVisitEntity, String> {

    List<WaitingRoomVisitEntity> findByChannelIdAndUserId(String channelId, String userId);

    List<WaitingRoomVisitEntity> findByChannelId(String channelId);
}
