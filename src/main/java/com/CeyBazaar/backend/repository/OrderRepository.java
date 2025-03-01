package com.CeyBazaar.backend.repository;

import com.CeyBazaar.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order , Integer> {
    boolean existsByOrderId(String orderId);
}
