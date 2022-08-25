package org.capstone.job_fair.services.impl.attendant.application;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.BoothJobPositionEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.capstone.job_fair.models.enums.TestStatus;
import org.capstone.job_fair.repositories.attendant.application.ApplicationRepository;
import org.capstone.job_fair.repositories.attendant.cv.CvRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.BoothJobPositionRepository;
import org.capstone.job_fair.services.interfaces.attendant.application.ApplicationService;
import org.capstone.job_fair.services.interfaces.job_fair.InterviewService;
import org.capstone.job_fair.services.interfaces.util.XSLSFileService;
import org.capstone.job_fair.services.mappers.attendant.application.*;
import org.capstone.job_fair.utils.MessageUtil;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private BoothJobPositionRepository regisJobPosRepository;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private ApplicationActivityMapper applicationActivityMapper;

    @Autowired
    private ApplicationCertificationMapper applicationCertificationMapper;

    @Autowired
    private ApplicationEducationMapper applicationEducationMapper;

    @Autowired
    private ApplicationReferenceMapper applicationReferenceMapper;

    @Autowired
    private ApplicationSkillMapper applicationSkillMapper;

    @Autowired
    private ApplicationWorkHistoryMapper applicationWorkHistoryMapper;

    @Autowired
    private Clock clock;

    @Autowired
    @Qualifier("LocalInterviewService")
    private InterviewService interviewService;

    @Value("${front-end.endpoint}")
    private String frontEndEndpoint;

    @Autowired
    private XSLSFileService xslsFileService;


    private TestStatus getTestStatus(String boothJobPositionId) {
        Optional<BoothJobPositionEntity> boothJobPositionEntityOptional = regisJobPosRepository.findById(boothJobPositionId);
        if (!boothJobPositionEntityOptional.isPresent()) {
            return null;
        }
        if (boothJobPositionEntityOptional.get().getIsHaveTest()) return TestStatus.NOT_TAKEN;
        return TestStatus.NO_TEST;
    }

    private void validatePaging(int pageSize, int offset) {
        if (offset < DataConstraint.Paging.OFFSET_MIN || pageSize < DataConstraint.Paging.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_PAGE_NUMBER));
    }


    private String applicationForCompanySortByMapper(String sortBy) {
        switch (sortBy) {
            case "appliedDate":
                return "createDate";
            case "jobPositionTitle":
                return "registrationJobPosition.title";
            case "jobFairName":
                return "registrationJobPosition.companyRegistration.jobFairEntity.name";
            case "evaluateDate":
                return "evaluateDate";
            default:
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.UNSUPPORTED_SORT_VALUE_FOR_APPLICATION_FOR_COMPANY_ERROR));
        }
    }


    @Override
    @Transactional
    public ApplicationDTO createNewApplication(ApplicationDTO dto) {
        //if attendantId not exist, throw error
        Optional<CvEntity> cvOptional = cvRepository.findByIdAndAttendantAccountId(dto.getOriginCvId(), dto.getAttendant().getAccount().getId());
        if (!cvOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.CV_NOT_FOUND));
        }
        CvEntity cvEntity = cvOptional.get();
        //if registration job position id not exist, throw  error
        TestStatus testStatus = getTestStatus(dto.getBoothJobPositionDTO().getId());
        if (testStatus == null) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.NOT_FOUND_REGISTRATION_JOB_POSITION));
        }

        Optional<BoothJobPositionEntity> jobPositionOpt = regisJobPosRepository.findById(dto.getBoothJobPositionDTO().getId());
        if (!jobPositionOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.NOT_FOUND_REGISTRATION_JOB_POSITION));
        }

        dto.setTestStatus(testStatus);
        ApplicationEntity entity = applicationMapper.toEntity(dto);
        entity.setEmail(cvEntity.getEmail());
        entity.setPhone(cvEntity.getPhone());
        entity.setYearOfExp(cvEntity.getYearOfExp());
        entity.setJobTitle(cvEntity.getJobTitle());
        entity.setJobLevel(cvEntity.getJobLevel());
        if (cvEntity.getActivities() != null) {
            entity.setActivities(cvEntity.getActivities().stream().map(applicationActivityMapper::toEntity).collect(Collectors.toList()));
        }
        if (cvEntity.getCertifications() != null) {
            entity.setCertifications(cvEntity.getCertifications().stream().map(applicationCertificationMapper::toEntity).collect(Collectors.toList()));
        }
        if (cvEntity.getEducations() != null) {
            entity.setEducations(cvEntity.getEducations().stream().map(applicationEducationMapper::toEntity).collect(Collectors.toList()));
        }
        if (cvEntity.getReferences() != null) {
            entity.setReferences(cvEntity.getReferences().stream().map(applicationReferenceMapper::toEntity).collect(Collectors.toList()));
        }
        if (cvEntity.getSkills() != null) {
            entity.setSkills(cvEntity.getSkills().stream().map(applicationSkillMapper::toEntity).collect(Collectors.toList()));
        }
        if (cvEntity.getWorkHistories() != null) {
            entity.setWorkHistories(cvEntity.getWorkHistories().stream().map(applicationWorkHistoryMapper::toEntity).collect(Collectors.toList()));
        }
        entity.setBoothJobPosition(jobPositionOpt.get());
        entity.setAboutMe(cvEntity.getAboutMe());
        entity.setCountryId(cvEntity.getCountryId());
        entity.setFullName(cvEntity.getFullName());
        entity.setProfileImageUrl(cvEntity.getProfileImageUrl());


        ApplicationEntity resultEntity = applicationRepository.save(entity);
        return applicationMapper.toDTO(resultEntity);
    }

    @Override
    public Page<ApplicationEntity> getAllApplicationsOfAttendantByCriteria(String userId, String jobFairName, String jobPositionName, List<ApplicationStatus> statusList, Long fromTime, Long toTime, int offset, int pageSize, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        if (statusList == null || statusList.isEmpty())
            statusList = Arrays.asList(ApplicationStatus.FINISHED, ApplicationStatus.DRAFT, ApplicationStatus.DELETED, ApplicationStatus.PENDING, ApplicationStatus.CANCEL, ApplicationStatus.APPROVE, ApplicationStatus.REJECT);
        if (fromTime > toTime)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INALID_TIME));
        return applicationRepository.findAllApplicationOfAttendantByCriteria(userId, fromTime, toTime, statusList, jobPositionName, jobFairName, PageRequest.of(offset, pageSize).withSort(direction, sortBy));

    }

    @Override
    @Transactional
    public ApplicationDTO submitApplication(String applicationId, String userId) {
        Optional<ApplicationEntity> entityOptional = applicationRepository.findById(applicationId);
        if (!entityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity entity = entityOptional.get();
        if (!entity.getAttendant().getAccount().getId().equals(userId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Attendant.ATTENDANT_MISMATCH));
        }

        List<ApplicationEntity> result = applicationRepository.findByOriginCvIdAndBoothJobPositionIdAndStatusIn(
                entity.getOriginCvId(),
                entity.getBoothJobPosition().getId(),
                Arrays.asList(ApplicationStatus.APPROVE, ApplicationStatus.PENDING, ApplicationStatus.REJECT));

        if (!result.isEmpty()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.ALREADY_APPLY_CV));
        }

        entity.setStatus(ApplicationStatus.PENDING);
        entity = applicationRepository.save(entity);
        return applicationMapper.toDTO(entity);
    }

    @Override
    public Optional<ApplicationDTO> getApplicationByIdForAttendant(String applicationId, String userId) {
        return applicationRepository.findByIdAndAttendantAccountId(applicationId, userId).map(applicationMapper::toDTO);
    }

    @Override
    public Optional<ApplicationDTO> getApplicationByIdForCompanyEmployee(String applicationId, String userId) {
        //TODO: check for company employee valid time
        return applicationRepository.findById(applicationId).map(applicationMapper::toDTO);
    }

    @Override
    public String exportApplicationByJobFair(String companyId, String jobFairId) {
        List<ApplicationEntity> applicationEntities = applicationRepository.findByBoothJobPositionJobFairBoothJobFairIdAndBoothJobPositionJobFairBoothJobFairCompanyId(jobFairId, companyId);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Applications");

        XSSFRow row;
        CreationHelper createHelper = workbook.getCreationHelper();

        XSSFCellStyle hlinkstyle = workbook.createCellStyle();
        XSSFFont hlinkfont = workbook.createFont();
        hlinkfont.setUnderline(XSSFFont.U_SINGLE);
        hlinkfont.setColor(IndexedColors.BLUE.index);
        hlinkstyle.setFont(hlinkfont);

        XSSFCellStyle headerStyle = workbook.createCellStyle();
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight(12);
        headerStyle.setFont(headerFont);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);

        // This data needs to be written (Object[])
        Map<String, Object[]> applicationData
                = new TreeMap<String, Object[]>();

        applicationData.put(
                "0",
                new Object[]{"No", "Candidate's name   ", "Job position   ", "Matching point   ", "Status   ", "Interview status    ", "Is qualified  ", "URL"});


        for (int i = 0; i < applicationEntities.size(); i++) {
            ApplicationEntity application = applicationEntities.get(i);
            String url = frontEndEndpoint + "/company/resume-detail/" + application.getId();
            applicationData.put(Integer.toString(i + 1), new Object[]{
                    Integer.toString(i + 1),
                    application.getFullName(),
                    application.getBoothJobPosition().getTitle(),
                    application.getMatchingPoint() != null ? String.format("%.2f", application.getMatchingPoint() * 100) : "0",
                    application.getStatus().toString(),
                    application.getInterviewStatus() != null ? application.getInterviewStatus().toString() : "",
                    application.getIsQualified() != null ? application.getIsQualified().toString(): "",
                    url});
        }

        Set<String> keyid = applicationData.keySet();


        int rowid = 0;
        for (String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = applicationData.get(key);
            int cellid = 0;

            for (int i=0; i < objectArr.length; i++){

                Object obj = objectArr[i];
                Cell cell = row.createCell(cellid++);
                if (i == 0 && !key.equals("0")){
                    cell.setCellValue(rowid);
                } else {
                    cell.setCellValue((String) obj);
                }
                if (i == 7 && !key.equals("0")){
                    XSSFHyperlink link = (XSSFHyperlink)createHelper.createHyperlink(HyperlinkType.URL);
                    link.setAddress((String) obj);
                    cell.setHyperlink((XSSFHyperlink) link);
                    cell.setCellStyle(hlinkstyle);
                }
                if (key.equals("0")){
                    cell.setCellStyle(headerStyle);
                }
            }

        }

        for (int i = 0; i < spreadsheet.getRow(0).getPhysicalNumberOfCells(); i++) {
            spreadsheet.autoSizeColumn(i);
        }
        spreadsheet.setAutoFilter(new CellRangeAddress(0, 0, 1, 6));
        spreadsheet.createFreezePane(0, 1);
        String url = xslsFileService.uploadXSLFile(workbook, AWSConstant.APPLICATION_FOLDER);

        return url;

    }

    @Override
    public Page<ApplicationEntity> getApplicationOfCompanyByJobPositionIdAndStatus(String companyId, String jobPositionId, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = applicationForCompanySortByMapper(sortBy);
        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(ApplicationStatus.FINISHED, ApplicationStatus.PENDING, ApplicationStatus.CANCEL, ApplicationStatus.APPROVE, ApplicationStatus.REJECT);
        }
        Page<ApplicationEntity> applicationEntityPage = applicationRepository.findAllApplicationOfCompanyByJobPositionIdAndStatusIn(companyId, jobPositionId, statusList, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        return applicationEntityPage;
    }

    @Override
    public Page<ApplicationEntity> getApplicationOfCompanyByJobFairIdAndStatus(String companyId, String jobFairId, List<ApplicationStatus> statusList, String attendantName, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = applicationForCompanySortByMapper(sortBy);
        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(ApplicationStatus.FINISHED, ApplicationStatus.PENDING, ApplicationStatus.CANCEL, ApplicationStatus.APPROVE, ApplicationStatus.REJECT);
        }

        Page<ApplicationEntity> applicationEntityPage = applicationRepository.findAllApplicationOfCompanyByJobFairIdAndStatusIn(companyId, jobFairId, statusList, attendantName, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        return applicationEntityPage;
    }

    @Override
    public Page<ApplicationEntity> getApplicationOfCompanyByJobFairNameAndJobPositionNameAndAttendantNameAndStatus(String companyId, String jobFairName, String jobPositionName, String attendantName, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = applicationForCompanySortByMapper(sortBy);

        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(ApplicationStatus.FINISHED, ApplicationStatus.PENDING, ApplicationStatus.CANCEL, ApplicationStatus.APPROVE, ApplicationStatus.REJECT);
        }

        Page<ApplicationEntity> applicationEntityPage = applicationRepository.findAllApplicationOfCompanyByJobPositionTitleLikeAndJobFairNameLikeAndAttendantNameLikeStatusIn(companyId, jobPositionName, jobFairName, attendantName, statusList, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        return applicationEntityPage;
    }

    @Override
    public Optional<ApplicationEntity> getApplicationWithGeneralDataByIdOfCompany(String companyId, String applicationId) {
        Optional<ApplicationEntity> applicationEntityOptional = applicationRepository.findByIdAndCompanyId(applicationId, companyId);
        if (!applicationEntityOptional.isPresent()) return Optional.empty();
        return applicationEntityOptional;
    }

    @Override
    @Transactional
    public ApplicationDTO evaluateApplication(ApplicationDTO dto, String userId) {
        Optional<ApplicationEntity> applicationEntityOptional = applicationRepository.findById(dto.getId());
        //check application existed
        if (!applicationEntityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        ApplicationEntity applicationEntity = applicationEntityOptional.get();

        //check if this user is an interviewer assigned to the same booth
        boolean isValidUser = applicationEntity
                .getBoothJobPosition().getJobFairBooth().getAssignments()
                .stream()
                .anyMatch(assignmentEntity -> assignmentEntity.getCompanyEmployee().getAccountId().equals(userId) && assignmentEntity.getType() == AssignmentType.INTERVIEWER);
        if (!isValidUser) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.MISS_MATCH_INTERVIEWER));
        }
        if (!applicationEntity.getStatus().equals(ApplicationStatus.PENDING))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_EVALUATE_STATUS));
        if (!dto.getStatus().equals(ApplicationStatus.APPROVE) && !dto.getStatus().equals(ApplicationStatus.REJECT))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_EVALUATE_STATUS));
        if (dto.getStatus().equals(ApplicationStatus.REJECT) && (dto.getEvaluateMessage() == null || dto.getEvaluateMessage().isEmpty()))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.EVALUATE_MESSAGE_IS_EMPTY));

        CompanyEmployeeDTO companyEmployeeDTO = new CompanyEmployeeDTO();
        companyEmployeeDTO.setAccountId(userId);
        dto.setInterviewer(companyEmployeeDTO);
        dto.setEvaluateDate(clock.millis());


        applicationMapper.updateFromDTO(applicationEntity, dto);
        applicationEntity = applicationRepository.save(applicationEntity);
        if (applicationEntity.getStatus() == ApplicationStatus.APPROVE) {
            interviewService.scheduleInterview(applicationEntity.getId(), userId);
        }

        return applicationMapper.toDTO(applicationEntity);
    }
}
