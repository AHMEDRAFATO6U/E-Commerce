package com.boot_demo1.ecommerce.Service;

import com.boot_demo1.ecommerce.Payload.OrderDto;
import jakarta.transaction.Transactional;

public interface OrderService {
    @Transactional
    OrderDto placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}

