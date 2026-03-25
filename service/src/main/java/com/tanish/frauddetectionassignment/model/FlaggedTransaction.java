package com.tanish.frauddetectionassignment.model;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FlaggedTransaction {

    private String id;
    private String accountId;
    private double amount;
    private String location;
    private String timestamp;
    private List<String> reasons;

    public FlaggedTransaction(Transaction t, List<String> reasons) {
        this.id = t.getId();
        this.accountId = t.getAccountId();
        this.amount = t.getAmount();
        this.location = t.getLocation();
        this.timestamp = t.getTimestamp();
        this.reasons = reasons;
    }
}
