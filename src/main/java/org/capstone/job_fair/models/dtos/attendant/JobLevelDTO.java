package org.capstone.job_fair.models.dtos.attendant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobLevelDTO implements Serializable {
    private String id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobLevelDTO that = (JobLevelDTO) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
