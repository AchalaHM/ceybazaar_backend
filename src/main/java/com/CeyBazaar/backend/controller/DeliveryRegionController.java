package com.CeyBazaar.backend.controller;

import com.CeyBazaar.backend.dto.DeliveryRegionDTO;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.service.DeliveryRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Region")
public class DeliveryRegionController {

    @Autowired
    private DeliveryRegionService deliveryRegionService;

    @PostMapping("/NewDeliveryRegion")
    public ResponseEntity<Response<String>> addNewRgion(@RequestBody DeliveryRegionDTO deliveryRegionDTO){
        return ResponseEntity.ok(deliveryRegionService.addNewDeliveryRegion(deliveryRegionDTO));
    } // done frontend

    @GetMapping("/ViewDeliveryRegions")
    public ResponseEntity<Response<List<DeliveryRegionDTO>>> viewDeliveryRegion(){
        return ResponseEntity.ok(deliveryRegionService.viewDeliveryRegion());
    } //done frontend
}
