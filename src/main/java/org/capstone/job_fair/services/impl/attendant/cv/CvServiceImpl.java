package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvCertificationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.repositories.attendant.cv.CvRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.mappers.attendant.cv.CvMapper;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        entity.getActivities().forEach(activity -> activity.setCv(entity));
        entity.getCertifications().forEach(certification -> {
            validateCertification(certification);
            certification.setCv(entity);
        });
        entity.getEducations().forEach(education -> education.setCv(entity));
        entity.getReferences().forEach(reference -> reference.setCv(entity));
        entity.getWorkHistories().forEach(history -> history.setCv(entity));
        entity.getSkills().forEach(skill -> skill.setCv(entity));

        CvEntity newEntity = cvRepository.save(cvMapper.toEntity(dto));
        return cvMapper.toDTO(newEntity);
    }

    @Override
    public List<CvDTO> getAllByAttendantId(String attendantId) {
        return cvRepository.findByAttendantAccountId(attendantId).stream().map(cvMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CvDTO> getByIdAndAttendantId(String id, String attendantId) {
        return cvRepository.findByIdAndAttendantAccountId(id, attendantId).map(cvMapper::toDTO);
    }

    @Override
    @Transactional
    public CvDTO updateCV(CvDTO dto, String userId) {
        Optional<CvEntity> opt = cvRepository.findById(dto.getId());
        if (!opt.isPresent()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Cv.NOT_FOUND));
        }
        CvEntity cvEntity = opt.get();
        if (!cvEntity.getAttendant().getAccountId().equals(userId)){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Cv.NOT_FOUND));
        }
        cvMapper.updateCvEntityFromCvDTO(dto, cvEntity);
        cvEntity = cvRepository.save(cvEntity);
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

}
