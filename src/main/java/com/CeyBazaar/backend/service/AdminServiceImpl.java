package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.AdminOrderDTO;
import com.CeyBazaar.backend.dto.AdminReportDTO;
import com.CeyBazaar.backend.dto.OrderItemDTO;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.entity.Order;
import com.CeyBazaar.backend.entity.OrderItem;
import com.CeyBazaar.backend.repository.OrderRepository;
import com.CeyBazaar.backend.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Response<List<AdminOrderDTO>> getAllOrders() {
        try {
            logger.info("Fetching all orders");
            List<Order> orders = orderRepository.findAllByOrderByAddedOnDesc();
            logger.info("Found {} orders", orders.size());
            List<AdminOrderDTO> adminOrderDTOs = orders.stream()
                    .map(this::convertToAdminOrderDTO)
                    .collect(Collectors.toList());
            logger.info("Successfully converted {} orders to DTOs", adminOrderDTOs.size());
            return new Response<>(Constants.SUCCESS, "Orders retrieved successfully", adminOrderDTOs);
        } catch (Exception e) {
            logger.error("Error retrieving orders: {}", e.getMessage(), e);
            return new Response<>(Constants.RUNTIME_EXCEPTION, "Error retrieving orders: " + e.getMessage(), null);
        }
    }

    @Override
    public Response<List<AdminOrderDTO>> getPaidOrders() {
        try {
            logger.info("Fetching paid orders");
            List<Order> orders = orderRepository.findByStatusOrderByAddedOnDesc(Constants.PAID);
            logger.info("Found {} paid orders", orders.size());
            List<AdminOrderDTO> adminOrderDTOs = orders.stream()
                    .map(this::convertToAdminOrderDTO)
                    .collect(Collectors.toList());
            return new Response<>(Constants.SUCCESS, "Paid orders retrieved successfully", adminOrderDTOs);
        } catch (Exception e) {
            logger.error("Error retrieving paid orders: {}", e.getMessage(), e);
            return new Response<>(Constants.RUNTIME_EXCEPTION, "Error retrieving paid orders: " + e.getMessage(), null);
        }
    }

    @Override
    public Response<String> updateDeliveryStatus(String orderId, String deliveryStatus) {
        try {
            Order order = orderRepository.findByOrderId(orderId);
            if (order == null) {
                return new Response<>(Constants.NOT_FOUND, "Order not found", null);
            }
            if (!Constants.PAID.equals(order.getStatus())) {
                return new Response<>(Constants.RUNTIME_EXCEPTION, "Order must be paid to update delivery status", null);
            }
            order.setDeliveryStatus(deliveryStatus);
            order.setUpdatedOn(LocalDate.now());
            orderRepository.save(order);
            return new Response<>(Constants.SUCCESS, "Delivery status updated successfully", "Delivery status updated to " + deliveryStatus);
        } catch (Exception e) {
            return new Response<>(Constants.RUNTIME_EXCEPTION, "Error updating delivery status", null);
        }
    }

    @Override
    public Response<String> completeOrder(String orderId) {
        try {
            Order order = orderRepository.findByOrderId(orderId);
            if (order == null) {
                return new Response<>(Constants.NOT_FOUND, "Order not found", null);
            }
            if (!Constants.PAID.equals(order.getStatus())) {
                return new Response<>(Constants.RUNTIME_EXCEPTION, "Order must be paid before completion", null);
            }
            order.setStatus(Constants.COMPLETED);
            order.setDeliveryStatus(Constants.DELIVERY_DELIVERED);
            order.setUpdatedOn(LocalDate.now());
            order.setTerminatedOn(LocalDate.now());
            orderRepository.save(order);
            return new Response<>(Constants.SUCCESS, "Order completed successfully", "Order " + orderId + " completed");
        } catch (Exception e) {
            return new Response<>(Constants.RUNTIME_EXCEPTION, "Error completing order", null);
        }
    }

    @Override
    public Response<AdminReportDTO> getDailyReport(LocalDate date) {
        try {
            AdminReportDTO report = new AdminReportDTO();
            report.setDate(date);
            report.setTotalOrders(orderRepository.countOrdersByDate(date));
            report.setPaidOrders(orderRepository.countPaidOrdersByDate(date));
            report.setCompletedOrders(orderRepository.countCompletedOrdersByDate(date));
            report.setTotalRevenue(orderRepository.sumRevenueByDate(date));
            report.setTotalShippingRevenue(orderRepository.sumShippingRevenueByDate(date));
            return new Response<>(Constants.SUCCESS, "Daily report generated successfully", report);
        } catch (Exception e) {
            return new Response<>(Constants.RUNTIME_EXCEPTION, "Error generating daily report", null);
        }
    }

    @Override
    public Response<List<AdminReportDTO>> getDateRangeReport(LocalDate startDate, LocalDate endDate) {
        try {
            List<AdminReportDTO> reports = new ArrayList<>();
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                AdminReportDTO report = new AdminReportDTO();
                report.setDate(currentDate);
                report.setTotalOrders(orderRepository.countOrdersByDate(currentDate));
                report.setPaidOrders(orderRepository.countPaidOrdersByDate(currentDate));
                report.setCompletedOrders(orderRepository.countCompletedOrdersByDate(currentDate));
                report.setTotalRevenue(orderRepository.sumRevenueByDate(currentDate));
                report.setTotalShippingRevenue(orderRepository.sumShippingRevenueByDate(currentDate));
                reports.add(report);
                currentDate = currentDate.plusDays(1);
            }
            return new Response<>(Constants.SUCCESS, "Date range report generated successfully", reports);
        } catch (Exception e) {
            return new Response<>(Constants.RUNTIME_EXCEPTION, "Error generating date range report", null);
        }
    }

    private AdminOrderDTO convertToAdminOrderDTO(Order order) {
        AdminOrderDTO dto = new AdminOrderDTO();
        dto.setId(order.getId());
        dto.setOrderId(order.getOrderId());
        dto.setStatus(order.getStatus());
        dto.setDeliveryStatus(order.getDeliveryStatus());
        dto.setTotalCost(order.getTotalCost());
        dto.setShippingCost(order.getShippingCost());
        dto.setAddedOn(order.getAddedOn());
        dto.setUpdatedOn(order.getUpdatedOn());
        dto.setTerminatedOn(order.getTerminatedOn());
        
        if (order.getOrderDetails() != null) {
            dto.setCustomerName(order.getOrderDetails().getCustomerName());
            dto.setCustomerEmail(order.getOrderDetails().getCustomerEmail());
            dto.setAddress(order.getOrderDetails().getAddress());
            dto.setRegion(order.getOrderDetails().getRegion());
        }
        
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(this::convertToOrderItemDTO)
                .collect(Collectors.toList());
        dto.setOrderItems(orderItemDTOs);
        
        return dto;
    }

    private OrderItemDTO convertToOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        
        if (orderItem.getProduct() != null) {
            dto.setProductId(orderItem.getProduct().getId());
            dto.setProductName(orderItem.getProduct().getProductName());
        } else {
            dto.setProductId(orderItem.getItemId());
            dto.setProductName("Product not found");
        }
        
        return dto;
    }
}