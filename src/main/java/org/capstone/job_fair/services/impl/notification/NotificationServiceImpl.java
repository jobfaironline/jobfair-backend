package org.capstone.job_fair.services.impl.notification;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.notification.NotificationMessageDTO;
import org.capstone.job_fair.models.entities.notification.NotificationMessageEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.mappers.notification.NotificationMessageMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
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

    @Value("${aws.sqs.url}")
    private String queueURL;

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
        entity.setUserId(message.getUserId());

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

    @Override
    public List<NotificationMessageDTO> getNotificationByAccountId(String id) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);

        HashMap<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId", new AttributeValue().withS(id));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("userId = :userId")
                .withExpressionAttributeValues(eav);


        List<NotificationMessageEntity> notifications = dynamoDBMapper.scan(NotificationMessageEntity.class, scanExpression);
        notifications = notifications.stream().collect(Collectors.toList());
        notifications.sort((o1, o2) -> Math.toIntExact(o2.getCreateDate() - o1.getCreateDate()));

        return notifications.stream().map(notificationMessageMapper::toDTO).collect(Collectors.toList());

    }

    @Override
    public void readNotification(String id, String userId) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
        NotificationMessageEntity notification = dynamoDBMapper.load(NotificationMessageEntity.class, id);
        if (!notification.getUserId().equals(userId)){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Notification.NOT_FOUND));
        }
        notification.setRead(true);
        dynamoDBMapper.save(notification);
    }

    @Override
    public void readAll(String userId) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);

        HashMap<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId", new AttributeValue().withS(userId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("userId = :userId")
                .withExpressionAttributeValues(eav);
        List<NotificationMessageEntity> notifications = dynamoDBMapper.scan(NotificationMessageEntity.class, scanExpression);
        for(NotificationMessageEntity noti : notifications){
            noti.setRead(true);
        }
        dynamoDBMapper.batchSave(notifications);
    }


}
