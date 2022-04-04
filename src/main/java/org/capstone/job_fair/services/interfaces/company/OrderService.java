package org.capstone.job_fair.services.interfaces.company;

public interface OrderService {
    OrderDTO createOrder(String companyRegistrationId);
    OrderDTO approveOrder(String orderId);
}
