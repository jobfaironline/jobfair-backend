package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.CountryDTO;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.repositories.attendant.CountryRepository;
import org.capstone.job_fair.services.interfaces.attendant.CountryService;
import org.capstone.job_fair.services.mappers.attendant.CountryMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;




    @Override
    public Integer getCountCountryById(int id) {
        return countryRepository.countById(id);
    }

    @Autowired
    CountryMapper countryMapper;

    @Override
    public List<CountryDTO> getAllCountries(){
        List<CountryEntity> countryEntities = countryRepository.findAll();
        return countryEntities.stream().map(countryMapper::toDTO).collect(Collectors.toList());


    }

    @Override
    public Optional<CountryDTO> findById(int id) {
        return countryRepository.findById(id).map(countryEntity -> countryMapper.toDTO(countryEntity));
    }

    @Override
    public List<CountryDTO> getAll() {
        return countryRepository.findAll().stream().map(countryEntity -> countryMapper.toDTO(countryEntity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CountryDTO delete(int id) {
        Optional<CountryEntity> entityOptional = countryRepository.findById(id);
        if(!entityOptional.isPresent()) throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND_COUNTRY));
        countryRepository.deleteById(id);
        return countryMapper.toDTO(entityOptional.get());
    }

    @Override
    @Transactional
    public CountryDTO create(CountryDTO dto) {
        CountryEntity entity = countryRepository.save(countryMapper.toEntity(dto));
        return countryMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public CountryDTO update(CountryDTO dto) {
        Optional<CountryEntity> entityOptional = countryRepository.findById(dto.getId());
        if(!entityOptional.isPresent()) throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND_COUNTRY));
        CountryEntity entity = countryRepository.save(countryMapper.toEntity(dto));
        return countryMapper.toDTO(entity);
    }
}
