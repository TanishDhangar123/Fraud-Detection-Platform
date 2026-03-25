package com.tanish.frauddetectionassignment.controller;

import com.tanish.frauddetectionassignment.model.Transaction;
import com.tanish.frauddetectionassignment.service.TransactionDynamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionDynamoService dynamoService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return dynamoService.findAll();
    }

    @GetMapping("/fraud")
    public List<Transaction> getFraudTransactions() {
        return dynamoService.findAll()
                .stream()
                .filter(Transaction::isFlagged)
                .toList();
    }
}
