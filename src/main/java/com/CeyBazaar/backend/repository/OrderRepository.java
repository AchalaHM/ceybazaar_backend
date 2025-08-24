package com.CeyBazaar.backend.repository;

import com.CeyBazaar.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order , Integer> {
    boolean existsByOrderId(String orderId);

    @Query("SELECT o FROM orders o LEFT JOIN FETCH o.orderDetails LEFT JOIN FETCH o.orderItems WHERE o.orderId = :orderId")
    Order findByOrderId(@Param("orderId") String orderId);

    List<Order> findByStatusOrderByAddedOnDesc(String status);
    
    List<Order> findAllByOrderByAddedOnDesc();

    @Query("SELECT COUNT(o) FROM orders o WHERE o.addedOn = :date")
    long countOrdersByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(o) FROM orders o WHERE o.status = 'PAID' AND o.addedOn = :date")
    long countPaidOrdersByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(o) FROM orders o WHERE o.status = 'COMPLETED' AND o.addedOn = :date")
    long countCompletedOrdersByDate(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(o.totalCost), 0) FROM orders o WHERE (o.status = 'PAID' OR o.status = 'COMPLETED') AND o.addedOn = :date")
    double sumRevenueByDate(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(o.shippingCost), 0) FROM orders o WHERE (o.status = 'PAID' OR o.status = 'COMPLETED') AND o.addedOn = :date")
    double sumShippingRevenueByDate(@Param("date") LocalDate date);
}
