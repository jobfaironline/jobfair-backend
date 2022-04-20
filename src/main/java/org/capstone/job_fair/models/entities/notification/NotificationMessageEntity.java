package org.capstone.job_fair.models.entities.notification;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "NotificationMessage")
@Setter
public class NotificationMessageEntity {
    private String id;
    private String notificationId;
    private String notificationType;
    private String title;
    private String message;
    private boolean isRead;
    private long createDate;

    private String userId;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    @DynamoDBAttribute(attributeName = "notificationId")
    public String getNotificationId() {
        return notificationId;
    }

    @DynamoDBAttribute(attributeName = "notificationType")
    public String getNotificationType() {
        return notificationType;
    }

    @DynamoDBAttribute(attributeName = "title")
    public String getTitle() {
        return title;
    }

    @DynamoDBAttribute(attributeName = "message")
    public String getMessage() {
        return message;
    }

    @DynamoDBAttribute(attributeName = "isRead")
    public boolean isRead() {
        return isRead;
    }

    @DynamoDBAttribute(attributeName = "createDate")
    public long getCreateDate() {
        return createDate;
    }

    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return userId;
    }
}
