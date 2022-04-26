package org.capstone.job_fair.controllers.account;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.account.RoleDTO;
import org.capstone.job_fair.models.dtos.attendant.profile.QualificationDTO;
import org.capstone.job_fair.services.interfaces.account.RoleService;
import org.capstone.job_fair.services.interfaces.attendant.profile.QualificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;


    @GetMapping(ApiEndPoint.Role.ROLE_ENDPOINT + "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Optional<RoleDTO> dtoOptional = roleService.findById(id);
        return dtoOptional.isPresent() ? ResponseEntity.ok(dtoOptional.get()) : ResponseEntity.noContent().build();
    }

    @GetMapping(ApiEndPoint.Role.ROLE_ENDPOINT)
    public ResponseEntity<?> getAll() {
        List<RoleDTO> dtoList = roleService.getAll();
        return dtoList.size() == 0 ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtoList);
    }


}
