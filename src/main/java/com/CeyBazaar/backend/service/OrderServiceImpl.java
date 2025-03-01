package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.OrderDTO;
import com.CeyBazaar.backend.dto.OrderItemDTO;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.entity.Order;
import com.CeyBazaar.backend.entity.OrderDetails;
import com.CeyBazaar.backend.entity.OrderItem;
import com.CeyBazaar.backend.repository.OrderDetailsRepository;
import com.CeyBazaar.backend.repository.OrderItemRepository;
import com.CeyBazaar.backend.repository.OrderRepository;
import com.CeyBazaar.backend.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService{
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Transactional // Ensures atomicity: all operations succeed or rollback
    @Override
    public Response<String> addNewOrder(OrderDTO orderDTO) {

        String OID;
        do {
            OID = generateRandomID(); // Generate a new order ID
        } while (orderRepository.existsByOrderId(OID)); // Fixed method name

        try {
            // Create Order entity
            Order order = new Order();
            order.setOrderId(OID);
            order.setStatus(Constants.UNPAID);
            order.setTotalCost(orderDTO.getTotalCost());

            // Save order first (important for foreign key relation)
            order = orderRepository.save(order);

            // Create and save Order Items
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order); // Set the parent order
                orderItem.setItemId(itemDTO.getItemId());
                orderItem.setQuantity(itemDTO.getQuantity());
                orderItems.add(orderItem);
            }
            orderItemRepository.saveAll(orderItems); // Batch insert order items

            // Create and save Order Details
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order); // Set the parent order
            orderDetails.setCustomerName(orderDTO.getCustomerName());
            orderDetails.setCustomerEmail(orderDTO.getCustomerEmail());
            orderDetails.setAddress(orderDTO.getAddress());
            orderDetails.setRegion(orderDTO.getRegion());
            orderDetails.setCardName(orderDTO.getCardName());
            orderDetails.setCardNumber(orderDTO.getCardNumber());

            orderDetailsRepository.save(orderDetails);
            logger.info("Order placed successfully with order ID : " + OID);
            return new Response<>(Constants.SUCCESS, "Order placed successfully", OID);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException("Order creation failed", ex);
            // Triggers rollback
        }

    }
    public static String generateRandomID() {
        Random random = new Random();
        int randomNumber = random.nextInt(1_000_000); // Generates a number between 0 and 999999
        return String.format("D%06d", randomNumber); // Adds 'D' prefix and ensures 6-digit format
    }


}
