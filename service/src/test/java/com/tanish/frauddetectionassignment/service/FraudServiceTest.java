package com.tanish.frauddetectionassignment.service;

import com.tanish.frauddetectionassignment.model.FlaggedTransaction;
import com.tanish.frauddetectionassignment.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FraudServiceTest {

@Mock
    private TransactionDynamoService transactionDynamoService;
@Mock
    private AuditS3Service auditS3Service;

@InjectMocks
    private FraudService fraudService;

@Test
    void analyze_ShouldFlagFraudulentTransaction() throws Exception {

    //Arrange
    Transaction tx = Transaction.builder()
            .id("T1")
            .accountId("A1")
            .amount(3)
            .location("IN")
            .timestamp("2023-10-01T10:00:00Z")
            .build();

    //Act
    FlaggedTransaction result = fraudService.analyze(tx);

    //Assert
    assertFalse(result.getReasons().isEmpty());

    verify(transactionDynamoService).save(tx);
    verify(auditS3Service).upload(tx);

}

@Test
    void analyze_ShouldNotFlagLegitimateTransaction() throws Exception {

    //Arrange
    Transaction tx = Transaction.builder()
            .id("T2")
            .accountId("A2")
            .amount(50)
            .location("IN")
            .timestamp("2023-10-01T10:00:00Z")
            .build();

    //Act
    FlaggedTransaction result = fraudService.analyze(tx);

    //Assert
    assertTrue(result.getReasons().isEmpty());

    verify(transactionDynamoService).save(tx);
    verify(auditS3Service,never()).upload(any());

}

@Test
    void analyze_ShouldHandleDynamoDBSaveFailureGracefully() throws Exception {

    //Arrange
    Transaction tx = Transaction.builder()
            .id("T3")
            .accountId("A3")
            .amount(300)
            .location("IN")
            .timestamp("2023-10-01T10:00:00Z")
            .build();

    //Simulate DynamoDB save failure
    doThrow(new RuntimeException("DynamoDB error")).when(transactionDynamoService).save(tx);

    assertDoesNotThrow(() -> fraudService.analyze(tx));
}
}
