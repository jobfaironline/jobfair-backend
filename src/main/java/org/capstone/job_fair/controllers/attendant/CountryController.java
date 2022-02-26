package org.capstone.job_fair.controllers.attendant;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.attendant.CountryDTO;
import org.capstone.job_fair.services.interfaces.attendant.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CountryController {

    @Autowired
    CountryService countryService;

    @GetMapping(ApiEndPoint.Country.COUNTRY_ENPOINT)
    public ResponseEntity<?> getAllCountries() {
        List<CountryDTO> countries = countryService.getAllCountries();
        if (countries.isEmpty()) ResponseEntity.notFound();
        return ResponseEntity.ok(countries);
    }


}
