package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.statuses.OrderStatus;
import org.capstone.job_fair.repositories.company.OrderRepository;
import org.capstone.job_fair.services.interfaces.company.OrderService;
import org.capstone.job_fair.services.mappers.company.OrderMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO createOrder(String companyRegistrationId) {
        OrderEntity orderEntity = new OrderEntity();
        String id = UUID.randomUUID().toString();
        Long createDate = new Date().getTime();
        CompanyRegistrationEntity companyRegistrationEntity = new CompanyRegistrationEntity();
        companyRegistrationEntity.setId(companyRegistrationId);

        orderEntity.setId(id);
        orderEntity.setCreateDate(createDate);
        orderEntity.setCompanyRegistration(companyRegistrationEntity);
        orderEntity.setStatus(OrderStatus.PENDING);
        orderEntity.setTotal(0.0);

        orderRepository.save(orderEntity);
        return orderMapper.toDTO(orderEntity);

    }

    @Override
    @Transactional
    public OrderDTO approveOrder(String orderId) {
        Optional<OrderEntity> orderEntityOpt = orderRepository.findById(orderId);
        if (!orderEntityOpt.isPresent()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Order.NOT_FOUND));
        }
        OrderEntity entity = orderEntityOpt.get();
        entity.setStatus(OrderStatus.APPROVE);
        orderRepository.save(entity);
        return orderMapper.toDTO(entity);
    }
}
