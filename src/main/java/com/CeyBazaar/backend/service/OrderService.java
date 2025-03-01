package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.OrderDTO;
import com.CeyBazaar.backend.dto.Response;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    @Transactional
        // Ensures atomicity: all operations succeed or rollback
    Response<String> addNewOrder(OrderDTO orderDTO);
}
