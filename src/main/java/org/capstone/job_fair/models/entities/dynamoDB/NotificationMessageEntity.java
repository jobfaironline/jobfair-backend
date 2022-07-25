package org.capstone.job_fair.models.entities.dynamoDB;


import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.enums.NotificationType;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "NotificationMessage")
@Getter
@Setter
@Entity
@Table(name = "NotificationMessage")
public class NotificationMessageEntity {
    @Id
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @Column(name = "notificationId")
    @DynamoDBAttribute(attributeName = "notificationId")
    private String notificationId;
    @Column(name = "notificationType")
    @DynamoDBAttribute(attributeName = "notificationType")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private NotificationType notificationType;
    @Column(name = "title")
    @DynamoDBAttribute(attributeName = "title")
    private String title;
    @Column(name = "message")
    @DynamoDBAttribute(attributeName = "message")
    private String message;
    @Column(name = "isRead")
    @DynamoDBAttribute(attributeName = "isRead")
    private boolean isRead;
    @Column(name = "createDate")
    @DynamoDBRangeKey(attributeName = "createDate")
    private Long createDate;
    @Column(name = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    private String userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        NotificationMessageEntity that = (NotificationMessageEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
