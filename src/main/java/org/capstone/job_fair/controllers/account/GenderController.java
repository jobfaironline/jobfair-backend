package org.capstone.job_fair.controllers.account;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.account.GenderDTO;
import org.capstone.job_fair.services.interfaces.account.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class GenderController {

    @Autowired
    private GenderService genderService;


    @GetMapping(ApiEndPoint.Gender.GENDER_ENDPOINT + "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Optional<GenderDTO> dtoOptional = genderService.findById(id);
        return dtoOptional.isPresent() ? ResponseEntity.ok(dtoOptional.get()) : ResponseEntity.noContent().build();
    }

    @GetMapping(ApiEndPoint.Gender.GENDER_ENDPOINT)
    public ResponseEntity<?> getAll() {
        List<GenderDTO> dtoList = genderService.getAll();
        return dtoList.size() == 0 ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtoList);
    }

    @PutMapping(ApiEndPoint.Gender.GENDER_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> createGender(@RequestBody @Validated GenderDTO request) {
        GenderDTO dto = new GenderDTO(request.getId(), request.getName(), request.getDescription());
        dto = genderService.createGender(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(ApiEndPoint.Gender.GENDER_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> updateGender(@RequestBody @Validated GenderDTO request) {
        request = genderService.updateGender(request);
        return ResponseEntity.ok(request);
    }

    @DeleteMapping(ApiEndPoint.Gender.GENDER_ENDPOINT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> deleteGender(@PathVariable("id") int id) {
        GenderDTO dto = genderService.deleteGender(id);
        return ResponseEntity.ok(dto);
    }


}
