package org.capstone.job_fair.controllers.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.RegisterAttendantRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.capstone.job_fair.services.mappers.AttendantMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class AttendantController {

    @Autowired
    private AttendantMapper attendantMapper;

    @Autowired
    private AttendantService attendantService;

    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT)
    public ResponseEntity<List<AttendantDTO>> getAllAccounts() {
        return new ResponseEntity<>(attendantService.getAllAttendants(), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PutMapping(ApiEndPoint.Attendant.UPDATE_ENDPOINT)
    public ResponseEntity<?> update(@Validated @RequestBody UpdateAttendantRequest request) {
        try {
            AttendantDTO attendantDTO = attendantMapper.toDTO(request);
            attendantService.updateAccount(attendantDTO);
        } catch (NoSuchElementException | IllegalArgumentException ex ) {
            return GenericResponse.build(MessageUtil.getMessage(ex.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.Attendant.UPDATE_PROFILE_SUCCESSFULLY),
                HttpStatus.OK);
    }

    @PostMapping(ApiEndPoint.Attendant.REGISTER_ENDPOINT)
    public ResponseEntity<?> register(@Validated @RequestBody RegisterAttendantRequest req) {

        try {
            AttendantDTO attendantDTO = attendantMapper.toDTO(req);
            attendantService.createNewAccount(attendantDTO);
        } catch (NoSuchElementException | IllegalArgumentException ex ) {
            return GenericResponse.build(MessageUtil.getMessage(ex.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }

        return GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.Attendant.REGISTER_SUCCESSFULLY),
                HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT + "/{id}")
    public ResponseEntity<?> getAttendant(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(attendantService.getAttendantById(id));
    }

}
