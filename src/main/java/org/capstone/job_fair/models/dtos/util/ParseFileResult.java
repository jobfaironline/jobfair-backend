package org.capstone.job_fair.models.dtos.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParseFileResult<T> {
    @JsonIgnore
    private List<T> result;
    private Map<Integer, String> errors;
    private String errorFileUrl;

    public boolean isHasError() {
        if (errors == null) return false;
        return !errors.isEmpty();
    }

    public void addToResult(T item) {
        if (result == null) {
            result = new ArrayList<>();
        }
        result.add(item);
    }

    public void addErrorMessage(int rowNum, String message) {
        if (errors == null) {
            errors = new HashMap<>();
        }
        String oldMessage = errors.get(rowNum);
        if (oldMessage == null) oldMessage = "";
        errors.put(rowNum, oldMessage + message);
    }
}
