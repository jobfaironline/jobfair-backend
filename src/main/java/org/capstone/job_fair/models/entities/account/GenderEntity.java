package org.capstone.job_fair.models.entities.account;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gender", schema = "dbo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GenderEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;

}
