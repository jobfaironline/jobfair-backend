package org.capstone.job_fair.models.entities.dynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamoDBTable(tableName = "JobhubConnections")
public class JobhubConnectionsEntity {
    @DynamoDBHashKey(attributeName = "connectionId")
    private String connectionId;
    @DynamoDBAttribute(attributeName = "userId")
    private String userId;
}
