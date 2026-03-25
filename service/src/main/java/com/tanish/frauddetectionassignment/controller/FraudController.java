package com.tanish.frauddetectionassignment.controller;

import com.tanish.frauddetectionassignment.model.Transaction;
import com.tanish.frauddetectionassignment.model.FlaggedTransaction;
import com.tanish.frauddetectionassignment.service.FraudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fraud")

public class FraudController {

    private final FraudService fraudService;

    @GetMapping("/debug")
    public Object debug(Authentication authentication) {
        return authentication;
    }

    @PostMapping("/check")
    public FlaggedTransaction checkFraud(@Valid @RequestBody Transaction transaction) {

        log.info("Recieved transaction : {}",transaction);

        FlaggedTransaction response= fraudService.analyze(transaction);

        if(response.getReasons().isEmpty()){
            log.info("Transaction approved : {}",transaction.getId());
        }
        else{
            log.warn("Fraud detected : {} for reasons {}", transaction.getId(), response.getReasons());
        }

        return response;
    }
}
