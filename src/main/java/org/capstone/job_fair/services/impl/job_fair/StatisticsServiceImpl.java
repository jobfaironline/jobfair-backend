package org.capstone.job_fair.services.impl.job_fair;

import org.apache.commons.collections4.map.HashedMap;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.admin.AdminStatisticsDTO;
import org.capstone.job_fair.models.dtos.company.CompanyStatisticsDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairStatisticsDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothStatisticsDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.BoothJobPositionEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.models.statuses.InterviewStatus;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.attendant.application.ApplicationRepository;
import org.capstone.job_fair.repositories.company.CompanyRepository;
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

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;

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
        Page<ApplicationEntity> applicationPage = applicationRepository.findAllApplicationOfCompanyByJobFairIdAndStatusIn(companyId, jobFairId, Arrays.asList(ApplicationStatus.APPROVE, ApplicationStatus.PENDING, ApplicationStatus.REJECT), Pageable.unpaged());
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
            int approvedCVNum = applicationByJobPosition.get(jobPositionEntity.getId()) == null ? 0 : (int) applicationByJobPosition.get(jobPositionEntity.getId()).stream().filter(application -> {
                return application.getStatus() == ApplicationStatus.APPROVE;
            }).count();
            jobPosition.setCurrent(appliedCVNum);
            jobPosition.setApproveCV(approvedCVNum);
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
            int approvedCVNum = applicationByBooth.get(boothEntity.getId()) == null ? 0 : (int) applicationByBooth.get(boothEntity.getId()).stream().filter(application -> {
                return application.getStatus() == ApplicationStatus.APPROVE;
            }).count();
            booth.setVisitNum(visitNum);
            booth.setCvNum(appliedCvNum);
            booth.setApproveCV(approvedCVNum);
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
        Page<ApplicationEntity> applicationPage = applicationRepository.findAllApplicationOfCompanyByJobFairBoothIdAndStatusIn(companyId, boothId, Arrays.asList(ApplicationStatus.APPROVE, ApplicationStatus.PENDING, ApplicationStatus.REJECT), Pageable.unpaged());
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

    @Override
    public CompanyStatisticsDTO getCompanyStatistics(String companyId) {
        CompanyStatisticsDTO result = new CompanyStatisticsDTO();

        CompanyEntity companyEntity = companyRepository.getById(companyId);


        //get general statistics
        List<JobFairEntity> publishedJobFairs = jobFairRepository.findByCompanyIdAndStatus(companyId, JobFairPlanStatus.PUBLISH);
        List<JobFairEntity> draftJobFairs = jobFairRepository.findByCompanyIdAndStatus(companyId, JobFairPlanStatus.DRAFT);

        long now = new Date().getTime();
        List<JobFairEntity> inProgressJobFairs = publishedJobFairs.stream().filter(jobfair -> jobfair.getDecorateStartTime() < now && jobfair.getPublicEndTime() > now).collect(Collectors.toList());
        List<JobFairEntity> pastJobFairs = publishedJobFairs.stream().filter(jobFair -> jobFair.getPublicEndTime() <= now).collect(Collectors.toList());
        List<JobFairEntity> upComingJobFairs = publishedJobFairs.stream().filter(jobFair -> jobFair.getDecorateStartTime() >= now).collect(Collectors.toList());

        int createdJobFairNum = publishedJobFairs.size() + draftJobFairs.size();
        int publishedJobFairNum = publishedJobFairs.size();
        int boothTotalNum = publishedJobFairs.stream().map(jobFair -> jobFair.getJobFairBoothList().size()).mapToInt(Integer::intValue).sum();
        int inProgressJobFairNum = inProgressJobFairs.size();
        int pastJobFairNum = pastJobFairs.size();
        int upComingJobFairNum = upComingJobFairs.size();

        result.getGeneralStatistics().setCreatedJobFairNum(createdJobFairNum);
        result.getGeneralStatistics().setPublishedJobFairNum(publishedJobFairNum);
        result.getGeneralStatistics().setParticipantNum(0);
        result.getGeneralStatistics().setBoothTotalNums(boothTotalNum);
        result.getGeneralStatistics().setInProgressJobFairNum(inProgressJobFairNum);
        result.getGeneralStatistics().setPastJobFairNum(pastJobFairNum);
        result.getGeneralStatistics().setUpComingJobFairNum(upComingJobFairNum);
        result.getGeneralStatistics().setCompanyName(companyEntity.getName());


        //get CV statisics
        List<ApplicationEntity> applications = publishedJobFairs.stream().flatMap(jobFair -> {
            Page<ApplicationEntity> applicationPage = applicationRepository.findAllApplicationOfCompanyByJobFairIdAndStatusIn(companyId, jobFair.getId(), Arrays.asList(ApplicationStatus.APPROVE, ApplicationStatus.PENDING, ApplicationStatus.REJECT), Pageable.unpaged());
            return applicationPage.toList().stream();
        }).collect(Collectors.toList());

        List<ApplicationEntity> approvedApplications = applications.stream().filter(application -> application.getStatus() == ApplicationStatus.APPROVE).collect(Collectors.toList());
        List<ApplicationEntity> rejectedApplications = applications.stream().filter(application -> application.getStatus() == ApplicationStatus.REJECT).collect(Collectors.toList());
        List<ApplicationEntity> pendingApplications = applications.stream().filter(application -> application.getStatus() == ApplicationStatus.PENDING).collect(Collectors.toList());
        List<ApplicationEntity> interviewApplications = applications.stream().filter(application -> application.getInterviewStatus() != null).collect(Collectors.toList());
        List<ApplicationEntity> doneInterviewApplications = applications.stream().filter(application -> application.getInterviewStatus() == InterviewStatus.DONE).collect(Collectors.toList());

        int cvNum = approvedApplications.size() + rejectedApplications.size() + pendingApplications.size();
        int approvedCvNum = approvedApplications.size();
        int rejectedCvNum = rejectedApplications.size();
        int pendingCvNum = pendingApplications.size();
        int interviewNum = interviewApplications.size();
        int doneInterviewNum = doneInterviewApplications.size();

        result.getCvStatistics().setCvNum(cvNum);
        result.getCvStatistics().setApprovedCvNum(approvedCvNum);
        result.getCvStatistics().setRejectedCvNum(rejectedCvNum);
        result.getCvStatistics().setPendingCvNum(pendingCvNum);
        result.getCvStatistics().setInterviewNum(interviewNum);
        result.getCvStatistics().setDoneInterviewNum(doneInterviewNum);


        //get job statistics
        Map<String, List<ApplicationEntity>> applicationByJobPosition = new HashedMap<>();
        applications.forEach(applicationEntity -> {
            String jobPositionId = applicationEntity.getBoothJobPosition().getId();
            if (applicationByJobPosition.containsKey(jobPositionId)) {
                applicationByJobPosition.get(jobPositionId).add(applicationEntity);
                return;
            }
            List<ApplicationEntity> newList = new ArrayList<>();
            newList.add(applicationEntity);
            applicationByJobPosition.put(jobPositionId, newList);
        });
        List<BoothJobPositionEntity> jobPositionEntities = publishedJobFairs.stream().flatMap(jobFair -> {
            return jobFair.getJobFairBoothList().stream().flatMap(booth -> booth.getBoothJobPositions().stream());
        }).collect(Collectors.toList());
        jobPositionEntities.forEach(jobPositionEntity -> {
            CompanyStatisticsDTO.JobPosition jobPosition = new CompanyStatisticsDTO.JobPosition();
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
            result.getJobPositions().add(jobPosition);
        });

        //get job fair statistics
        publishedJobFairs.forEach(jobFair -> {
            int boothNum = jobFair.getJobFairBoothList().size();
            int participationNum = 0;
            int jobPositionNum = jobFair.getJobFairBoothList().stream().map(booth -> booth.getBoothJobPositions().size()).mapToInt(Integer::intValue).sum();
            int employeeNum = assignmentService.getCountAssignedEmployeeByJobFair(jobFair.getId());

            CompanyStatisticsDTO.JobFair jobFairStatistics = new CompanyStatisticsDTO.JobFair();
            jobFairStatistics.setId(jobFair.getId());
            jobFairStatistics.setName(jobFair.getName());
            jobFairStatistics.setBoothNum(boothNum);
            jobFairStatistics.setParticipationNum(participationNum);
            jobFairStatistics.setJobPositionNum(jobPositionNum);
            jobFairStatistics.setEmployeeNum(employeeNum);
            jobFairStatistics.setDecorateStartTime(jobFair.getDecorateStartTime());
            jobFairStatistics.setPublicEndTime(jobFair.getPublicEndTime());
            result.getJobFairs().add(jobFairStatistics);
        });

        return result;
    }

    @Override
    public AdminStatisticsDTO getAdminStatistics() {
        AdminStatisticsDTO result = new AdminStatisticsDTO();

        //account statistics
        long verifiedNum = accountRepository.countByStatus(AccountStatus.VERIFIED);
        long inactiveNum = accountRepository.countByStatus(AccountStatus.INACTIVE);
        long registeredNum = accountRepository.countByStatus(AccountStatus.REGISTERED);
        long suspendedNum = accountRepository.countByStatus(AccountStatus.SUSPENDED);
        long attendantNum = accountRepository.countByRoleId(Role.ATTENDANT.ordinal());
        long companyManagerNum = accountRepository.countByRoleId(Role.COMPANY_MANAGER.ordinal());
        long companyEmployeeNum = accountRepository.countByRoleId(Role.COMPANY_EMPLOYEE.ordinal());

        result.getAccountStatistics().setVerifiedNum(verifiedNum);
        result.getAccountStatistics().setInactiveNum(inactiveNum);
        result.getAccountStatistics().setRegisteredNum(registeredNum);
        result.getAccountStatistics().setSuspendedNum(suspendedNum);
        result.getAccountStatistics().setAttendantNum(attendantNum);
        result.getAccountStatistics().setCompanyManagerNum(companyManagerNum);
        result.getAccountStatistics().setCompanyEmployeeNum(companyEmployeeNum);

        //job fair statistics
        List<JobFairEntity> publishedJobFairs = jobFairRepository.findByStatus(JobFairPlanStatus.PUBLISH);
        long now = new Date().getTime();
        List<JobFairEntity> inProgressJobFairs = publishedJobFairs.stream().filter(jobFair -> jobFair.getDecorateStartTime() < now && jobFair.getPublicEndTime() > now).collect(Collectors.toList());
        List<JobFairEntity> pastJobFairs = publishedJobFairs.stream().filter(jobFair -> jobFair.getPublicEndTime() <= now).collect(Collectors.toList());
        List<JobFairEntity> comingSoonJobFairs = publishedJobFairs.stream().filter(jobFair -> jobFair.getDecorateStartTime() >= now).collect(Collectors.toList());

        long pastNum = pastJobFairs.size();
        long inProgressNum = inProgressJobFairs.size();
        long incomingNum = comingSoonJobFairs.size();

        result.getJobFairStatistics().setPastNum(pastNum);
        result.getJobFairStatistics().setIncomingNum(incomingNum);
        result.getJobFairStatistics().setInProgressNum(inProgressNum);

        //general statistics
        long companyNum = companyRepository.count();
        long visitorNum = 0;
        long userNum = verifiedNum + inactiveNum + registeredNum + suspendedNum;
        result.getGeneralStatistics().setCompanyNum(companyNum);
        result.getGeneralStatistics().setVisitorNum(visitorNum);
        result.getGeneralStatistics().setUserNum(userNum);

        return result;
    }
}
