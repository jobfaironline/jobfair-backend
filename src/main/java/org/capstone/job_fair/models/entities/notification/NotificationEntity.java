package org.capstone.job_fair.models.entities.notification;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "JobHubNoti")
@Setter
public class NotificationEntity {

    private String id;
    private String userId;
    private String title;
    private String message;
    private boolean isRead;
    private long createDate;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    @DynamoDBAttribute(attributeName = "createDate")
    public long getCreateDate() {
        return createDate;
    }

    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return userId;
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
}
