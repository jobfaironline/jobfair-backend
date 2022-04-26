package org.capstone.job_fair.controllers.attendant;


import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.account.GenderDTO;
import org.capstone.job_fair.models.dtos.attendant.JobLevelDTO;
import org.capstone.job_fair.services.interfaces.account.GenderService;
import org.capstone.job_fair.services.interfaces.attendant.JobLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class JobLevelController {
    @Autowired
    private JobLevelService jobLevelService;


    @GetMapping(ApiEndPoint.JobLevel.JOB_LEVEL_ENDPOINT + "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Optional<JobLevelDTO> dtoOptional = jobLevelService.findById(id);
        return dtoOptional.isPresent() ? ResponseEntity.ok(dtoOptional.get()) : ResponseEntity.noContent().build();
    }

    @GetMapping(ApiEndPoint.JobLevel.JOB_LEVEL_ENDPOINT)
    public ResponseEntity<?> getAll() {
        List<JobLevelDTO> dtoList = jobLevelService.getAll();
        return dtoList.size() == 0 ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtoList);
    }

    @PostMapping(ApiEndPoint.JobLevel.JOB_LEVEL_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> create(@RequestBody @Validated JobLevelDTO request) {
        JobLevelDTO dto = new JobLevelDTO(request.getId(), request.getName());
        dto = jobLevelService.create(dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(ApiEndPoint.JobLevel.JOB_LEVEL_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> update(@RequestBody @Validated JobLevelDTO request) {
        request = jobLevelService.update(request);
        return ResponseEntity.ok(request);
    }

    @DeleteMapping(ApiEndPoint.JobLevel.JOB_LEVEL_ENDPOINT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        JobLevelDTO dto = jobLevelService.delete(id);
        return ResponseEntity.ok(dto);
    }


}
