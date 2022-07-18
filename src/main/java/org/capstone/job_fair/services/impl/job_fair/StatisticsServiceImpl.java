package org.capstone.job_fair.services.impl.job_fair;

import org.apache.commons.collections4.map.HashedMap;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.JobFairStatisticsDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothStatisticsDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.BoothJobPositionEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.repositories.attendant.application.ApplicationRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.JobFairBoothRepository;
import org.capstone.job_fair.services.interfaces.job_fair.StatisticsService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.AssignmentService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private JobFairBoothRepository jobFairBoothRepository;

    @Override
    public JobFairStatisticsDTO getJobFairStatistics(String jobFairId, String companyId) {
        Random random = new Random();
        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }

        JobFairEntity jobFairEntity = jobFairOpt.get();


        JobFairStatisticsDTO statisticsDTO = new JobFairStatisticsDTO();
        //get general statistics
        statisticsDTO.getJobFair().setName(jobFairEntity.getName());
        statisticsDTO.getJobFair().setId(jobFairEntity.getId());

        long boothNum = jobFairEntity.getJobFairBoothList().stream().filter(booth -> {
            return booth.getName() != null;
        }).count();
        statisticsDTO.getGeneralStatistics().setBoothNum((int) boothNum);
        int jobPositionNum = jobFairEntity.getJobFairBoothList().stream()
                .map(booth -> booth.getBoothJobPositions().size()).mapToInt(Integer::intValue).sum();
        statisticsDTO.getGeneralStatistics().setJobPositionNum(jobPositionNum);
        int employeeNum = assignmentService.getCountAssignedEmployeeByJobFair(jobFairId);
        statisticsDTO.getGeneralStatistics().setEmployeeNum(employeeNum);

        //get CV general statistics
        Page<ApplicationEntity> applicationPage = applicationRepository.findAllApplicationOfCompanyByJobFairIdAndStatusIn(companyId, jobFairId, Arrays.asList(ApplicationStatus.APPROVE, ApplicationStatus.PENDING, ApplicationStatus.PENDING), Pageable.unpaged());
        long pendingCVNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.PENDING).count();
        long rejectCVNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.REJECT).count();
        long approveCVNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.APPROVE).count();
        double matchingPointAverage = applicationPage.stream().map(applicationEntity -> {
            if (applicationEntity.getMatchingPoint() == null) return 0.0;
            return applicationEntity.getMatchingPoint();
        }).mapToDouble(Double::doubleValue).average().orElse(0);
        statisticsDTO.getCvStatistics().setPendingNum(pendingCVNum);
        statisticsDTO.getCvStatistics().setRejectNum(rejectCVNum);
        statisticsDTO.getCvStatistics().setApprovedNum(approveCVNum);
        statisticsDTO.getCvStatistics().setMatchingAverage(matchingPointAverage);
        double[] matchingRange = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        applicationPage.forEach(application -> {
            double point = 0.0;
            if (application.getMatchingPoint() != null) point = application.getMatchingPoint();
            if (point >= 0.9)
                matchingRange[9] += 1;
            if (0.8 <= point && point < 0.9)
                matchingRange[8] += 1;
            if (0.7 <= point && point < 0.8)
                matchingRange[7] += 1;
            if (0.6 <= point && point < 0.7)
                matchingRange[6] += 1;
            if (0.5 <= point && point < 0.6)
                matchingRange[5] += 1;
            if (0.4 <= point && point < 0.5)
                matchingRange[4] += 1;
            if (0.3 <= point && point < 0.4)
                matchingRange[3] += 1;
            if (0.2 <= point && point < 0.3)
                matchingRange[2] += 1;
            if (0.1 <= point && point < 0.2)
                matchingRange[1] += 1;
            if (0.0 <= point && point < 0.1)
                matchingRange[0] += 1;
        });
        statisticsDTO.getCvStatistics().setMatchingRange(matchingRange);

        //get job position statistics
        Map<String, List<ApplicationEntity>> applicationByJobPosition = new HashedMap<>();
        applicationPage.forEach(applicationEntity -> {
            String jobPositionId = applicationEntity.getBoothJobPosition().getId();
            if (applicationByJobPosition.containsKey(jobPositionId)) {
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
            JobFairStatisticsDTO.JobPosition jobPosition = new JobFairStatisticsDTO.JobPosition();
            jobPosition.setId(jobPositionEntity.getId());
            jobPosition.setName(jobPositionEntity.getTitle());
            jobPosition.setGoal(jobPositionEntity.getNumOfPosition());
            int appliedCVNum = applicationByJobPosition.get(jobPositionEntity.getId()) != null ? applicationByJobPosition.get(jobPositionEntity.getId()).size() : 0;
            jobPosition.setCurrent(appliedCVNum);
            if (applicationByJobPosition.get(jobPositionEntity.getId()) != null) {
                double averageMatchingPoint = applicationByJobPosition.get(jobPositionEntity.getId()).stream().map(applicationEntity -> {
                    if (applicationEntity.getMatchingPoint() == null) return 0.0;
                    return applicationEntity.getMatchingPoint();
                }).mapToDouble(Double::doubleValue).average().orElse(0.0);
                jobPosition.setMatchingPointAverage(averageMatchingPoint);
            }

            statisticsDTO.getJobPositions().add(jobPosition);
        });

        //get booth statistics
        Map<String, List<ApplicationEntity>> applicationByBooth = new HashedMap<>();
        applicationPage.forEach(applicationEntity -> {
            String boothId = applicationEntity.getBoothJobPosition().getJobFairBooth().getId();
            if (applicationByBooth.containsKey(boothId)) {
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
            JobFairStatisticsDTO.Booth booth = new JobFairStatisticsDTO.Booth();
            booth.setId(boothEntity.getId());
            booth.setName(boothEntity.getName());
            int visitNum = random.ints(0, 10).findFirst().getAsInt();
            int appliedCvNum = applicationByBooth.get(boothEntity.getId()) != null ? applicationByBooth.get(boothEntity.getId()).size() : 0;
            booth.setVisitNum(visitNum);
            booth.setCvNum(appliedCvNum);
            if (applicationByBooth.get(boothEntity.getId()) != null) {
                double average = applicationByBooth.get(boothEntity.getId()).stream().map(applicationEntity -> {
                    if (applicationEntity.getMatchingPoint() == null) return 0.0;
                    return applicationEntity.getMatchingPoint();
                }).mapToDouble(Double::doubleValue).average().orElse(0.0);
                booth.setMatchingPointAverage(average);
            }

            statisticsDTO.getBooths().add(booth);
        });
        int sumVisitNum = statisticsDTO.getBooths().stream().map(JobFairStatisticsDTO.Booth::getVisitNum).mapToInt(Integer::intValue).sum();
        statisticsDTO.getGeneralStatistics().setParticipationNum(sumVisitNum);
        return statisticsDTO;
    }

    @Override
    public BoothStatisticsDTO getJobFairBoothStatistics(String boothId, String companyId) {
        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findById(boothId);
        if (!jobFairBoothOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }

        JobFairBoothEntity jobFairBoothEntity = jobFairBoothOpt.get();

        BoothStatisticsDTO statisticsDTO = new BoothStatisticsDTO();
        //get booth general information
        statisticsDTO.getBooth().setName(jobFairBoothEntity.getName());
        statisticsDTO.getBooth().setId(jobFairBoothEntity.getId());

        //get CV statistics
        Page<ApplicationEntity> applicationPage = applicationRepository.findAllApplicationOfCompanyByJobFairBoothIdAndStatusIn(companyId, boothId, Arrays.asList(ApplicationStatus.APPROVE, ApplicationStatus.PENDING, ApplicationStatus.PENDING), Pageable.unpaged());
        long pendingCVNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.PENDING).count();
        long rejectCVNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.REJECT).count();
        long approveCVNum = applicationPage.stream().filter(application -> application.getStatus() == ApplicationStatus.APPROVE).count();
        double matchingPointAverage = applicationPage.stream().map(applicationEntity -> {
            if (applicationEntity.getMatchingPoint() == null) return 0.0;
            return applicationEntity.getMatchingPoint();
        }).mapToDouble(Double::doubleValue).average().orElse(0);
        statisticsDTO.getCvStatistics().setPendingNum(pendingCVNum);
        statisticsDTO.getCvStatistics().setRejectNum(rejectCVNum);
        statisticsDTO.getCvStatistics().setApprovedNum(approveCVNum);
        statisticsDTO.getCvStatistics().setMatchingAverage(matchingPointAverage);

        //get job statistics
        Map<String, List<ApplicationEntity>> applicationByJobPosition = new HashedMap<>();
        applicationPage.forEach(applicationEntity -> {
            String jobPositionId = applicationEntity.getBoothJobPosition().getId();
            if (applicationByJobPosition.containsKey(jobPositionId)) {
                applicationByJobPosition.get(jobPositionId).add(applicationEntity);
                return;
            }
            List<ApplicationEntity> newList = new ArrayList<>();
            newList.add(applicationEntity);
            applicationByJobPosition.put(jobPositionId, newList);
        });

        List<BoothJobPositionEntity> jobPositionEntities = jobFairBoothEntity.getBoothJobPositions();
        jobPositionEntities.forEach(jobPositionEntity -> {
            BoothStatisticsDTO.JobPosition jobPosition = new BoothStatisticsDTO.JobPosition();
            jobPosition.setId(jobPositionEntity.getId());
            jobPosition.setName(jobPositionEntity.getTitle());
            jobPosition.setGoal(jobPositionEntity.getNumOfPosition());
            int appliedCVNum = applicationByJobPosition.get(jobPositionEntity.getId()) != null ? applicationByJobPosition.get(jobPositionEntity.getId()).size() : 0;
            jobPosition.setCurrent(appliedCVNum);
            if (applicationByJobPosition.get(jobPositionEntity.getId()) != null) {
                double averageMatchingPoint = applicationByJobPosition.get(jobPositionEntity.getId()).stream().map(applicationEntity -> {
                    if (applicationEntity.getMatchingPoint() == null) return 0.0;
                    return applicationEntity.getMatchingPoint();
                }).mapToDouble(Double::doubleValue).average().orElse(0.0);
                jobPosition.setMatchingPointAverage(averageMatchingPoint);
            }

            statisticsDTO.getJobPositions().add(jobPosition);
        });
        return statisticsDTO;
    }
}
