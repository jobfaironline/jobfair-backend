package org.capstone.job_fair.services.impl.notification;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.capstone.job_fair.models.dtos.notification.NotificationDTO;
import org.capstone.job_fair.models.entities.notification.NotificationEntity;
import org.capstone.job_fair.services.mappers.notification.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@Service
public class NotificationServiceImpl {

    @Autowired
    private AmazonDynamoDB dynamoDBClient;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private AmazonSQS amazonSQS;

    public void createNotification(String receiverId, String message){

    }

    public void createNotification(List<String> receiverIds, String message){

    }

    public void createNotification(List<NotificationDTO> notificationDTOList) {
        List<NotificationEntity> notificationEntityList = (List<NotificationEntity>) notificationDTOList.stream().map(noti -> notificationMapper.toEntity(noti));
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
        mapper.batchSave(notificationEntityList);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl("https://sqs.ap-southeast-1.amazonaws.com/845953824246/jobhub-messageQueue")
                .withMessageBody("hello world");
        SendMessageBatchRequest sendMessageBatchRequest = new SendMessageBatchRequest()
                .withQueueUrl("https://sqs.ap-southeast-1.amazonaws.com/845953824246/jobhub-messageQueue").
        amazonSQS.sendMessage(send_msg_request);

    }


}
