package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.AdminOrderDTO;
import com.CeyBazaar.backend.dto.AdminReportDTO;
import com.CeyBazaar.backend.dto.Response;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {
    Response<List<AdminOrderDTO>> getAllOrders();
    Response<List<AdminOrderDTO>> getPaidOrders();
    Response<String> completeOrder(String orderId);
    Response<String> updateDeliveryStatus(String orderId, String deliveryStatus);
    Response<AdminReportDTO> getDailyReport(LocalDate date);
    Response<List<AdminReportDTO>> getDateRangeReport(LocalDate startDate, LocalDate endDate);
}