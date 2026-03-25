package com.tanish.frauddetectionassignment.service;

import com.tanish.frauddetectionassignment.model.FlaggedTransaction;
import com.tanish.frauddetectionassignment.model.Transaction;
import com.tanish.frauddetectionassignment.rules.FraudRules;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FraudService {

    private final TransactionDynamoService dynamoService;
    private final AuditS3Service auditS3Service;

    public FlaggedTransaction analyze(Transaction transaction) {
        log.debug("Running fraud check on transaction : {}", transaction);

        List<String> reasons = FraudRules.checkTransaction(transaction);

        boolean isFraud = !reasons.isEmpty();
        transaction.setFlagged(isFraud);

        FlaggedTransaction flaggedTx =
                new FlaggedTransaction(transaction, reasons.isEmpty()
                        ? Collections.emptyList()
                        : reasons);

        try {
            dynamoService.save(transaction);
            log.info("Saved {} into DynamoDB", transaction.getId());
        } catch (Exception e) {
            log.error("Failed to save transaction {} to DynamoDB", transaction.getId(), e);
        }

        if (isFraud) {
            try {
                auditS3Service.upload(transaction);
                log.warn("Uploaded flagged transaction {} to S3", transaction.getId());
            } catch (Exception e) {
                log.error("Failed to upload flagged transaction {} to S3", transaction.getId(), e);
            }
        }

        if (!isFraud) {
            log.info("Transaction {} passed all checks", transaction.getId());
        } else {
            log.warn("Transaction {} is flagged for reasons {}", transaction.getId(), reasons);
        }

        return flaggedTx;
    }
}
