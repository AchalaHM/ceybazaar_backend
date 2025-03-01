package com.CeyBazaar.backend.controller;

import com.CeyBazaar.backend.dto.OrderDTO;
import com.CeyBazaar.backend.dto.ProductCatDTO;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/NewOrder")
    public ResponseEntity<Response<String>> addNewOrder(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok(orderService.addNewOrder(orderDTO));
    }
}
