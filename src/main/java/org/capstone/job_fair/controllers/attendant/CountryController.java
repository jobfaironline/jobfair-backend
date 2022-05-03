package org.capstone.job_fair.controllers.attendant;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.attendant.CreateCountryRequest;
import org.capstone.job_fair.models.dtos.attendant.CountryDTO;
import org.capstone.job_fair.services.interfaces.attendant.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class CountryController {

    @Autowired
    CountryService countryService;


    @GetMapping(ApiEndPoint.Country.COUNTRY_ENPOINT + "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Optional<CountryDTO> dtoOptional = countryService.findById(id);
        return dtoOptional.isPresent() ? ResponseEntity.ok(dtoOptional.get()) : ResponseEntity.notFound().build();
    }

    @GetMapping(ApiEndPoint.Country.COUNTRY_ENPOINT)
    public ResponseEntity<?> getAll() {
        List<CountryDTO> dtoList = countryService.getAll();
        return dtoList.size() == 0 ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtoList);
    }

    @PostMapping(ApiEndPoint.Country.COUNTRY_ENPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> create(@RequestBody @Validated CreateCountryRequest request) {
        CountryDTO dto = new CountryDTO(null, request.getName());
        dto = countryService.create(dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(ApiEndPoint.Country.COUNTRY_ENPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> update(@RequestBody @Validated CountryDTO request) {
        request = countryService.update(request);
        return ResponseEntity.ok(request);
    }

    @DeleteMapping(ApiEndPoint.Country.COUNTRY_ENPOINT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        CountryDTO dto = countryService.delete(id);
        return ResponseEntity.ok(dto);
    }



}
