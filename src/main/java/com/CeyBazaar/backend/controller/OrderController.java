package com.CeyBazaar.backend.controller;

import com.CeyBazaar.backend.dto.*;
import com.CeyBazaar.backend.service.OrderService;
import com.CeyBazaar.backend.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/Orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/NewOrder")
    public ResponseEntity<Response<PaymentDTO>> addNewOrder(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok(orderService.addNewOrder(orderDTO));
    }

    @PostMapping("/notify")
    public ResponseEntity<Response<String>> handlePayHereNotification(HttpServletRequest request) {
        System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
        String status = request.getParameter("status_code"); // or use a PaymentNotificationDTO

        if ("2".equals(status)) {
            String orderId = request.getParameter("order_id");
            return ResponseEntity.ok(orderService.updateOrder(orderId));

        } else {
            return ResponseEntity.ok(new Response<>(Constants.RUNTIME_EXCEPTION , null , null));
        }
    }

    @PostMapping("check-status")
    public ResponseEntity<Response<PaymentStatusDTO>> getPaymentStatus(@RequestBody Map<String, String> request){
        String orderId  = String.valueOf(request.get("orderId"));
        return ResponseEntity.ok(orderService.getPaymentStatus(orderId));
    }
}
