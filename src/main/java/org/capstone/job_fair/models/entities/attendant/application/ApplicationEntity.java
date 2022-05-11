package org.capstone.job_fair.models.entities.attendant.application;

import lombok.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.BoothJobPositionEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.models.enums.TestStatus;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "application", schema = "dbo")
public class ApplicationEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "summary")
    private String summary;

    @Column(name = "create_date")
    private Long createDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booth_job_position_id")
    private BoothJobPositionEntity boothJobPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorizer_id")
    private AccountEntity authorizer;

    @Column(name = "evaluate_message")
    private String evaluateMessage;

    @Column(name = "evaluate_date")
    private Long evaluateDate;

    @Column(name="email")
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="year_of_exp")
    private Integer yearOfExp;

    @Column(name="job_title")
    private String jobTitle;

    @Column(name = "job_level")
    @Enumerated(EnumType.ORDINAL)
    private JobLevel jobLevel;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private List<ApplicationActivityEntity> activities;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private List<ApplicationCertificationEntity> certifications;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private List<ApplicationEducationEntity> educations;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private List<ApplicationReferenceEntity> references;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private List<ApplicationSkillEntity> skills;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private List<ApplicationWorkHistoryEntity> workHistories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

    @Column(name = "origin_cv_id")
    private String originCvId;


    @Column(name = "test_status")
    @Enumerated(EnumType.ORDINAL)
    private TestStatus testStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationEntity that = (ApplicationEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + "id = " + id + ", " + "summary = " + summary + ", " + "createDate = " + createDate + ", " + "status = " + status + ")";
    }
}