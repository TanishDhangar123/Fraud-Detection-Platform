package com.tanish.frauddetectionassignment.controller;

import com.tanish.frauddetectionassignment.model.FraudSummary;
import com.tanish.frauddetectionassignment.service.TransactionDynamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fraud/summary")
@RequiredArgsConstructor
public class FraudSummaryController {

    private final TransactionDynamoService dynamoService;

    @GetMapping
    public List<FraudSummary> getSummary() {
        return dynamoService.getFraudSummary();
    }
}
