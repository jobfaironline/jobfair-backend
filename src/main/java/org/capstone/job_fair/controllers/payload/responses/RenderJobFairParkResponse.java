package org.capstone.job_fair.controllers.payload.responses;

import lombok.*;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothLayoutVideoDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RenderJobFairParkResponse {
    private String jobFairLayoutUrl;
    private List<BoothData> booths = new ArrayList<>();

    public void addBoothDataInformation(BoothData boothData){
        this.booths.add(boothData);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class BoothData{

        private Position position = new Position(0, 0, 0);
        private String slotName;
        private String boothUrl;
        private String companyBoothId;
        private String boothName;
        private List<JobFairBoothLayoutVideoDTO> companyBoothLayoutVideos;

        public void setPosition(double x, double y, double z){
            this.position.setX(x);
            this.position.setY(y);
            this.position.setZ(z);
        }


        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        private static class Position{
            private double x;
            private double y;
            private double z;
        }
    }

}
