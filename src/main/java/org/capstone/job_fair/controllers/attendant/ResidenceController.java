package org.capstone.job_fair.controllers.attendant;


import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.attendant.CreateResidenceRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.UpdateResidenceRequest;
import org.capstone.job_fair.models.dtos.attendant.JobLevelDTO;
import org.capstone.job_fair.models.dtos.attendant.ResidenceDTO;
import org.capstone.job_fair.models.entities.attendant.ResidenceEntity;
import org.capstone.job_fair.services.interfaces.attendant.JobLevelService;
import org.capstone.job_fair.services.interfaces.attendant.ResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ResidenceController {

    @Autowired
    private ResidenceService residenceService;


    @GetMapping(ApiEndPoint.Residence.RESIDENCE_ENDPOINT + "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        Optional<ResidenceDTO> dtoOptional = residenceService.findById(id);
        return dtoOptional.isPresent() ? ResponseEntity.ok(dtoOptional.get()) : ResponseEntity.noContent().build();
    }

    @GetMapping(ApiEndPoint.Residence.RESIDENCE_ENDPOINT)
    public ResponseEntity<?> getAll() {
        List<ResidenceDTO> dtoList = residenceService.getAll();
        return dtoList.size() == 0 ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtoList);
    }

    @PutMapping(ApiEndPoint.Residence.RESIDENCE_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> create(@RequestBody @Validated CreateResidenceRequest request) {
        ResidenceDTO dto = new ResidenceDTO(null, request.getName());
        dto = residenceService.create(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(ApiEndPoint.Residence.RESIDENCE_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> update(@RequestBody @Validated UpdateResidenceRequest request) {
        ResidenceDTO dto = new ResidenceDTO();
        dto.setId(request.getId());
        dto.setName(request.getName());
        dto = residenceService.update(dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(ApiEndPoint.Residence.RESIDENCE_ENDPOINT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        ResidenceDTO dto = residenceService.delete(id);
        return ResponseEntity.ok(dto);
    }

}
