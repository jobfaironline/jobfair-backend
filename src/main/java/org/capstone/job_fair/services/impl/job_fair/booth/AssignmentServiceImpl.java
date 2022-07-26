package org.capstone.job_fair.services.impl.job_fair.booth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.capstone.job_fair.constants.AssignmentConstant;
import org.capstone.job_fair.constants.FileConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.AssignmentDTO;
import org.capstone.job_fair.models.dtos.util.ParseFileResult;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.ShiftEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.AssignmentEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.company.CompanyEmployeeRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.repositories.job_fair.ShiftRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.AssignmentRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.JobFairBoothRepository;
import org.capstone.job_fair.services.interfaces.job_fair.booth.AssignmentService;
import org.capstone.job_fair.services.interfaces.util.XSLSFileService;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.capstone.job_fair.services.mappers.job_fair.booth.AssignmentMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;

    @Autowired
    private JobFairBoothRepository jobFairBoothRepository;

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private CompanyEmployeeMapper companyEmployeeMapper;

    @Autowired
    private XSLSFileService xslsFileService;

    @Autowired
    private ShiftRepository shiftRepository;


    @Getter
    @Setter
    @AllArgsConstructor
    private static class ParseException extends Exception {
        private ParseFileResult result;
    }

    private AssignmentDTO updateAssigment(AssignmentEntity entity, String companyId) {
        CompanyEmployeeEntity companyEmployee = entity.getCompanyEmployee();
        if (!companyEmployee.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.NOT_FOUND));
        }
        assignmentRepository.save(entity);
        return assignmentMapper.toDTO(entity);
    }

    private AssignmentDTO createAssignment(String assignerId, String employeeId, String jobFairBoothId, AssignmentType type, String companyId, Long beginTime, Long endTime) {
        Optional<CompanyEmployeeEntity> employeeOpt = companyEmployeeRepository.findByAccountId(employeeId);
        if (!employeeOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMPLOYEE_NOT_FOUND));
        }
        CompanyEmployeeEntity employee = employeeOpt.get();

        Optional<CompanyEmployeeEntity> assignerOpt = companyEmployeeRepository.findByAccountId(assignerId);
        if (!assignerOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMPLOYEE_NOT_FOUND));
        }
        CompanyEmployeeEntity assigner = assignerOpt.get();

        if (!employee.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.NOT_FOUND));
        }
        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findById(jobFairBoothId);
        if (!jobFairBoothOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
        }
        JobFairBoothEntity jobFairBooth = jobFairBoothOpt.get();
        if (!jobFairBooth.getJobFair().getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
        }

        AssignmentEntity entity = new AssignmentEntity();
        entity.setCompanyEmployee(employee);
        entity.setJobFairBooth(jobFairBooth);
        entity.setType(type);
        entity.setBeginTime(beginTime);
        entity.setEndTime(endTime);
        entity.setCreateTime(new Date().getTime());
        entity.setAssigner(assigner);
        assignmentRepository.save(entity);
        return assignmentMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public AssignmentDTO assignEmployee(String assignerId, String employeeId, String jobFairBoothId, AssignmentType type, String companyId, Long beginTime, Long endTime) {
        if (type == AssignmentType.INTERVIEWER || type == AssignmentType.RECEPTION) {
            return createAssignment(assignerId, employeeId, jobFairBoothId, type, companyId, beginTime, endTime);
        }
        List<AssignmentEntity> assignments = assignmentRepository.findByCompanyEmployeeAccountIdAndJobFairBoothId(employeeId, jobFairBoothId);
        if (!assignments.isEmpty()) {
            AssignmentEntity entity = assignments.get(0);
            entity.setType(type);
            entity.setBeginTime(beginTime);
            entity.setEndTime(endTime);
            return updateAssigment(entity, companyId);
        }
        return createAssignment(assignerId, employeeId, jobFairBoothId, type, companyId, beginTime, endTime);
    }

    @Override
    @Transactional
    public AssignmentDTO unAssignEmployee(String assignmentId) {
        Optional<AssignmentEntity> entityOpt = assignmentRepository.findById(assignmentId);
        if (!entityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.NOT_FOUND));
        }
        AssignmentEntity entity = entityOpt.get();
        /*if (!entity.getCompanyEmployee().getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.NOT_FOUND));
        }*/
        assignmentRepository.delete(entity);
        return assignmentMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public AssignmentDTO updateAssignment(String assignmentId, long beginTime, long endTime, AssignmentType type) {
        Optional<AssignmentEntity> entityOpt = assignmentRepository.findById(assignmentId);
        if (!entityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.NOT_FOUND));
        }
        AssignmentEntity entity = entityOpt.get();
        entity.setBeginTime(beginTime);
        entity.setEndTime(endTime);
        entity.setType(type);
        entity = assignmentRepository.save(entity);
        return assignmentMapper.toDTO(entity);
    }

    @Override
    public List<AssignmentDTO> getAssignmentByJobFairId(String jobFairId, String companyId) {
        return assignmentRepository.findByJobFairBoothJobFairIdAndJobFairBoothJobFairCompanyId(jobFairId, companyId).stream().map(assignmentMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AssignmentDTO> getAssigmentByJobFairBoothId(String jobFairBoothId, String companyId) {
        return assignmentRepository.findByJobFairBoothIdAndJobFairBoothJobFairCompanyId(jobFairBoothId, companyId).stream().map(assignmentMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CompanyEmployeeDTO> getAvailableCompanyByJobFairId(String jobFairId, String companyId) {
        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findByIdAndCompanyId(jobFairId, companyId);
        if (!jobFairOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        List<AssignmentEntity> assignmentsInJobFair = assignmentRepository.findByJobFairBoothJobFairIdAndJobFairBoothJobFairCompanyId(jobFairId, companyId);
        List<CompanyEmployeeEntity> companyEmployees = companyEmployeeRepository.findAllByCompanyIdAndAccountRoleId(companyId, Role.COMPANY_EMPLOYEE.ordinal());

        companyEmployees = companyEmployees.stream().filter(companyEmployee -> {
            boolean result = assignmentsInJobFair.stream().anyMatch(assignment -> {
                boolean isUserHasAssignment = assignment.getCompanyEmployee().getAccountId().equals(companyEmployee.getAccountId());
                boolean isDecorator = assignment.getType() == AssignmentType.DECORATOR;
                return (isUserHasAssignment && !isDecorator);
            });
            return !result;
        }).collect(Collectors.toList());
        return companyEmployees.stream().map(companyEmployeeMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Integer getCountAssignedBoothByJobFair(String jobFairId) {
        return assignmentRepository.countDistinctJobFairBoothByJobFairBoothJobFairId(jobFairId);
    }

    @Override
    public Integer getCountAssignedEmployeeByJobFair(String jobFairId) {
        return assignmentRepository.countByJobFairBoothJobFairId(jobFairId);
    }

    @Override
    public Page<AssignmentDTO> getAssignmentByEmployeeIdAndType(String employeeId, AssignmentType type, Pageable pageable) {
        if (type == null)
            return assignmentRepository.findByCompanyEmployeeAccountIdAndJobFairBoothJobFairStatus(employeeId, JobFairPlanStatus.PUBLISH, pageable).map(assignmentMapper::toDTO);
        return assignmentRepository.findByCompanyEmployeeAccountIdAndJobFairBoothJobFairStatusAndType(employeeId, JobFairPlanStatus.PUBLISH, type, pageable).map(assignmentMapper::toDTO);
    }

    @Override
    public Optional<AssignmentDTO> getAssignmentById(String id) {
        return assignmentRepository.findById(id).map(assignmentMapper::toDTO);
    }

    private ParseFileResult<AssignmentDTO> createNewAssignmentsFromListString(List<List<String>> data, String jobFairId, String companyId, CompanyEmployeeEntity assigner) throws ParseException {
        ParseFileResult<AssignmentDTO> parseResult = new ParseFileResult<>();
        int rowNum = data.size();
        List<AssignmentEntity> entities = new ArrayList<>();
        for (int i = 1; i < rowNum; i++) {
            List<String> rowData = data.get(i);

            String employeeId = rowData.get(AssignmentConstant.XLSXFormat.EMPLOYEE_ID_INDEX);
            String slotName = rowData.get(AssignmentConstant.XLSXFormat.SLOT_NAME_INDEX);
            String boothName = rowData.get(AssignmentConstant.XLSXFormat.BOOTH_NAME_INDEX);
            String assignmentTypeString = rowData.get(AssignmentConstant.XLSXFormat.ASSIGMENT_INDEX);
            AssignmentType type = null;
            try {
                type = AssignmentType.valueOf(assignmentTypeString);
                //when assign employee as manager there are no REVIEWER and RECEPTION
                if (type == AssignmentType.INTERVIEWER || type == AssignmentType.RECEPTION) {
                    parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.Assignment.INVALID_ASSIGNMENT_ORGANIZE_JOB_FAIR));
                }
            } catch (IllegalArgumentException e) {
                parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.Assignment.WRONG_ASSIGNMENT_TYPE));
            }

            Optional<CompanyEmployeeEntity> companyEmployeeOpt = companyEmployeeRepository.findByEmployeeIdAndCompanyId(employeeId, companyId);
            if (!companyEmployeeOpt.isPresent()) {
                parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMPLOYEE_NOT_FOUND));
            }
            Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findFirstByJobFairIdAndBoothName(jobFairId, slotName);
            if (!jobFairBoothOpt.isPresent()) {
                parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
            }
            if (boothName.isEmpty() || boothName.length() > 100) {
                parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.JobFairBooth.NAME_INVALID_LENGTH));
            } else {
                if (jobFairBoothOpt.isPresent()) {
                    JobFairBoothEntity jobFairBooth = jobFairBoothOpt.get();
                    jobFairBooth.setName(boothName);
                }
            }


            AssignmentEntity entity = new AssignmentEntity();
            entity.setCompanyEmployee(companyEmployeeOpt.orElse(null));
            entity.setJobFairBooth(jobFairBoothOpt.orElse(null));
            entity.setType(type);
            entity.setCreateTime(new Date().getTime());
            entity.setAssigner(assigner);

            entities.add(entity);
        }
        if (!parseResult.isHasError()) {
            for (int i = 0; i < entities.size(); i++) {
                AssignmentEntity assignmentEntity = entities.get(i);
                try {

                    List<CompanyEmployeeDTO> availableEmployees = this.getAvailableCompanyByJobFairId(jobFairId, companyId);
                    CompanyEmployeeEntity companyEmployee = assignmentEntity.getCompanyEmployee();
                    boolean isEmployeeAvailable = availableEmployees.stream().anyMatch(dto -> dto.getAccountId().equals(companyEmployee.getAccountId()));
                    if (!isEmployeeAvailable) {
                        parseResult.addErrorMessage(i + 1, MessageUtil.getMessage(MessageConstant.Assignment.UNAVAILABLE_EMPLOYEE));
                        throw new ParseException(parseResult);
                    }
                    if (!parseResult.isHasError()) {
                        jobFairBoothRepository.save(assignmentEntity.getJobFairBooth());
                        assignmentEntity = assignmentRepository.save(assignmentEntity);
                        AssignmentDTO dto = assignmentMapper.toDTO(assignmentEntity);
                        parseResult.addToResult(dto);
                    }
                } catch (Exception e) {
                    //+1 because row start at 1
                    parseResult.addErrorMessage(i + 1, e.getMessage());
                }
            }
        }
        return parseResult;
    }

    @SneakyThrows
    private ParseFileResult<AssignmentDTO> parseExcelFile(MultipartFile file, String jobFairId, String companyId, CompanyEmployeeEntity assigner) {
        ParseFileResult<AssignmentDTO> parseResult;
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        if (workbook.getNumberOfSheets() != 1) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.XSL_NO_SHEET));
        }
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> data = xslsFileService.readXSLSheet(sheet, AssignmentConstant.XLSXFormat.COLUMN_NUM);
        try {
            parseResult = createNewAssignmentsFromListString(data, jobFairId, companyId, assigner);
        } catch (ParseException e) {
            parseResult = e.getResult();
        }

        if (parseResult.isHasError()) {
            String url = xslsFileService.uploadErrorXSLFile(workbook, parseResult.getErrors(), file.getOriginalFilename(), AssignmentConstant.XLSXFormat.ERROR_INDEX);
            parseResult.setErrorFileUrl(url);
        }

        return parseResult;
    }

    @SneakyThrows
    private ParseFileResult<AssignmentDTO> parseCsvFile(MultipartFile file, String jobFairId, String companyId, CompanyEmployeeEntity assigner) {
        ParseFileResult<AssignmentDTO> parseResult;
        List<List<String>> data = xslsFileService.readCSVFile(file.getInputStream());
        try {
            parseResult = createNewAssignmentsFromListString(data, jobFairId, companyId, assigner);
        } catch (ParseException e) {
            parseResult = e.getResult();
        }
        if (parseResult.isHasError()) {
            String url = xslsFileService.uploadErrorCSVFile(data, parseResult.getErrors(), file.getOriginalFilename());
            parseResult.setErrorFileUrl(url);
        }
        return parseResult;
    }

    @Override
    @SneakyThrows
    @Transactional
    public ParseFileResult<AssignmentDTO> createNewAssignmentsFromFile(MultipartFile file, String jobFairId, String companyId, String assignerId) {
        Optional<CompanyEmployeeEntity> assignerOpt = companyEmployeeRepository.findByAccountId(assignerId);
        if (!assignerOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMPLOYEE_NOT_FOUND));
        }
        CompanyEmployeeEntity assigner = assignerOpt.get();
        //check for invalid type
        List<String> allowTypes = Arrays.asList(FileConstant.CSV_CONSTANT.TYPE, FileConstant.XLS_CONSTANT.TYPE, FileConstant.XLSX_CONSTANT.TYPE);
        String fileType = file.getContentType();
        if (!allowTypes.contains(fileType)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.NOT_ALLOWED));
        }
        ParseFileResult<AssignmentDTO> parseResult;
        if (Objects.equals(fileType, FileConstant.XLSX_CONSTANT.TYPE) || Objects.equals(fileType, FileConstant.XLS_CONSTANT.TYPE)) {
            parseResult = parseExcelFile(file, jobFairId, companyId, assigner);
            return parseResult;
        }
        parseResult = parseCsvFile(file, jobFairId, companyId, assigner);
        return parseResult;
    }

    @Override
    public List<CompanyEmployeeDTO> getAvailableInterviewer(String jobFairBoothId) {
        long now = new Date().getTime();
        List<AssignmentEntity> assignments = assignmentRepository.findByJobFairBoothIdAndType(jobFairBoothId, AssignmentType.INTERVIEWER);
        assignments = assignments.stream().filter(assignment -> assignment.getEndTime() > now).collect(Collectors.toList());
        assignments.sort((o1, o2) -> Math.toIntExact(o1.getBeginTime() - o2.getBeginTime()));
        List<CompanyEmployeeEntity> employees = new ArrayList<>();
        assignments.forEach(assignment -> {
            if (!employees.contains(assignment.getCompanyEmployee())) {
                employees.add(assignment.getCompanyEmployee());
            }
        });

        return employees.stream().map(companyEmployeeMapper::toDTO).collect(Collectors.toList());
    }

    private String getShiftTime(String name, Long jobFairStartTime, Long jobFairEndTime, int day, ShiftEntity morningShift, ShiftEntity afternoonShift) {
        final Long ONE_DAY = 24 * 60 * 60 * 1000L;
        final Long MORNING_SHIFT_START = morningShift.getBeginTime();
        final Long MORNING_SHIFT_END = morningShift.getEndTime();
        final Long AFTERNOON_SHIFT_START = afternoonShift.getBeginTime();
        final Long AFTERNOON_SHIFT_END = afternoonShift.getEndTime();
        Long timeTmp = 0L;
        String shiftTime = "";
        switch (name) {
            case "morning":
                timeTmp = jobFairStartTime + (day - 1) * ONE_DAY + MORNING_SHIFT_START;

                if (timeTmp > jobFairEndTime) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.SHIFT_TIME_OVER_JOB_FAIR_END_TIME));
                } else {
                    shiftTime = Long.toString(timeTmp);
                }

                timeTmp = jobFairStartTime + (day - 1) * ONE_DAY + MORNING_SHIFT_END;
                if (timeTmp > jobFairEndTime) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.SHIFT_TIME_OVER_JOB_FAIR_END_TIME));
                } else {
                    shiftTime += "," + Long.toString(timeTmp);
                }
                break;
            case "afternoon":
                timeTmp = jobFairStartTime + (day - 1) * ONE_DAY + AFTERNOON_SHIFT_START;
                if (timeTmp > jobFairEndTime) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.SHIFT_TIME_OVER_JOB_FAIR_END_TIME));
                } else {
                    shiftTime = Long.toString(timeTmp);
                }

                timeTmp = jobFairStartTime + (day - 1) * ONE_DAY + AFTERNOON_SHIFT_END;
                if (timeTmp > jobFairEndTime) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.SHIFT_TIME_OVER_JOB_FAIR_END_TIME));
                } else {
                    shiftTime += "," + Long.toString(timeTmp);
                }
                break;
            default:
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Assignment.INVALID_SHIFT_NAME));
        }
        return shiftTime;
    }

    private ParseFileResult<AssignmentDTO> createShiftAssignmentFromListString(List<List<String>> data, String jobFairBoothId, String companyId, CompanyEmployeeEntity assigner) throws ParseException {
        ParseFileResult<AssignmentDTO> parseResult = new ParseFileResult<>();
        int rowNum = data.size();

        List<AssignmentEntity> entities = new ArrayList<>();
        for (int i = 1; i < rowNum; i++) {
            List<String> rowData = data.get(i);
            boolean isEmpty = true;
            String employeeId = rowData.get(0);
            String jobFairDay = rowData.get(1);
            String shift = rowData.get(2);
            String assignmentTypeString = rowData.get(3);
            isEmpty = (employeeId.trim().isEmpty() || jobFairDay.trim().isEmpty() || shift.trim().isEmpty() || assignmentTypeString.trim().isEmpty());
            if (!isEmpty) {
                AssignmentType type = null;
                //Only allow interviewer and receptionist
                try {
                    type = AssignmentType.valueOf(assignmentTypeString);
                    if (type == AssignmentType.SUPERVISOR || type == AssignmentType.DECORATOR || type == AssignmentType.STAFF) {
                        parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.Assignment.INVALID_ASSIGNMENT_ORGANIZE_JOB_FAIR));
                    }
                } catch (IllegalArgumentException e) {
                    parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.Assignment.WRONG_ASSIGNMENT_TYPE));
                }
                //Find company employee
                Optional<CompanyEmployeeEntity> companyEmployeeOpt = companyEmployeeRepository.findByEmployeeIdAndCompanyId(employeeId, companyId);
                if (!companyEmployeeOpt.isPresent()) {
                    parseResult.addErrorMessage(i, MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMPLOYEE_NOT_FOUND));
                }
                //Get all shift of job fair
                Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findById(jobFairBoothId);
                if (!jobFairBoothOpt.isPresent()) {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
                }
                JobFairBoothEntity jobFairBooth = jobFairBoothOpt.get();
                List<ShiftEntity> shiftEntities = shiftRepository.findAllByJobFairId(jobFairBooth.getJobFair().getId());
                ShiftEntity morningShift, afternoonShift;
                //Get morning shift and afternoon shift
                if (shiftEntities.get(0).getBeginTime() < shiftEntities.get(1).getBeginTime()) {
                    morningShift = shiftEntities.get(0);
                    afternoonShift = shiftEntities.get(1);
                } else {
                    morningShift = shiftEntities.get(1);
                    afternoonShift = shiftEntities.get(0);
                }

                AssignmentEntity entity = new AssignmentEntity();
                entity.setCompanyEmployee(companyEmployeeOpt.get());
                String shiftTime = null;
                try {
                    shiftTime = getShiftTime(shift, jobFairBooth.getJobFair().getPublicStartTime(), jobFairBooth.getJobFair().getPublicEndTime(), Integer.parseInt(jobFairDay), morningShift, afternoonShift);
                    entity.setBeginTime(Long.parseLong(shiftTime.split(",")[0]));
                    entity.setEndTime(Long.parseLong(shiftTime.split(",")[1]));
                } catch (IllegalArgumentException e) {
                    parseResult.addErrorMessage(i, e.getMessage());
                }
                entity.setType(type);
                entity.setAssigner(assigner);
                entities.add(entity);
            }
        }
        if (!parseResult.isHasError()) {
            for (int i = 0; i < entities.size(); i++) {
                AssignmentEntity assignmentEntity = entities.get(i);
                try {
                    if (!parseResult.isHasError()) {
                        AssignmentDTO dto = null;
                        Optional<AssignmentEntity> assignmentEntityOpt = assignmentRepository.findAssignmentEntityByJobFairBoothIdAndCompanyEmployeeAccountIdAndBeginTimeAndEndTime(jobFairBoothId, assignmentEntity.getCompanyEmployee().getAccountId(), assignmentEntity.getBeginTime(), assignmentEntity.getEndTime());
                        if (assignmentEntityOpt.isPresent())
                            dto = updateAssignment(assignmentEntityOpt.get().getId(), assignmentEntity.getBeginTime(), assignmentEntity.getEndTime(), assignmentEntity.getType());
                        else
                            dto = assignEmployee(assigner.getAccountId(), assignmentEntity.getCompanyEmployee().getAccountId(), jobFairBoothId, assignmentEntity.getType(), assignmentEntity.getCompanyEmployee().getCompany().getId(), assignmentEntity.getBeginTime(), assignmentEntity.getEndTime());
                        parseResult.addToResult(dto);
                    }
                } catch (Exception e) {
                    //+1 because row start at 1
                    parseResult.addErrorMessage(i + 1, e.getMessage());
                }
            }
        }
        return parseResult;
    }

    @Override
    @SneakyThrows
    @Transactional
    public ParseFileResult<AssignmentDTO> assignShiftForMultipleEmployee(MultipartFile file, String jobFairId, String companyId, String assignerId) {
        Optional<CompanyEmployeeEntity> assignerOpt = companyEmployeeRepository.findByAccountId(assignerId);
        if (!assignerOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMPLOYEE_NOT_FOUND));
        }
        CompanyEmployeeEntity assigner = assignerOpt.get();
        //check for invalid type
        List<String> allowTypes = Arrays.asList(FileConstant.CSV_CONSTANT.TYPE, FileConstant.XLS_CONSTANT.TYPE, FileConstant.XLSX_CONSTANT.TYPE);
        String fileType = file.getContentType();
        if (!allowTypes.contains(fileType)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.NOT_ALLOWED));
        }
        ParseFileResult<AssignmentDTO> parseResult;
        List<List<String>> data = null;

        //Excel file
        if (Objects.equals(fileType, FileConstant.XLSX_CONSTANT.TYPE) || Objects.equals(fileType, FileConstant.XLS_CONSTANT.TYPE)) {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            if (workbook.getNumberOfSheets() != 1) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.XSL_NO_SHEET));
            }
            Sheet sheet = workbook.getSheetAt(0);
            data = xslsFileService.readXSLSheet(sheet, AssignmentConstant.XLSXFormat.COLUMN_NUM);
            try {
                parseResult = createShiftAssignmentFromListString(data, jobFairId, companyId, assigner);
            } catch (ParseException e) {
                parseResult = e.getResult();
            }
            if (parseResult.isHasError()) {
                String url = xslsFileService.uploadErrorXSLFile(workbook, parseResult.getErrors(), file.getOriginalFilename(), AssignmentConstant.XLSXFormat.ERROR_INDEX);
                parseResult.setErrorFileUrl(url);
            }
            return parseResult;
        }
        //CSV file
        data = xslsFileService.readCSVFile(file.getInputStream());
        parseResult = createShiftAssignmentFromListString(data, jobFairId, companyId, assigner);
        return parseResult;
    }


}
