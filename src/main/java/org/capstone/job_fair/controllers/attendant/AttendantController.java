package org.capstone.job_fair.controllers.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.AttendantRequest;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.CountryDTO;
import org.capstone.job_fair.models.dtos.attendant.JobLevelDTO;
import org.capstone.job_fair.models.dtos.attendant.ResidenceDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.controllers.payload.AttendantRegisterRequest;
import org.capstone.job_fair.controllers.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AttendantController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AttendantService attendantService;

    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT)
    public ResponseEntity<List<AccountEntity>> getAllAccounts() {
        return new ResponseEntity<List<AccountEntity>>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    private AttendantDTO mappingRegisterRequestToDTO(AttendantRegisterRequest request) {
        AccountDTO dto = new AccountDTO();
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setFirstname(request.getFirstName());
        dto.setLastname(request.getLastName());
        dto.setFirstname(request.getFirstName());
        dto.setGender(request.getGender());

        AttendantDTO attendantDTO = new AttendantDTO();
        attendantDTO.setAccount(dto);
        attendantDTO.setAccountId(dto.getId());
        attendantDTO.setAccount(dto);
        return attendantDTO;
    }

    @PostMapping(ApiEndPoint.Attendant.REGISTER_ENDPOINT)
    public ResponseEntity<?> register(@Validated @RequestBody AttendantRegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return GenericMessageResponseEntity.build("Confirm password mismatch", HttpStatus.BAD_REQUEST);
        }
        Optional<AccountEntity> existedAccount = accountService.getActiveAccountByEmail(request.getEmail());
        if (existedAccount.isPresent()) {
            return GenericMessageResponseEntity.build("Existed account", HttpStatus.BAD_REQUEST);
        }

        AttendantDTO attendantDTO = mappingRegisterRequestToDTO(request);
        attendantService.createNewAccount(attendantDTO);
        return GenericMessageResponseEntity.build("Noice", HttpStatus.OK);
    }

    @PostMapping(ApiEndPoint.Attendant.UPDATE_ENDPOINT)
    public ResponseEntity<?> update(@Validated @RequestBody AttendantRequest req) {
        AccountDTO accountDTO = req.getAccount() != null ? AccountDTO.builder()
                .lastname(req.getAccount().getLastname())
                .firstname(req.getAccount().getFirstname())
                .middlename(req.getAccount().getMiddlename())
                .email(req.getAccount().getEmail())
                .gender(req.getAccount().getGender())
                .phone(req.getAccount().getPhone())
                .profileImageUrl(req.getAccount().getProfileImageUrl())
                .build() : new AccountDTO();
        CountryDTO countryDTO = req.getCountry() != null ? CountryDTO.builder()
                .id(req.getCountry().getId())
                .description(req.getCountry().getDescription())
                .name(req.getCountry().getName())
                .build() : new CountryDTO();
        ResidenceDTO residenceDTO = req.getResidence() != null ? ResidenceDTO.builder()
                .id(req.getResidence().getId())
                .name(req.getResidence().getName())
                .build() : new ResidenceDTO();
        JobLevelDTO jobLevelRequest = req.getCurrentJobLevel() !=null ? JobLevelDTO.builder()
                .id(req.getCurrentJobLevel().getId())
                .name(req.getCurrentJobLevel().getName())
                .description(req.getCurrentJobLevel().getDescription())
                .build() : new JobLevelDTO();
        AttendantDTO dto = AttendantDTO.builder()
                .accountId(req.getAccountId())
                .account(accountDTO)
                .address(req.getAddress())
                .dob(req.getDob())
                .country(countryDTO)
                .currentJobLevel(jobLevelRequest)
                .yearOfExp(req.getYearOfExp())
                .title(req.getTitle())
                .residence(residenceDTO)
                .maritalStatus(req.getMaritalStatus())
                .build();
        attendantService.update(dto);
        return GenericMessageResponseEntity.build("", HttpStatus.OK);
    }

    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT + "/{email}")
    public ResponseEntity<?> getAttendant(@PathVariable("email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(attendantService.getAttendantByEmail(email));
    }
}
