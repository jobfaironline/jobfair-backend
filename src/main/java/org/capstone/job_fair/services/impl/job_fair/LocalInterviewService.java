package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.dynamoDB.WaitingRoomVisitEntity;
import org.capstone.job_fair.repositories.attendant.application.ApplicationRepository;
import org.capstone.job_fair.repositories.local_dynamo.WaitingRoomVisitRepository;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Qualifier("LocalInterviewService")
public class LocalInterviewService extends InterviewServiceImpl {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private WaitingRoomVisitRepository waitingRoomVisitRepository;


    @Override
    @Transactional
    public void visitWaitingRoom(String channelId, String userId, boolean isAttendant) {
        Optional<ApplicationEntity> applicationOpt;
        if (isAttendant) {
            applicationOpt = applicationRepository.findByWaitingRoomIdAndAttendantAccountId(channelId, userId);
        } else {
            applicationOpt = applicationRepository.findByWaitingRoomIdAndInterviewerAccountId(channelId, userId);
        }

        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity application = applicationOpt.get();

        WaitingRoomVisitEntity entity = new WaitingRoomVisitEntity();
        entity.setUserId(userId);
        entity.setAttendant(isAttendant);
        entity.setChannelId(channelId);
        waitingRoomVisitRepository.save(entity);

        this.sendWaitingCountToConnectedUser(channelId, userId, application.getInterviewer().getAccountId(), false);

    }

    @Override
    @Transactional
    public void leaveWaitingRoom(String channelId, String userId, boolean isAttendant) {
        Optional<ApplicationEntity> applicationOpt;
        if (isAttendant) {
            applicationOpt = applicationRepository.findByWaitingRoomIdAndAttendantAccountId(channelId, userId);
        } else {
            applicationOpt = applicationRepository.findByWaitingRoomIdAndInterviewerAccountId(channelId, userId);
        }
        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity application = applicationOpt.get();

        List<WaitingRoomVisitEntity> queryResult = waitingRoomVisitRepository.findByChannelIdAndUserId(channelId, userId);
        waitingRoomVisitRepository.deleteAll(queryResult);
        this.sendWaitingCountToConnectedUser(channelId, userId, application.getInterviewer().getAccountId(), true);
    }

    @Override
    public List<String> getConnectedUserIds(String channelId) {
        List<WaitingRoomVisitEntity> scanResult = waitingRoomVisitRepository.findByChannelId(channelId);
        List<String> userIds = scanResult.stream().map(WaitingRoomVisitEntity::getUserId).collect(Collectors.toList());
        return userIds;

    }


}
