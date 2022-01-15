package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.models.enums.Application;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "application", schema = "dbo")
public class ApplicationEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "summary")
    private String summary;

    @Column(name = "create_date")
    private Long createDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Application status;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

    @ManyToOne()
    @JoinColumn(name = "job_position_id", nullable = false)
    private JobLevelEntity jobLevel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicationEntity)) return false;
        ApplicationEntity that = (ApplicationEntity) o;
        return getId().equals(that.getId()) && getSummary().equals(that.getSummary()) && getCreateDate().equals(that.getCreateDate()) && getAttendant().equals(that.getAttendant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSummary(), getCreateDate(), getAttendant());
    }
}
