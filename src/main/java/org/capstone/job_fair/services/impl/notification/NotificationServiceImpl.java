package org.capstone.job_fair.services.impl.notification;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.entities.dynamoDB.NotificationMessageEntity;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.mappers.dynamoDB.NotificationMessageMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private AmazonDynamoDB dynamoDBClient;

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private NotificationMessageMapper notificationMessageMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DynamoDBMapperConfig dynamoDBMapperConfig;

    @Value("${aws.sqs.url}")
    private String queueURL;

    //SQS has SendMessageResult, but I have no idea what to do with this shit
    //So I just simply return true if it works
    private Boolean broadcastMessage(SendMessageRequest sendMessageRequest){
        try {
            amazonSQS.sendMessage(sendMessageRequest);
            return true;
        } catch (SdkClientException ex){
            log.error(NotificationServiceImpl.class.getSimpleName() + ": " + ex.getMessage());
            return false;
        }
    }

    @Override
    public void createNotification(NotificationMessageDTO message, Role role) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);

        List<String> accountIdList = accountRepository.findAccountByRole(role.ordinal());

        message.setNotificationId(UUID.randomUUID().toString());
        message.setRead(false);
        Long now = new Date().getTime();
        message.setCreateDate(now);

        List<NotificationMessageEntity> notificationMessageEntityList = accountIdList.stream().map(s -> {
            NotificationMessageEntity entity = notificationMessageMapper.toEntity(message);
            entity.setId(UUID.randomUUID().toString());
            entity.setUserId(s);
            return entity;
        }).collect(Collectors.toList());
        dynamoDBMapper.batchSave(notificationMessageEntityList);

        SendMessageRequest sendMessageRequest = new SendMessageRequest().withMessageBody(message.getNotificationId()).withQueueUrl(queueURL);
        this.broadcastMessage(sendMessageRequest);
    }

    @Override
    public void createNotification(NotificationMessageDTO message, String receiverId) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);

        message.setNotificationId(UUID.randomUUID().toString());
        message.setRead(false);
        Long now = new Date().getTime();
        message.setCreateDate(now);

        NotificationMessageEntity entity = notificationMessageMapper.toEntity(message);
        entity.setId(UUID.randomUUID().toString());
        entity.setUserId(receiverId);

        dynamoDBMapper.save(entity);

        SendMessageRequest sendMessageRequest = new SendMessageRequest().withMessageBody(message.getNotificationId()).withQueueUrl(queueURL);
        this.broadcastMessage(sendMessageRequest);
    }

    @Override
    public void createNotification(NotificationMessageDTO message, List<String> receiverIdList) {

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);

        message.setNotificationId(UUID.randomUUID().toString());
        message.setRead(false);
        Long now = new Date().getTime();
        message.setCreateDate(now);

        List<NotificationMessageEntity> notificationMessageEntityList = receiverIdList.stream().map(s -> {
            NotificationMessageEntity entity = notificationMessageMapper.toEntity(message);
            entity.setId(UUID.randomUUID().toString());
            entity.setUserId(s);
            return entity;
        }).collect(Collectors.toList());
        dynamoDBMapper.batchSave(notificationMessageEntityList);

        SendMessageRequest sendMessageRequest = new SendMessageRequest().withMessageBody(message.getNotificationId()).withQueueUrl(queueURL);
        this.broadcastMessage(sendMessageRequest);
    }

    @Override
    public List<NotificationMessageDTO> getNotificationByAccountId(String id) {
        try {
            DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);

            HashMap<String, AttributeValue> eav = new HashMap<>();
            eav.put(":userId", new AttributeValue().withS(id));
            eav.put(":notificationType", new AttributeValue().withS(NotificationType.NOTI.name()));

            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                    .withFilterExpression("userId = :userId AND notificationType = :notificationType")
                    .withExpressionAttributeValues(eav);


            List<NotificationMessageEntity> notifications = dynamoDBMapper.scan(NotificationMessageEntity.class, scanExpression);
            notifications = notifications.stream().collect(Collectors.toList());
            notifications.sort((o1, o2) -> Math.toIntExact(o2.getCreateDate().compareTo(o1.getCreateDate())));

            return notifications.stream().map(notificationMessageMapper::toDTO).collect(Collectors.toList());
        } catch (SdkClientException ex){
            log.error(NotificationServiceImpl.class.getSimpleName() + ": " + ex.getMessage());
            return Collections.EMPTY_LIST;
        }


    }

    @Override
    public void readNotification(String id, String userId) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);
        NotificationMessageEntity notification = dynamoDBMapper.load(NotificationMessageEntity.class, id);
        if (!notification.getUserId().equals(userId)){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Notification.NOT_FOUND));
        }
        notification.setRead(true);
        dynamoDBMapper.save(notification);
    }

    @Override
    public void readAll(String userId) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);

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
