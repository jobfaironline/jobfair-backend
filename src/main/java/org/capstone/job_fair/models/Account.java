package org.capstone.job_fair.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "middlename")
    private String middleName;
    @Column(name = "title")
    private String title;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "dob")
    private Long dob;
    @Column(name = "status")
    private Integer status;
    @Column(name = "job_title")
    private String jobTitle;
    @Column(name = "current_job_level_id")
    private Integer currentJobLevelId;
    @Column(name = "year_of_exp")
    private Float yearOfExp;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @Column(name = "country_id")
    private Integer countryId;
    @Column(name = "residence_id")
    private Integer residenceId;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Role role;

    public String getFullname (){
        return firstName + middleName + lastName;
    }



}
