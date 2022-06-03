package org.capstone.job_fair.controllers.company;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CreateCompanyRequest;
import org.capstone.job_fair.controllers.payload.requests.company.UpdateCompanyRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.mappers.company.CompanyMapper;
import org.capstone.job_fair.utils.ImageUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@RestController
@Slf4j
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @Autowired
    private FileStorageService fileStorageService;


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> getCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping(ApiEndPoint.Company.COMPANY_ENDPOINT + "/" + "{id}")
    public ResponseEntity<?> getByID(@PathVariable(value = "id") String id) {
        Optional<CompanyDTO> opt = companyService.getCompanyById(id);
        if (opt.isPresent()) {
            return new ResponseEntity<>(opt.get(), HttpStatus.OK);
        }
        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @DeleteMapping(ApiEndPoint.Company.COMPANY_ENDPOINT + "/" + "{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
        Boolean result = companyService.deleteCompany(id);
        if (result) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.DELETE_SUCCESSFULLY), HttpStatus.OK);
        }
        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.DELETE_FAILED), HttpStatus.NOT_FOUND);
    }

    @PostMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> create(@Validated @RequestBody CreateCompanyRequest request) {
        try {
            CompanyDTO dto = companyMapper.toDTO(request);
            return new ResponseEntity<>(companyService.createCompany(dto), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiEndPoint.Company.COMPANY_LOGO_ENPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> uploadCompanyLogo(@RequestParam("file") MultipartFile file) {
        CompanyDTO companyDTO;
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userId = userDetails.getId();
            CompanyEmployeeDTO companyEmployeeDTO = companyEmployeeService.getCompanyEmployeeByAccountId(userId).get();
            String companyId = companyEmployeeDTO.getCompanyDTO().getId();
            byte[] image = ImageUtil.convertImage(file, DataConstraint.Company.COMPANY_LOGO_TYPE, DataConstraint.Company.WIDTH_FACTOR, DataConstraint.Company.HEIGHT_FACTOR, DataConstraint.Company.COMPANY_LOGO_EXTENSION_TYPE);
            String companyLogoFolder = AWSConstant.COMPANY_LOGO_FOLDER;
            companyDTO = companyService.updateCompanyLogo(companyLogoFolder, companyId);
            fileStorageService.store(image, companyLogoFolder + "/" + companyDTO.getId());
        } catch (IOException e) {
            return GenericResponse.build((e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.created(URI.create(companyDTO.getCompanyLogoURL())).build();
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PutMapping(ApiEndPoint.Company.COMPANY_ENDPOINT + "/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody UpdateCompanyRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        if (!companyId.equals(id)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.COMPANY_MISSMATCH));
        }
        CompanyDTO dto = companyMapper.toDTO(request);
        dto.setId(id);
        companyService.updateCompany(dto);
        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.UPDATE_SUCCESSFULLY), HttpStatus.OK);
    }


}
