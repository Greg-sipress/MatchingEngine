package org.example.matchingengine.controller;

import org.example.matchingengine.service.OrderGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order-generator")
public class OrderGeneratorController {

    private final OrderGenerator orderGenerator;

    @Autowired
    public OrderGeneratorController(OrderGenerator orderGenerator) {
        this.orderGenerator = orderGenerator;
    }

    @PostMapping("/begin")
    public String beginOrderGeneration() {
        orderGenerator.begin(); // Calls the begin() method in OrderGenerator
        return "Order generation started!";
    }
}