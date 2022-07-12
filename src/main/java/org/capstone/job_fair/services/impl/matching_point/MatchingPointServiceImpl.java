package org.capstone.job_fair.services.impl.matching_point;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.SkillExtractorApiEndpoint;
import org.capstone.job_fair.controllers.payload.requests.matching_point.CalculateMatchingPointApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.matching_point.CalculateMatchingPointRequest;
import org.capstone.job_fair.models.entities.attendant.application.*;
import org.capstone.job_fair.models.entities.company.misc.SkillTagEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.BoothJobPositionEntity;
import org.capstone.job_fair.repositories.attendant.application.ApplicationRepository;
import org.capstone.job_fair.services.interfaces.matching_point.MatchingPointService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingPointServiceImpl implements MatchingPointService {
    @Autowired
    private WebClient webClient;

    @Value("${skill.processor.url}")
    private String skillProcessorURL;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    @Transactional
    public Mono<ApplicationEntity> calculateFromApplication(String applicationId) {
        return Mono.fromCallable(() -> {
            Optional<ApplicationEntity> entityOptional = applicationRepository.findById(applicationId);
            if (!entityOptional.isPresent()) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
            }
            ObjectMapper mapper = new ObjectMapper();
            ApplicationEntity application = entityOptional.get();

            //extract job position key words
            BoothJobPositionEntity jobPosition = application.getBoothJobPosition();
            TypeReference<List<String>> listStringType = new TypeReference<List<String>>() {
            };

            List<String> descriptionKeyWords = Collections.emptyList();
            try {
                descriptionKeyWords = mapper.readValue(jobPosition.getDescriptionKeyWord(), listStringType);
            } catch (JsonProcessingException | NullPointerException ignored) {
            }
            List<String> requirementKeyWords = Collections.emptyList();
            try {
                requirementKeyWords = mapper.readValue(jobPosition.getRequirementKeyWord(), listStringType);
            } catch (JsonProcessingException | NullPointerException ignored) {
            }
            List<String> jobSkills = Collections.emptyList();
            if (jobPosition.getSkillTagEntities() != null) {
                jobSkills = jobPosition.getSkillTagEntities().stream().map(SkillTagEntity::getName).collect(Collectors.toList());
            }
            List<String> otherKeyWords = new ArrayList<>();
            otherKeyWords.add(jobPosition.getLanguage().getName());
            if (jobPosition.getCategories() != null) {
                jobPosition.getCategories().forEach(subCategoryEntity -> {
                    otherKeyWords.add(subCategoryEntity.getName());
                });
            }


            //extract application keywords
            //get skill
            List<String> skillKeywords = Collections.emptyList();
            if (application.getSkills() != null) {
                skillKeywords = application.getSkills().stream().map(ApplicationSkillEntity::getName).collect(Collectors.toList());
            }

            //get education
            List<String> educationKeyWords = new ArrayList<>();
            if (application.getEducations() != null) {
                for (ApplicationEducationEntity educationEntity : application.getEducations()) {
                    try {
                        educationKeyWords.add(educationEntity.getSchool());
                        educationKeyWords.add(educationEntity.getSubject());
                        educationKeyWords.add(educationEntity.getQualificationId().name());
                        List<String> keyWords = mapper.readValue(educationEntity.getAchievementKeyWord(), listStringType);
                        educationKeyWords.addAll(keyWords);
                    } catch (JsonProcessingException | NullPointerException | IllegalArgumentException ignored) {
                    }
                }
            }

            //get work history
            List<String> workHistoriesKeyWords = new ArrayList<>();
            if (application.getWorkHistories() != null) {
                for (ApplicationWorkHistoryEntity workHistory : application.getWorkHistories()) {
                    try {
                        workHistoriesKeyWords.add(workHistory.getCompany());
                        workHistoriesKeyWords.add(workHistory.getPosition());
                        List<String> keyWords = mapper.readValue(workHistory.getDescriptionKeyWord(), listStringType);
                        workHistoriesKeyWords.addAll(keyWords);
                    } catch (JsonProcessingException | NullPointerException | IllegalArgumentException ignored) {
                    }

                }
            }

            //get certification
            List<String> certificationKeyWords = new ArrayList<>();
            if (application.getCertifications() != null) {
                for (ApplicationCertificationEntity certificationEntity : application.getCertifications()) {
                    certificationKeyWords.add(certificationEntity.getName());
                    certificationKeyWords.add(certificationEntity.getInstitution());
                }
            }

            //get activity
            List<String> activityKeyWords = new ArrayList<>();
            if (application.getActivities() != null) {
                for (ApplicationActivityEntity activityEntity : application.getActivities()) {
                    try {
                        activityKeyWords.add(activityEntity.getName());
                        activityKeyWords.add(activityEntity.getFunctionTitle());
                        activityKeyWords.add(activityEntity.getOrganization());
                        List<String> keyWords = mapper.readValue(activityEntity.getDescriptionKeyWord(), listStringType);
                        activityKeyWords.addAll(keyWords);
                    } catch (JsonProcessingException | NullPointerException | IllegalArgumentException ignored) {
                    }

                }

            }


            CalculateMatchingPointApplicationRequest request = CalculateMatchingPointApplicationRequest.builder()
                    .applicationId(applicationId)
                    .requirementKeyWords(requirementKeyWords)
                    .descriptionKeyWords(descriptionKeyWords)
                    .jobSkills(jobSkills)
                    .otherRequireKeywords(otherKeyWords)
                    .attendantSkills(skillKeywords)
                    .attendantEducationKeyWords(educationKeyWords)
                    .attendantWorkHistoryKeyWords(workHistoriesKeyWords)
                    .attendantCertificationKeyWords(certificationKeyWords)
                    .attendantActivityKeyWords(activityKeyWords)
                    .build();


            Mono<Map<String, Double>> response = webClient.post().uri(skillProcessorURL + SkillExtractorApiEndpoint.MATCHING_POINT_APPLICATION)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(request), CalculateMatchingPointRequest.class)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Double>>() {
                    });

            Map<String, Double> responseBody = response.block();
            double result = responseBody.get("result");
            application.setMatchingPoint(result);
            applicationRepository.save(application);
            return application;
        });
    }

    @Override
    public Mono<Void> calculateBetweenCVAndBoothJobPosition(String cvId, String jobPositionId) {
        return null;
    }

    @Override
    public Mono<Void> calculateBetweenProfileAndBoothJobPosition(String attendantId, String jobPositonId) {
        return null;
    }
}
