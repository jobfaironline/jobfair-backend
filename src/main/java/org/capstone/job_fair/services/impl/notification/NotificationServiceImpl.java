package org.capstone.job_fair.services.impl.notification;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.capstone.job_fair.models.dtos.notification.NotificationDTO;
import org.capstone.job_fair.models.dtos.notification.NotificationMessageDTO;
import org.capstone.job_fair.models.entities.notification.NotificationEntity;
import org.capstone.job_fair.models.entities.notification.NotificationMessageEntity;
import org.capstone.job_fair.models.entities.notification.SendToUserEntity;
import org.capstone.job_fair.services.mappers.notification.NotificationMapper;
import org.capstone.job_fair.services.mappers.notification.NotificationMessageMapper;
import org.capstone.job_fair.services.mappers.notification.SendToUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class NotificationServiceImpl {

    @Autowired
    private AmazonDynamoDB dynamoDBClient;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private NotificationMessageMapper notificationMessageMapper;

    @Autowired
    private SendToUserMapper sendToUserMapper;

    public void createNotification(String receiverId, String message) {

    }

    public void createNotification(List<String> receiverIds, String message) {

    }

    public void createNotification(NotificationMessageDTO message, List<String> receiverIdList) {

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
        String queueURL = "https://sqs.ap-southeast-1.amazonaws.com/845953824246/jobhub-messageQueue";


        message.setNotificationId(UUID.randomUUID().toString());
        message.setRead(false);
        message.setCreateDate(new Date().getTime());

        NotificationMessageEntity notificationMessageEntity = notificationMessageMapper.toEntity(message);
        dynamoDBMapper.save(notificationMessageEntity);

        List<SendToUserEntity> sendToUserEntityList = receiverIdList.stream().map(s -> {
            SendToUserEntity entity = new SendToUserEntity(UUID.randomUUID().toString(), s, message.getNotificationId());
            return entity;
        }).collect(Collectors.toList());

        dynamoDBMapper.batchSave(sendToUserEntityList);
        SendMessageRequest sendMessageRequest = new SendMessageRequest().withMessageBody(message.getNotificationId()).withQueueUrl(queueURL);
        amazonSQS.sendMessage(sendMessageRequest);

    }


}
