package org.capstone.job_fair.services.impl.notification;

import org.apache.commons.collections4.map.HashedMap;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.entities.dynamoDB.NotificationMessageEntity;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.local_dynamo.NotificationMessageRepository;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.mappers.dynamoDB.NotificationMessageMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Qualifier("LocalNotificationService")
public class LocalNotificationService implements NotificationService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private NotificationMessageMapper notificationMessageMapper;

    @Autowired
    private NotificationMessageRepository notificationMessageRepository;

    @Autowired
    private WebClient webClient;

    @Autowired
    private Clock clock;

    private void sendNotification(String notificationId) {
        try {
            final String uri = "http://localhost:4000/notification";
            Map<String, String> body = new HashedMap<>();
            body.put("notificationId", notificationId);
            webClient.post().uri(uri)
                    .body(Mono.just(body), Map.class)
                    .retrieve()
                    .bodyToMono(Void.class).subscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    @Transactional
    public void createNotification(NotificationMessageDTO message, Role role) {
        List<String> accountIdList = accountRepository.findAccountByRole(role.ordinal());
        message.setNotificationId(UUID.randomUUID().toString());
        message.setRead(false);
        Long now = clock.millis();
        message.setCreateDate(now);

        List<NotificationMessageEntity> notificationMessageEntityList = accountIdList.stream().map(s -> {
            NotificationMessageEntity entity = notificationMessageMapper.toEntity(message);
            entity.setId(UUID.randomUUID().toString());
            entity.setUserId(s);
            return entity;
        }).collect(Collectors.toList());

        notificationMessageRepository.saveAll(notificationMessageEntityList);
        notificationMessageRepository.flush();
        notificationMessageEntityList.forEach(notificationMessageEntity -> {
            this.sendNotification(notificationMessageEntity.getNotificationId());
        });
    }

    @Transactional
    protected NotificationMessageEntity createNotificationWrapper(NotificationMessageDTO message, String receiverId) {
        message.setNotificationId(UUID.randomUUID().toString());
        message.setRead(false);
        Long now = clock.millis();
        message.setCreateDate(now);

        NotificationMessageEntity entity = notificationMessageMapper.toEntity(message);
        entity.setId(UUID.randomUUID().toString());
        entity.setUserId(receiverId);

        notificationMessageRepository.save(entity);
        notificationMessageRepository.flush();
        return entity;
    }

    @Override
    public void createNotification(NotificationMessageDTO message, String receiverId) {
        NotificationMessageEntity entity = createNotificationWrapper(message, receiverId);
        this.sendNotification(entity.getNotificationId());
    }

    @Transactional
    protected List<NotificationMessageEntity> createNotificationWrapper(NotificationMessageDTO message, List<String> receiverIdList) {
        message.setRead(false);
        Long now = clock.millis();
        message.setCreateDate(now);

        List<NotificationMessageEntity> notificationMessageEntityList = receiverIdList.stream().map(s -> {
            NotificationMessageEntity entity = notificationMessageMapper.toEntity(message);
            entity.setNotificationId(UUID.randomUUID().toString());
            entity.setId(UUID.randomUUID().toString());
            entity.setUserId(s);
            return entity;
        }).collect(Collectors.toList());

        notificationMessageRepository.saveAll(notificationMessageEntityList);
        notificationMessageRepository.flush();
        return notificationMessageEntityList;
    }

    @Override
    public void createNotification(NotificationMessageDTO message, List<String> receiverIdList) {
        List<NotificationMessageEntity> notificationMessageEntityList = this.createNotificationWrapper(message, receiverIdList);
        notificationMessageEntityList.forEach(notificationMessageEntity -> {
            this.sendNotification(notificationMessageEntity.getNotificationId());
        });
    }

    @Override
    public List<NotificationMessageDTO> getNotificationByAccountId(String id) {
        List<NotificationMessageEntity> notifications = notificationMessageRepository.findByUserId(id);
        notifications.sort((o1, o2) -> Math.toIntExact(o2.getCreateDate().compareTo(o1.getCreateDate())));
        return notifications.stream().filter(notificationMessageEntity -> notificationMessageEntity.getNotificationType() == NotificationType.NOTI ).map(notificationMessageMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void readNotification(String id, String userId) {
        Optional<NotificationMessageEntity> notificationOpt = notificationMessageRepository.findById(id);
        if (!notificationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Notification.NOT_FOUND));
        }
        NotificationMessageEntity notification = notificationOpt.get();
        if (!notification.getUserId().equals(userId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Notification.NOT_FOUND));
        }
        notification.setRead(true);
        notificationMessageRepository.save(notification);
    }

    @Override
    @Transactional
    public void readAll(String userId) {
        List<NotificationMessageEntity> notifications = notificationMessageRepository
                .findByUserId(userId);
        for (NotificationMessageEntity noti : notifications) {
            noti.setRead(true);
        }
        notificationMessageRepository.saveAll(notifications);
    }
}
