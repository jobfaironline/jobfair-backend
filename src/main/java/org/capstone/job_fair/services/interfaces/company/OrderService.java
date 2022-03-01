package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.OrderDTO;

public interface OrderService {
    OrderDTO createOrder(String companyRegistrationId);
    OrderDTO approveOrder(String orderId);
}
