package org.capstone.job_fair.models.dtos.attendant;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDTO implements Serializable {
    private String id;
    private String name;
}
