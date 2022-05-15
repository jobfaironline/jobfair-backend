package org.capstone.job_fair.models.entities.dynamoDB;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "WaitingRoomVisit")
@Data
public class WaitingRoomVisitEntity {
    @DynamoDBHashKey(attributeName = "channelId")
    private String channelId;
    @DynamoDBRangeKey(attributeName = "userId")
    private String userId;
    @DynamoDBAttribute(attributeName = "isAttendant")
    private boolean isAttendant;
}
