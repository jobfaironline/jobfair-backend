package org.capstone.job_fair.services.impl.attendant.cv;

import com.amazonaws.util.json.Jackson;
import org.apache.commons.collections4.map.HashedMap;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.SkillExtractorApiEndpoint;
import org.capstone.job_fair.controllers.payload.responses.KeyWordResponse;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvCertificationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.repositories.attendant.cv.CvActivityRepository;
import org.capstone.job_fair.repositories.attendant.cv.CvEducationRepository;
import org.capstone.job_fair.repositories.attendant.cv.CvRepository;
import org.capstone.job_fair.repositories.attendant.cv.CvWorkHistoryRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.mappers.attendant.cv.CvMapper;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CvServiceImpl implements CvService {
    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private CvMapper cvMapper;

    @Autowired
    private AwsUtil awsUtil;

    @Value("${skill.processor.url}")
    private String skillProcessorURL;

    @Autowired
    private WebClient webClient;

    @Autowired
    private CvWorkHistoryRepository cvWorkHistoryRepository;

    @Autowired
    private CvEducationRepository cvEducationRepository;

    @Autowired
    private CvActivityRepository cvActivityRepository;

    @Autowired
    private Clock clock;

    private void validateCertification(CvCertificationEntity entity) {
        //If certification has expiration date => we need to validate issue date and expired date are not null
        //And issue date is less than expired date
        //If certification doesn't have expiration date => don't care about expired date
        //Issue date has been validated not null in request
        if (!entity.getDoesNotExpired()) {
            if (entity.getExpiredDate() == null) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Certification.EXPIRED_DATE_NOT_FOUND));
            }
            if (entity.getIssueDate() > entity.getExpiredDate()) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Certification.ISSUE_DATE_AND_EXPIRED_DATE_RANGE_ERROR));
            }
        }

    }

    @Override
    @Transactional
    public CvDTO draftCv(CvDTO dto) {
        CvEntity entity = cvMapper.toEntity(dto);
        entity.setName("Untitled");
        entity.setUpdateTime(clock.millis());
        entity.setCreateTime(clock.millis());
        entity = cvRepository.save(entity);
        return cvMapper.toDTO(entity);
    }

    @Override
    public Page<CvDTO> getAllByAttendantIdAndByName(String attendantId, String cvName, Pageable pageable) {
        return cvRepository.findByNameLikeOrNameIsNullAndAttendantId('%' + cvName + '%', attendantId, pageable).map(cvMapper::toDTO);
    }

    @Override
    public Optional<CvDTO> getByIdAndAttendantId(String id, String attendantId) {
        return cvRepository.findByIdAndAttendantAccountId(id, attendantId).map(cvMapper::toDTO);
    }

    void updateWorkHistoryKeyWord(CvEntity cv) {
        Map<String, String> body = new HashedMap<>();
        cv.getWorkHistories().forEach(workHistoryEntity -> {
            if (workHistoryEntity.getDescription() == null) return;
            body.put("description", workHistoryEntity.getDescription());
            Mono<KeyWordResponse> descriptionResult = webClient.post().uri(skillProcessorURL + SkillExtractorApiEndpoint.EXTRACT_KEYWORD)
                    .body(Mono.just(body), Map.class)
                    .retrieve()
                    .bodyToMono(KeyWordResponse.class);
            descriptionResult.subscribe(keyWordResponse -> {
                try {
                    String parseResult = Jackson.getObjectMapper().writeValueAsString(keyWordResponse.result);
                    workHistoryEntity.setDescriptionKeyWord(parseResult);
                    cvWorkHistoryRepository.save(workHistoryEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    void updateEducationKeyWord(CvEntity cv) {
        Map<String, String> body = new HashedMap<>();
        cv.getEducations().forEach(educationEntity -> {
            if (educationEntity.getAchievement() == null) return;
            body.put("description", educationEntity.getAchievement());
            Mono<KeyWordResponse> descriptionResult = webClient.post().uri(skillProcessorURL + SkillExtractorApiEndpoint.EXTRACT_KEYWORD)
                    .body(Mono.just(body), Map.class)
                    .retrieve()
                    .bodyToMono(KeyWordResponse.class);
            descriptionResult.subscribe(keyWordResponse -> {
                try {
                    String parseResult = Jackson.getObjectMapper().writeValueAsString(keyWordResponse.result);
                    educationEntity.setAchievementKeyWord(parseResult);
                    cvEducationRepository.save(educationEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }
    void updateActivityKeyWord(CvEntity cv) {
        Map<String, String> body = new HashedMap<>();
        cv.getActivities().forEach(activityEntity -> {
            if (activityEntity.getDescription() == null) return;
            body.put("description", activityEntity.getDescription());
            Mono<KeyWordResponse> descriptionResult = webClient.post().uri(skillProcessorURL + SkillExtractorApiEndpoint.EXTRACT_KEYWORD)
                    .body(Mono.just(body), Map.class)
                    .retrieve()
                    .bodyToMono(KeyWordResponse.class);
            descriptionResult.subscribe(keyWordResponse -> {
                try {
                    String parseResult = Jackson.getObjectMapper().writeValueAsString(keyWordResponse.result);
                    activityEntity.setDescriptionKeyWord(parseResult);
                    cvActivityRepository.save(activityEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    @Transactional
    public CvDTO updateCV(CvDTO dto, String userId) {
        Optional<CvEntity> opt = cvRepository.findById(dto.getId());
        if (!opt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Cv.NOT_FOUND));
        }
        CvEntity cvEntity = opt.get();
        if (!cvEntity.getAttendant().getAccountId().equals(userId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Cv.NOT_FOUND));
        }
        cvMapper.updateCvEntityFromCvDTO(dto, cvEntity);
        cvEntity.setUpdateTime(clock.millis());


        cvEntity = cvRepository.save(cvEntity);
        updateWorkHistoryKeyWord(cvEntity);
        updateEducationKeyWord(cvEntity);
        updateActivityKeyWord(cvEntity);
        return cvMapper.toDTO(cvEntity);
    }

    @Override
    @Transactional
    public CvDTO updateProfilePicture(String pictureProfileFolder, String id) {
        String url = awsUtil.generateAwsS3AccessString(pictureProfileFolder, id);
        CvEntity cv = cvRepository.getById(id);
        cv.setProfileImageUrl(url);
        cv = cvRepository.save(cv);
        return cvMapper.toDTO(cv);
    }

    @Override
    @Transactional
    public CvDTO deleteCV(String cvId, String userId) {
        Optional<CvEntity> opt = cvRepository.findById(cvId);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Cv.NOT_FOUND));
        }
        CvEntity cvEntity = opt.get();
        if (!cvEntity.getAttendant().getAccountId().equals(userId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Cv.NOT_FOUND));
        }
        cvRepository.delete(cvEntity);
        return cvMapper.toDTO(cvEntity);

    }

}
