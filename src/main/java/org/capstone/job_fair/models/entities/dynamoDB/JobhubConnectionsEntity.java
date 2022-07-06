package org.capstone.job_fair.models.entities.dynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "JobHubConnections")
@DynamoDBTable(tableName = "JobHubConnections")
public class JobhubConnectionsEntity {
    @Id
    @DynamoDBHashKey(attributeName = "connectionId")
    private String connectionId;
    @DynamoDBAttribute(attributeName = "userId")
    @Column(name = "userId")
    private String userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        JobhubConnectionsEntity that = (JobhubConnectionsEntity) o;
        return connectionId != null && Objects.equals(connectionId, that.getConnectionId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
