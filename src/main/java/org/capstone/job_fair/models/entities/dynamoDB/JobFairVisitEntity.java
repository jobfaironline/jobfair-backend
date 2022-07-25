package org.capstone.job_fair.models.entities.dynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "JobFairVisit")
@Getter
@Setter
@Entity
@Table(name = "JobFairVisit")
public class JobFairVisitEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "userId")
    @DynamoDBRangeKey(attributeName = "userId")
    private String userId;
    @Column(name = "jobFairId")
    @DynamoDBHashKey(attributeName = "jobFairId")
    private String jobFairId;
    @Column(name = "jobFairBoothId")
    @DynamoDBAttribute(attributeName = "jobFairBoothId")
    private String jobFairBoothId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        JobFairVisitEntity that = (JobFairVisitEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
