package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.misc.CountryEntity;
import org.capstone.job_fair.models.entities.attendant.profile.CertificationEntity;
import org.capstone.job_fair.models.enums.JobLevel;
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
@Table(name = "cv", schema = "dbo")
public class CvEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "phone")
    private String phone;
    @Basic
    @Column(name = "year_of_exp")
    private Integer yearOfExp;
    @Basic
    @Column(name = "job_level")
    @Enumerated(EnumType.ORDINAL)
    private JobLevel jobLevel;
    @Basic
    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "cv_id", referencedColumnName = "id")
    private List<CvSkillEntity> skills;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "cv_id", referencedColumnName = "id")
    private List<CvWorkHistoryEntity> workHistories;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "cv_id", referencedColumnName = "id")
    private List<CvEducationEntity> educations;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "cv_id", referencedColumnName = "id")
    private List<CvCertificationEntity> certifications;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "cv_id", referencedColumnName = "id")
    private List<CvReferenceEntity> references;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "cv_id", referencedColumnName = "id")
    private List<CvActivityEntity> activities;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "country_id")
    private String countryId;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CertificationEntity that = (CertificationEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
