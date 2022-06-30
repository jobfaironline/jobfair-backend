package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.controllers.payload.requests.job_fair.DraftJobFairRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateJobFairRequest;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.dtos.job_fair.ShiftDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.ShiftEntity;
import org.capstone.job_fair.services.mappers.company.CompanyMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        CompanyMapper.class, ShiftMapper.class
})
public abstract class JobFairMapper {

    @Autowired
    private ShiftMapper shiftMapper;

    @Mapping(target = "jobFairBoothList", ignore = true)
    public abstract JobFairEntity toEntity(JobFairDTO dto);

    public abstract JobFairDTO toDTO(JobFairEntity entity);

    @Mapping(target = "jobFairBoothList", ignore = true)
    public abstract void updateFromDTO(@MappingTarget JobFairEntity entity, JobFairDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "cancelReason", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "shifts", ignore = true)
    public abstract JobFairDTO toDTO(DraftJobFairRequest request);

    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "cancelReason", ignore = true)
    @Mapping(target = "company", ignore = true)
    public abstract JobFairDTO toDTO(UpdateJobFairRequest request);

    public void mapToShiftList(List<ShiftDTO> dtos, @MappingTarget List<ShiftEntity> entities) {
        List<ShiftEntity> newEntities = new ArrayList<>();
        if (entities != null) {
            for (ShiftDTO dto : dtos) {
                Optional<ShiftEntity> entityOpt = entities.stream().filter(entity1 -> entity1.getId().equals(dto.getId())).findFirst();
                if (entityOpt.isPresent()) {
                    ShiftEntity entity = entityOpt.get();
                    entity.setBeginTime(dto.getBeginTime());
                    entity.setEndTime(dto.getEndTime());
                } else {
                    newEntities.add(shiftMapper.toEntity(dto));
                }
            }
            entities.addAll(newEntities);
        } else {
            entities = dtos.stream().map(dto -> shiftMapper.toEntity(dto)).collect(Collectors.toList());
        }

    }

}
