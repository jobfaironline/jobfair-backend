package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.*;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "reference", schema = "dbo")
public class ReferenceEntity {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "full_name", length = 100)
    private String fullName;
    @Column(name = "position", length = 100)
    private String position;
    @Column(name = "company", length = 1000)
    private String company;
    @Column(name = "email", length = 322)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private AttendantEntity attendant;

}
