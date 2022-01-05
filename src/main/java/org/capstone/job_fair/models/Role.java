package org.capstone.job_fair.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Role")
public class Role {
    @Id
    @Column(name = "id")
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    @NaturalId
    private RoleName name;
    @Column(name = "description")
    private String description;

    //Account
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Account> accounts = new HashSet<Account>();





}
