package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.company.ProfessionCategoryDTO;
import org.capstone.job_fair.models.dtos.company.SkillTagDTO;
import org.capstone.job_fair.services.interfaces.company.ProfessionalCategoryService;
import org.capstone.job_fair.services.interfaces.company.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SkillTagController {


    @Autowired
    private SkillService skillService;

    @GetMapping(ApiEndPoint.Skill.SKILL_ENDPOINT + "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Optional<SkillTagDTO> dtoOptional = skillService.findById(id);
        return dtoOptional.isPresent() ? ResponseEntity.ok(dtoOptional.get()) : ResponseEntity.noContent().build();
    }

    @GetMapping(ApiEndPoint.Skill.SKILL_ENDPOINT)
    public ResponseEntity<?> getAll() {
        List<SkillTagDTO> dtoList = skillService.getAll();
        return dtoList.size() == 0 ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtoList);
    }

    @PutMapping(ApiEndPoint.Skill.SKILL_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> create(@RequestBody @Validated SkillTagDTO request) {
        SkillTagDTO dto = new SkillTagDTO(request.getId(), request.getName());
        dto = skillService.create(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(ApiEndPoint.Skill.SKILL_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> update(@RequestBody @Validated SkillTagDTO request) {
        request = skillService.update(request);
        return ResponseEntity.ok(request);
    }

    @DeleteMapping(ApiEndPoint.Skill.SKILL_ENDPOINT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        SkillTagDTO dto = skillService.delete(id);
        return ResponseEntity.ok(dto);
    }
}
