package org.capstone.job_fair.controllers.job_fair;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.controllers.payload.responses.RenderJobFairParkResponse;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
import org.capstone.job_fair.models.dtos.company.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothLayoutService;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothService;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class JobFairController {

    @Autowired
    private CompanyBoothService companyBoothService;

    @Autowired
    private CompanyBoothLayoutService companyBoothLayoutService;

    @Autowired
    private LayoutService layoutService;

    @GetMapping(ApiEndPoint.JobFair.FOR_3D_MAP + "/{id}")
    public ResponseEntity<?> getJobFairInformationFor3DMap(@PathVariable("id") String jobFairId) {
        RenderJobFairParkResponse response = new RenderJobFairParkResponse();

        Optional<LayoutDTO> layoutDTOOpt = layoutService.getByJobFairId(jobFairId);

        if (!layoutDTOOpt.isPresent()) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Layout.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        LayoutDTO layoutDTO = layoutDTOOpt.get();
        response.setJobFairLayoutUrl(layoutDTO.getUrl());

        layoutDTO.getBooths().forEach(boothDTO -> {
            Optional<JobFairBoothDTO> companyBoothOpt = companyBoothService.getCompanyBoothByJobFairIdAndBoothId(jobFairId, boothDTO.getId());
            if (companyBoothOpt.isPresent()) {
                JobFairBoothDTO companyBooth = companyBoothOpt.get();

                RenderJobFairParkResponse.BoothData boothData = new RenderJobFairParkResponse.BoothData();
                boothData.setPosition(boothDTO.getX(), boothDTO.getY(), boothDTO.getZ());
                boothData.setSlotName(boothDTO.getName());
                boothData.setCompanyBoothId(companyBooth.getId());

                Optional<JobFairBoothLayoutDTO> layoutDTOOptional = companyBoothLayoutService.getLatestVersionByCompanyBoothId(companyBooth.getId());
                if (layoutDTOOptional.isPresent()) {
                    boothData.setBoothUrl(layoutDTOOptional.get().getUrl());
                    boothData.setCompanyBoothLayoutVideos(layoutDTOOptional.get().getCompanyBoothLayoutVideos());
                    response.addBoothDataInformation(boothData);
                }
            }
        });

        return ResponseEntity.ok(response);

    }


}
