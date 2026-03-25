package com.tanish.frauddetectionassignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FraudSummary {

    private String accountId;
    private String region;
    private int fraudCount;
    private double totalFraudAmount;
}
