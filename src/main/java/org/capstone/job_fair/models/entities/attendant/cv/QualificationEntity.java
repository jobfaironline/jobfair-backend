package org.capstone.job_fair.models.entities.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "qualification", schema = "dbo")
public class QualificationEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;

}
