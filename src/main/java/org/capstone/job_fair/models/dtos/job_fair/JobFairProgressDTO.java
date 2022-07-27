package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JobFairProgressDTO {
    private String name = "";
    private double overallProgress = 0.0;
    private List<Booth> booths = new ArrayList<>();

    @Data
    public static class Booth {
        private String id = "";
        private String name = "";
        private double progress = 0;
        private Supervisor supervisor = new Supervisor();
        private Decorator decorator = new Decorator();

    }

    @Data
    public static class Supervisor {
        private String name = "";
        private boolean assignTask = false;
        private boolean boothProfile = false;
    }

    @Data
    public static class Decorator {
        private String name = "";
        private boolean decorate = false;
    }
}
