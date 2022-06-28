package org.capstone.job_fair.controllers.payload.requests.account.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveQuizRequest {
    @NotNull
    private HashMap<String, Boolean> answers;
}
