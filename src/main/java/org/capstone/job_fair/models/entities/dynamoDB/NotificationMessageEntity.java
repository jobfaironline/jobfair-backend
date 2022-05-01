package org.capstone.job_fair.models.entities.dynamoDB;


import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.enums.NotificationType;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "NotificationMessage")
@Data
public class NotificationMessageEntity {
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "notificationId")
    private String notificationId;
    @DynamoDBAttribute(attributeName = "notificationType")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private NotificationType notificationType;
    @DynamoDBAttribute(attributeName = "title")
    private String title;
    @DynamoDBAttribute(attributeName = "message")
    private String message;
    @DynamoDBAttribute(attributeName = "isRead")
    private boolean isRead;
    @DynamoDBRangeKey(attributeName = "createDate")
    private String createDate;
    @DynamoDBAttribute(attributeName = "userId")
    private String userId;

}
