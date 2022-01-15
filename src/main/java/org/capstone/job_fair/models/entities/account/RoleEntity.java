package org.capstone.job_fair.models.entities.account;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role", schema = "dbo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RoleEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;

}
