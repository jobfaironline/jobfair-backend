package org.capstone.job_fair.controllers.job_fair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.responses.JobFairBoothResponse;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.BoothJobPositionEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.repositories.attendant.application.ApplicationRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.JobFairBoothRepository;
import org.capstone.job_fair.services.interfaces.job_fair.booth.AssignmentService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class StatisticsController {

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class JobFairStatisticsResponse {
        private JobFair jobFair = new JobFair();
        private GeneralStatistics generalStatistics = new JobFairStatisticsResponse.GeneralStatistics();
        private CvStatistics cvStatistics = new JobFairStatisticsResponse.CvStatistics();
        private List<JobPosition> jobPositions = new ArrayList<>();
        private List<Booth> booths = new ArrayList<>();


        @Data
        public static class JobFair {
            private String id;
            private String name;
        }

        @Data
        public static class GeneralStatistics {
            private int boothNum = 0;
            private int participationNum = 0;
            private int jobPositionNum = 0;
            private int employeeNum = 0;
        }

        @Data
        public static class CvStatistics {
            private long pendingNum = 0;
            private long approvedNum = 0;
            private long rejectNum = 0;
            private double matchingAverage = 0.0;
            private double[] matchingRange = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }

        @Data
        public static class JobPosition {
            private String id;
            private String name;
            private int goal = 0;
            private int current = 0;
            private double matchingPointAverage = 0;
        }

        @Data
        public static class Booth {
            private String id;
            private String name;
            private int visitNum = 0;
            private int cvNum = 0;
            private double matchingPointAverage = 0.0;
        }

    }

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class BoothStatisticsResponse {
        private Booth booth = new Booth();
        private CvStatistics cvStatistics = new CvStatistics();
        private List<JobPosition> jobPositions = new ArrayList<>();

        @Data
        public static class Booth {
            private String id;
            private String name;
        }

        @Data
        public static class CvStatistics {
            private long pendingNum = 0;
            private long approvedNum = 0;
            private long rejectNum = 0;
            private double matchingAverage = 0.0;
            private double[] matchingRange = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }

        @Data
        public static class JobPosition {
            private String id;
            private String name;
            private int goal = 0;
            private int current = 0;
            private double matchingPointAverage = 0;
        }



    }

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private JobFairBoothRepository jobFairBoothRepository;

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.JobFair.STATISTICS + "/{id}")
    public ResponseEntity<?> getJobFairStatistics(@PathVariable String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();


        Random random = new Random();
        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findById(id);
        if (!jobFairOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }

        JobFairEntity jobFairEntity = jobFairOpt.get();


        JobFairStatisticsResponse response = new JobFairStatisticsResponse();
        response.jobFair.name = jobFairEntity.getName();
        response.jobFair.id = jobFairEntity.getId();

        response.generalStatistics.boothNum = jobFairEntity.getJobFairBoothList().size();
        response.generalStatistics.jobPositionNum = jobFairEntity.getJobFairBoothList().stream()
                .map(booth -> booth.getBoothJobPositions().size()).mapToInt(Integer::intValue).sum();
        response.generalStatistics.employeeNum = assignmentService.getCountAssignedEmployeeByJobFair(id);

        Page<ApplicationEntity> applicationPage = applicationRepository.findAllApplicationOfCompanyByJobFairIdAndStatusIn(companyId, id, Arrays.asList(ApplicationStatus.APPROVE, ApplicationStatus.PENDING, ApplicationStatus.PENDING), Pageable.unpaged());

        response.cvStatistics.pendingNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.PENDING).count();
        response.cvStatistics.approvedNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.APPROVE).count();
        response.cvStatistics.rejectNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.REJECT).count();
        response.cvStatistics.matchingAverage = applicationPage.stream().map(applicationEntity -> {
            if (applicationEntity.getMatchingPoint() == null) return 0.0;
            return applicationEntity.getMatchingPoint();
        }).mapToDouble(Double::doubleValue).average().orElse(0);
        applicationPage.forEach(application -> {
            double point = 0.0;
            if (application.getMatchingPoint() != null) point = application.getMatchingPoint();
            if (point >= 0.9)
                response.cvStatistics.matchingRange[9] += 1;
            if (0.8 <= point && point < 0.9 )
                response.cvStatistics.matchingRange[8] += 1;
            if (0.7 <= point && point < 0.8 )
                response.cvStatistics.matchingRange[7] += 1;
            if (0.6 <= point && point < 0.7 )
                response.cvStatistics.matchingRange[6] += 1;
            if (0.5 <= point && point < 0.6 )
                response.cvStatistics.matchingRange[5] += 1;
            if (0.4 <= point && point < 0.5 )
                response.cvStatistics.matchingRange[4] += 1;
            if (0.3 <= point && point < 0.4 )
                response.cvStatistics.matchingRange[3] += 1;
            if (0.2 <= point && point < 0.3 )
                response.cvStatistics.matchingRange[2] += 1;
            if (0.1 <= point && point < 0.2 )
                response.cvStatistics.matchingRange[1] += 1;
            if (0.0 <= point && point < 0.1 )
                response.cvStatistics.matchingRange[0] += 1;
        });

        Map<String, List<ApplicationEntity>> applicationByJobPosition = new HashedMap<>();
        applicationPage.forEach(applicationEntity -> {
            String jobPositionId = applicationEntity.getBoothJobPosition().getId();
            if (applicationByJobPosition.containsKey(jobPositionId)){
                applicationByJobPosition.get(jobPositionId).add(applicationEntity);
                return;
            }
            List<ApplicationEntity> newList = new ArrayList<>();
            newList.add(applicationEntity);
            applicationByJobPosition.put(jobPositionId, newList);
        });

        List<BoothJobPositionEntity> jobPositionEntities = jobFairEntity.getJobFairBoothList()
                .stream().flatMap(booth -> booth.getBoothJobPositions().stream()).collect(Collectors.toList());
        jobPositionEntities.forEach(jobPositionEntity -> {
            JobFairStatisticsResponse.JobPosition jobPosition = new JobFairStatisticsResponse.JobPosition();
            jobPosition.id = jobPositionEntity.getId();
            jobPosition.name = jobPositionEntity.getTitle();
            jobPosition.goal = jobPositionEntity.getNumOfPosition();
            jobPosition.current = applicationByJobPosition.get(jobPositionEntity.getId()) != null ? applicationByJobPosition.get(jobPositionEntity.getId()).size() : 0;
            if (applicationByJobPosition.get(jobPositionEntity.getId()) != null){
                jobPosition.matchingPointAverage = applicationByJobPosition.get(jobPositionEntity.getId()).stream().map(applicationEntity -> {
                    if (applicationEntity.getMatchingPoint() == null) return 0.0;
                    return applicationEntity.getMatchingPoint();
                }).mapToDouble(Double::doubleValue).average().orElse(0.0);
            }

            response.jobPositions.add(jobPosition);
        });

        Map<String, List<ApplicationEntity>> applicationByBooth = new HashedMap<>();
        applicationPage.forEach(applicationEntity -> {
            String boothId = applicationEntity.getBoothJobPosition().getJobFairBooth().getId();
            if (applicationByBooth.containsKey(boothId)){
                applicationByBooth.get(boothId).add(applicationEntity);
                return;
            }
            List<ApplicationEntity> newList = new ArrayList<>();
            newList.add(applicationEntity);
            applicationByBooth.put(boothId, newList);
        });
        List<JobFairBoothEntity> jobFairBoothEntities = jobFairEntity.getJobFairBoothList();
        jobFairBoothEntities.forEach(boothEntity -> {
            if (boothEntity.getName() == null) return;
            JobFairStatisticsResponse.Booth booth = new JobFairStatisticsResponse.Booth();
            booth.id = boothEntity.getId();
            booth.name = boothEntity.getName();
            booth.visitNum = random.ints(0, 10).findFirst().getAsInt();
            booth.cvNum = applicationByBooth.get(boothEntity.getId()) != null ? applicationByBooth.get(boothEntity.getId()).size() : 0;
            if (applicationByBooth.get(boothEntity.getId()) != null) {
                booth.matchingPointAverage = applicationByBooth.get(boothEntity.getId()).stream().map(applicationEntity -> {
                    if (applicationEntity.getMatchingPoint() == null) return 0.0;
                    return applicationEntity.getMatchingPoint();
                }).mapToDouble(Double::doubleValue).average().orElse(0.0);
            }
            response.booths.add(booth);
        });
        response.generalStatistics.participationNum = response.booths.stream().map(JobFairStatisticsResponse.Booth::getVisitNum).mapToInt(Integer::intValue).sum();
        return ResponseEntity.ok(response);


    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.JobFairBooth.STATISTICS + "/{id}")
    public ResponseEntity<?> getBoothStatistics(@PathVariable String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();


        Random random = new Random();
        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findById(id);
        if (!jobFairBoothOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }

        JobFairBoothEntity jobFairBoothEntity = jobFairBoothOpt.get();


        BoothStatisticsResponse response = new BoothStatisticsResponse();
        response.booth.name = jobFairBoothEntity.getName();
        response.booth.id = jobFairBoothEntity.getId();

        Page<ApplicationEntity> applicationPage = applicationRepository.findAllApplicationOfCompanyByJobFairBoothIdAndStatusIn(companyId, id, Arrays.asList(ApplicationStatus.APPROVE, ApplicationStatus.PENDING, ApplicationStatus.PENDING), Pageable.unpaged());

        response.cvStatistics.pendingNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.PENDING).count();
        response.cvStatistics.approvedNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.APPROVE).count();
        response.cvStatistics.rejectNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.REJECT).count();
        response.cvStatistics.matchingAverage = applicationPage.stream().map(applicationEntity -> {
            if (applicationEntity.getMatchingPoint() == null) return 0.0;
            return applicationEntity.getMatchingPoint();
        }).mapToDouble(Double::doubleValue).average().orElse(0);
        applicationPage.forEach(application -> {
            double point = 0.0;
            if (application.getMatchingPoint() != null) point = application.getMatchingPoint();
            if (point >= 0.9)
                response.cvStatistics.matchingRange[9] += 1;
            if (0.8 <= point && point < 0.9 )
                response.cvStatistics.matchingRange[8] += 1;
            if (0.7 <= point && point < 0.8 )
                response.cvStatistics.matchingRange[7] += 1;
            if (0.6 <= point && point < 0.7 )
                response.cvStatistics.matchingRange[6] += 1;
            if (0.5 <= point && point < 0.6 )
                response.cvStatistics.matchingRange[5] += 1;
            if (0.4 <= point && point < 0.5 )
                response.cvStatistics.matchingRange[4] += 1;
            if (0.3 <= point && point < 0.4 )
                response.cvStatistics.matchingRange[3] += 1;
            if (0.2 <= point && point < 0.3 )
                response.cvStatistics.matchingRange[2] += 1;
            if (0.1 <= point && point < 0.2 )
                response.cvStatistics.matchingRange[1] += 1;
            if (0.0 <= point && point < 0.1 )
                response.cvStatistics.matchingRange[0] += 1;
        });

        Map<String, List<ApplicationEntity>> applicationByJobPosition = new HashedMap<>();
        applicationPage.forEach(applicationEntity -> {
            String jobPositionId = applicationEntity.getBoothJobPosition().getId();
            if (applicationByJobPosition.containsKey(jobPositionId)){
                applicationByJobPosition.get(jobPositionId).add(applicationEntity);
                return;
            }
            List<ApplicationEntity> newList = new ArrayList<>();
            newList.add(applicationEntity);
            applicationByJobPosition.put(jobPositionId, newList);
        });

        List<BoothJobPositionEntity> jobPositionEntities = jobFairBoothEntity.getBoothJobPositions();
        jobPositionEntities.forEach(jobPositionEntity -> {
            BoothStatisticsResponse.JobPosition jobPosition = new BoothStatisticsResponse.JobPosition();
            jobPosition.id = jobPositionEntity.getId();
            jobPosition.name = jobPositionEntity.getTitle();
            jobPosition.goal = jobPositionEntity.getNumOfPosition();
            jobPosition.current = applicationByJobPosition.get(jobPositionEntity.getId()) != null ? applicationByJobPosition.get(jobPositionEntity.getId()).size() : 0;
            if (applicationByJobPosition.get(jobPositionEntity.getId()) != null){
                jobPosition.matchingPointAverage = applicationByJobPosition.get(jobPositionEntity.getId()).stream().map(applicationEntity -> {
                    if (applicationEntity.getMatchingPoint() == null) return 0.0;
                    return applicationEntity.getMatchingPoint();
                }).mapToDouble(Double::doubleValue).average().orElse(0.0);
            }

            response.jobPositions.add(jobPosition);
        });

        return ResponseEntity.ok(response);


    }
}
