package org.capstone.job_fair.models.dtos.attendant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private int id;
    private String name;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleDTO roleDTO = (RoleDTO) o;

        return id == roleDTO.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
