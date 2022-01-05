package com.example.job_fair.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Account")
public class Account {
    @Id
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phone;
    @Column(name = "gender")
    private String gender;
    @Column(name = "status")
    private int status;
    @Column(name = "dob")
    private float dob;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleID")
    @JsonBackReference
    private Role role;

    @Column(name = "roleID", insertable = false, updatable = false)
    private int roleID;



}
