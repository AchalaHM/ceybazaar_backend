package com.CeyBazaar.backend.controller;

import com.CeyBazaar.backend.dto.AdminOrderDTO;
import com.CeyBazaar.backend.dto.AdminReportDTO;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/orders/all")
    public ResponseEntity<Response<List<AdminOrderDTO>>> getAllOrders() {
        return ResponseEntity.ok(adminService.getAllOrders());
    }

    @GetMapping("/orders/paid")
    public ResponseEntity<Response<List<AdminOrderDTO>>> getPaidOrders() {
        return ResponseEntity.ok(adminService.getPaidOrders());
    }

    @PostMapping("/orders/complete")
    public ResponseEntity<Response<String>> completeOrder(@RequestBody Map<String, String> request) {
        String orderId = request.get("orderId");
        return ResponseEntity.ok(adminService.completeOrder(orderId));
    }

    @PostMapping("/orders/delivery-status")
    public ResponseEntity<Response<String>> updateDeliveryStatus(@RequestBody Map<String, String> request) {
        String orderId = request.get("orderId");
        String deliveryStatus = request.get("deliveryStatus");
        return ResponseEntity.ok(adminService.updateDeliveryStatus(orderId, deliveryStatus));
    }

    @GetMapping("/reports/daily")
    public ResponseEntity<Response<AdminReportDTO>> getDailyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(adminService.getDailyReport(date));
    }

    @GetMapping("/reports/range")
    public ResponseEntity<Response<List<AdminReportDTO>>> getDateRangeReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(adminService.getDateRangeReport(startDate, endDate));
    }
}