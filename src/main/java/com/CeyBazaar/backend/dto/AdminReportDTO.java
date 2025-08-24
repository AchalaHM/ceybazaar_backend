package com.CeyBazaar.backend.dto;

import java.time.LocalDate;

public class AdminReportDTO {
    private LocalDate date;
    private long totalOrders;
    private long paidOrders;
    private long completedOrders;
    private double totalRevenue;
    private double totalShippingRevenue;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getPaidOrders() {
        return paidOrders;
    }

    public void setPaidOrders(long paidOrders) {
        this.paidOrders = paidOrders;
    }

    public long getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(long completedOrders) {
        this.completedOrders = completedOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTotalShippingRevenue() {
        return totalShippingRevenue;
    }

    public void setTotalShippingRevenue(double totalShippingRevenue) {
        this.totalShippingRevenue = totalShippingRevenue;
    }
}