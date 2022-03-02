package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.CompanyBoothDTO;
import org.capstone.job_fair.models.dtos.company.OrderDTO;
import org.capstone.job_fair.models.entities.company.CompanyBoothEntity;
import org.capstone.job_fair.models.entities.company.CompanyRegistrationEntity;
import org.capstone.job_fair.models.entities.company.OrderEntity;
import org.capstone.job_fair.models.entities.job_fair.BoothEntity;
import org.capstone.job_fair.repositories.company.CompanyBoothRepository;
import org.capstone.job_fair.repositories.company.CompanyRegistrationRepository;
import org.capstone.job_fair.repositories.company.OrderRepository;
import org.capstone.job_fair.repositories.job_fair.BoothRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothPurchaseService;
import org.capstone.job_fair.services.interfaces.company.OrderService;
import org.capstone.job_fair.services.mappers.company.CompanyBoothMapper;
import org.capstone.job_fair.services.mappers.company.OrderMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CompanyBoothPurchaseServiceImpl implements CompanyBoothPurchaseService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BoothRepository boothRepository;

    @Autowired
    private CompanyBoothRepository companyBoothRepository;

    @Autowired
    private CompanyBoothMapper companyBoothMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Override
    @Transactional
    public CompanyBoothDTO purchaseBooth(String companyRegistrationId, String boothId) {
        Optional<BoothEntity> boothOpt = boothRepository.findById(boothId);
        if (!boothOpt.isPresent()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Booth.NOT_FOUND));
        }

        Optional<CompanyRegistrationEntity> companyRegistrationOpt = companyRegistrationRepository.findById(companyRegistrationId);
        if (!companyRegistrationOpt.isPresent()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.NOT_FOUND));
        }

        List<CompanyBoothEntity> companyBoothEntities = companyBoothRepository.getCompanyBoothByJobFairIdAndCompanyId(companyRegistrationOpt.get().getJobFairId(), companyRegistrationOpt.get().getCompanyId());
        if (companyBoothEntities.size() > 0){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.BoothPurchase.ALREADY_HAS_BOOTH));
        }


        BoothEntity boothEntity = boothOpt.get();

        OrderDTO orderDTO = orderService.createOrder(companyRegistrationId);
        OrderEntity orderEntity = orderRepository.getById(orderDTO.getId());
        CompanyBoothEntity companyBoothEntity = new CompanyBoothEntity();


        companyBoothEntity.setPrice(0.0);
        companyBoothEntity.setOrder(orderEntity);
        companyBoothEntity.setBooth(boothEntity);

        companyBoothRepository.save(companyBoothEntity);
        orderService.approveOrder(orderEntity.getId());
        return companyBoothMapper.toDTO(companyBoothEntity);
    }
}
