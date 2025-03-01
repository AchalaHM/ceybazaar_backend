package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.DeliveryRegionDTO;
import com.CeyBazaar.backend.dto.Response;

import java.util.LinkedList;
import java.util.List;

public interface DeliveryRegionService {
    Response<String> addNewDeliveryRegion(DeliveryRegionDTO deliveryRegionDTO);
    Response<List<DeliveryRegionDTO>> viewDeliveryRegion();
}
