package org.capstone.job_fair.models.entities.dynamoDB;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "WaitingRoomVisit")
@Getter
@Setter
@Entity
@Table(name = "WaitingRoomVisit")
public class WaitingRoomVisitEntity {
    @Id
    @DynamoDBHashKey(attributeName = "channelId")
    private String channelId;
    @Column(name = "userId")
    @DynamoDBRangeKey(attributeName = "userId")
    private String userId;
    @Column(name = "isAttendant")
    @DynamoDBAttribute(attributeName = "isAttendant")
    private boolean isAttendant;
}
