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
@DynamoDBTable(tableName = "JobFairVisit")
@Data
public class JobFairVisitEntity {
    @DynamoDBRangeKey(attributeName = "userId")
    private String userId;
    @DynamoDBHashKey(attributeName = "jobFairId")
    private String jobFairId;
    @DynamoDBAttribute(attributeName = "jobFairBoothId")
    private String jobFairBoothId;
}
