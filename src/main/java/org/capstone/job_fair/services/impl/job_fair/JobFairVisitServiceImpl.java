package org.capstone.job_fair.services.impl.job_fair;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.amazonaws.util.json.Jackson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.entities.dynamoDB.JobFairVisitEntity;
import org.capstone.job_fair.models.entities.dynamoDB.JobhubConnectionsEntity;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.JobFairBoothRepository;
import org.capstone.job_fair.services.impl.notification.NotificationServiceImpl;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairVisitService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JobFairVisitServiceImpl implements JobFairVisitService {

    @Autowired
    private AmazonDynamoDB dynamoDBClient;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JobFairBoothRepository jobFairBoothRepository;

    @Autowired
    private DynamoDBMapperConfig dynamoDBMapperConfig;

    private List<String> getConnectedUsers(){
        try {
            DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            List<JobhubConnectionsEntity> scanResult = dynamoDBMapper.scan(JobhubConnectionsEntity.class, scanExpression);
            List<String> userIds = scanResult.stream().map(JobhubConnectionsEntity::getUserId).collect(Collectors.toList());
            return userIds;
        } catch (SdkClientException ex){
            log.error(JobFairVisitServiceImpl.class.getSimpleName() + ": " + ex.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void sendJobFairCountToConnectedUser(String jobFairId){
        Map<String, Object> payload = new HashMap<>();
        payload.put("jobFairId", jobFairId);
        payload.put("count", getCurrentVisitOfJobFair(jobFairId));


        //get current connected user
        List<String> userIds = getConnectedUsers();

        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                .title("Visit job fair")
                .message(Jackson.getObjectMapper().writeValueAsString(payload))
                .notificationType(NotificationType.VISIT_JOB_FAIR).build();


        notificationService.createNotification(notificationMessage, userIds);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void sendBoothCountToConnectedUser(String jobFairId, String jobFairBoothId){
        Map<String, Object> payload = new HashMap<>();
        payload.put("jobFairId", jobFairId);
        payload.put("jobFairBoothId", jobFairBoothId);
        payload.put("count", getCurrentVisitOfJobFairBooth(jobFairBoothId));

        //get current connected user
        List<String> userIds = getConnectedUsers();

        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                .title("Visit job fair booth")
                .message(Jackson.getObjectMapper().writeValueAsString(payload))
                .notificationType(NotificationType.VISIT_JOB_FAIR_BOOTH).build();


        notificationService.createNotification(notificationMessage, userIds);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Override
    public void visitJobFair(String userId, String jobFairId) {
        //this is a shitty method this should be optimized
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);
        //create new visit on dynamo
        JobFairVisitEntity entity = new JobFairVisitEntity();
        entity.setUserId(userId);
        entity.setJobFairId(jobFairId);
        dynamoDBMapper.save(entity);
        //send data
        sendJobFairCountToConnectedUser(jobFairId);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Override
    public void leaveJobFair(String userId, String jobFairId) {
        //this is a shitty method this should be optimized
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);
        //get visitation
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":jobFairId",new AttributeValue().withS(jobFairId));
        eav.put(":userId", new AttributeValue().withS(userId));
        DynamoDBQueryExpression<JobFairVisitEntity> queryExpression = new DynamoDBQueryExpression<JobFairVisitEntity>()
                .withKeyConditionExpression("jobFairId = :jobFairId AND userId = :userId")
                .withExpressionAttributeValues(eav);

        List<JobFairVisitEntity> queryResult = dynamoDBMapper.query(JobFairVisitEntity.class,queryExpression);
        //delete visitation
        dynamoDBMapper.batchDelete(queryResult);
        //send data
        sendJobFairCountToConnectedUser(jobFairId);

    }

    @Override
    @SneakyThrows
    public void visitBooth(String userId, String jobFairBoothId) {
        Thread.sleep(AWSConstant.DYNAMO_DELAY_TIME);
        //this is a shitty method this should be optimized
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);

        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findById(jobFairBoothId);
        if (!jobFairBoothOpt.isPresent()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
        }
        JobFairBoothEntity jobFairBooth = jobFairBoothOpt.get();
        //create new visit on dynamo
        JobFairVisitEntity entity = new JobFairVisitEntity();
        entity.setUserId(userId);
        entity.setJobFairId(jobFairBooth.getJobFair().getId());
        entity.setJobFairBoothId(jobFairBoothId);
        dynamoDBMapper.save(entity);
        //send data
        sendBoothCountToConnectedUser(jobFairBooth.getJobFair().getId(), jobFairBoothId);
    }

    @Override
    public void leaveBooth(String userId, String jobFairBoothId) {
        //this is a shitty method this should be optimized
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);

        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findById(jobFairBoothId);
        if (!jobFairBoothOpt.isPresent()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
        }
        JobFairBoothEntity jobFairBooth = jobFairBoothOpt.get();

        //get visitation
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":jobFairId",new AttributeValue().withS(jobFairBooth.getJobFair().getId()));
        eav.put(":userId", new AttributeValue().withS(userId));
        eav.put(":jobFairBoothId", new AttributeValue().withS(jobFairBoothId));

        DynamoDBQueryExpression<JobFairVisitEntity> queryExpression = new DynamoDBQueryExpression<JobFairVisitEntity>()
                .withKeyConditionExpression("jobFairId = :jobFairId AND userId = :userId")
                .withFilterExpression("jobFairBoothId = :jobFairBoothId")
                .withExpressionAttributeValues(eav);

        List<JobFairVisitEntity> queryResult = dynamoDBMapper.query(JobFairVisitEntity.class,queryExpression);
        //delete visitation
        dynamoDBMapper.batchDelete(queryResult);
        //send data
        sendBoothCountToConnectedUser(jobFairBooth.getJobFair().getId(), jobFairBoothId);
    }

    @Override
    public int getCurrentVisitOfJobFair(String jobFairId) {
        try {
            DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);

            Map<String, AttributeValue> eav = new HashMap<>();
            eav.put(":jobFairId", new AttributeValue().withS(jobFairId));


            DynamoDBQueryExpression<JobFairVisitEntity> queryExpression = new DynamoDBQueryExpression<JobFairVisitEntity>()
                    .withKeyConditionExpression("jobFairId = :jobFairId")
                    .withExpressionAttributeValues(eav).withSelect(Select.COUNT);

            return dynamoDBMapper.count(JobFairVisitEntity.class, queryExpression);
        } catch (SdkClientException ex){
            log.error(JobFairVisitServiceImpl.class.getSimpleName() + ": " + ex.getMessage());
            return 0;
        }
    }

    @Override
    public int getCurrentVisitOfJobFairBooth(String jobFairBoothId) {
        try {
            Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findById(jobFairBoothId);
            if (!jobFairBoothOpt.isPresent()){
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
            }
            JobFairBoothEntity jobFairBooth = jobFairBoothOpt.get();

            DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);

            Map<String, AttributeValue> eav = new HashMap<>();
            eav.put(":jobFairId", new AttributeValue().withS(jobFairBooth.getJobFair().getId()));
            eav.put(":jobFairBoothId", new AttributeValue().withS(jobFairBoothId));


            DynamoDBQueryExpression<JobFairVisitEntity> queryExpression = new DynamoDBQueryExpression<JobFairVisitEntity>()
                    .withIndexName("jobFairBoothId-index")
                    .withKeyConditionExpression("jobFairId = :jobFairId AND jobFairBoothId = :jobFairBoothId ")
                    .withExpressionAttributeValues(eav).withSelect(Select.COUNT);


            return dynamoDBMapper.count(JobFairVisitEntity.class, queryExpression);
        } catch (SdkClientException ex){
            log.error(JobFairVisitServiceImpl.class.getSimpleName() + ": " + ex.getMessage());
            return 0;
        }
    }
}
