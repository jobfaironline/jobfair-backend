package org.capstone.job_fair.services.impl.notification;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.capstone.job_fair.models.dtos.notification.NotificationMessageDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.notification.NotificationMessageEntity;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.mappers.notification.NotificationMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;


@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private AmazonDynamoDB dynamoDBClient;

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private NotificationMessageMapper notificationMessageMapper;

    @Autowired
    private AccountRepository accountRepository;

    private String queueURL = "https://sqs.ap-southeast-1.amazonaws.com/845953824246/jobhub-messageQueue";

    @Override
    public void createNotification(NotificationMessageDTO message, Role role) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);

        List<String> accountIdList = accountRepository.findAccountByRole(role.ordinal());

        message.setNotificationId(UUID.randomUUID().toString());
        message.setRead(false);
        message.setCreateDate(new Date().getTime());

        List<NotificationMessageEntity> notificationMessageEntityList = accountIdList.stream().map(s -> {
            NotificationMessageEntity entity = notificationMessageMapper.toEntity(message);
            entity.setId(UUID.randomUUID().toString());
            entity.setUserId(s);
            return entity;
        }).collect(Collectors.toList());
        dynamoDBMapper.batchSave(notificationMessageEntityList);

        SendMessageRequest sendMessageRequest = new SendMessageRequest().withMessageBody(message.getNotificationId()).withQueueUrl(queueURL);
        amazonSQS.sendMessage(sendMessageRequest);
    }

    @Override
    public void createNotification(NotificationMessageDTO message, String receiverId) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);

        message.setNotificationId(UUID.randomUUID().toString());
        message.setRead(false);
        message.setCreateDate(new Date().getTime());

        NotificationMessageEntity entity = notificationMessageMapper.toEntity(message);
        entity.setId(UUID.randomUUID().toString());
        entity.setUserId(receiverId);

        dynamoDBMapper.save(entity);

        SendMessageRequest sendMessageRequest = new SendMessageRequest().withMessageBody(message.getNotificationId()).withQueueUrl(queueURL);
        amazonSQS.sendMessage(sendMessageRequest);
    }

    @Override
    public void createNotification(NotificationMessageDTO message, List<String> receiverIdList) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);

        message.setNotificationId(UUID.randomUUID().toString());
        message.setRead(false);
        message.setCreateDate(new Date().getTime());

        List<NotificationMessageEntity> notificationMessageEntityList = receiverIdList.stream().map(s -> {
            NotificationMessageEntity entity = notificationMessageMapper.toEntity(message);
            entity.setId(UUID.randomUUID().toString());
            entity.setUserId(s);
            return entity;
        }).collect(Collectors.toList());
        dynamoDBMapper.batchSave(notificationMessageEntityList);

        SendMessageRequest sendMessageRequest = new SendMessageRequest().withMessageBody(message.getNotificationId()).withQueueUrl(queueURL);
        amazonSQS.sendMessage(sendMessageRequest);

    }


}
