package com.tanish.frauddetectionassignment.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class Transaction {

    @NotBlank(message="Transaction ID cannot be empty")
    private String id;

    @NotBlank(message = "Account ID cannot be empty")
    private String accountId;

    @Positive(message = "Amount must be greater than 0")
    private double amount;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Timestamp is required")
    private String timestamp;

    private boolean flagged;
}
