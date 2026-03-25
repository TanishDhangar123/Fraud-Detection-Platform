package com.tanish.frauddetectionassignment.service;

import com.tanish.frauddetectionassignment.model.FraudSummary;
import com.tanish.frauddetectionassignment.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDynamoService {

    private final DynamoDbClient dynamoDbClient;

    private static final String TABLE_NAME = "pari-fraudDetectionAssignment-transactions";

    public void save(Transaction tx){

        Map<String, AttributeValue> item= new HashMap<>();

        item.put("accountId",AttributeValue.builder()
                .s(tx.getAccountId())
                .build());

        item.put("id",AttributeValue.builder()
                .s(tx.getId())
                .build());

        item.put("amount", AttributeValue.builder()
                .n(String.valueOf(tx.getAmount()))
                .build());

        item.put("location", AttributeValue.builder()
                .s(tx.getLocation())
                .build());

        item.put("timestamp", AttributeValue.builder()
                .s(tx.getTimestamp())
                .build());

        item.put("flagged", AttributeValue.builder()
                .bool(tx.isFlagged())
                .build());

        PutItemRequest request= PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }

    public List<Transaction> findAll() {

        ScanRequest request = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build();

        ScanResponse response = dynamoDbClient.scan(request);

        return response.items().stream().map(item -> {
            Transaction tx = new Transaction();
            tx.setId(item.get("id").s());
            tx.setAccountId(item.get("accountId").s());
            tx.setAmount(Double.parseDouble(item.get("amount").n()));
            tx.setLocation(item.get("location").s());
            tx.setTimestamp(item.get("timestamp").s());
            tx.setFlagged(item.get("flagged").bool());
            return tx;
        }).toList();
    }

    public List<FraudSummary> getFraudSummary() {

        ScanRequest request = ScanRequest.builder()
                .tableName("pari-fraudDetectionAssignment-summary")
                .build();

        ScanResponse response = dynamoDbClient.scan(request);

        return response.items().stream().map(item ->
                new FraudSummary(
                        item.get("accountId").s(),
                        item.get("region").s(),
                        Integer.parseInt(item.get("fraudCount").n()),
                        Double.parseDouble(item.get("totalFraudAmount").n())
                )
        ).toList();
    }
}
