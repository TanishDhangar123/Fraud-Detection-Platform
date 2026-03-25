package com.tanish.frauddetectionassignment.service;

import com.tanish.frauddetectionassignment.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileWriter;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditS3Service {

    private final S3Client s3Client;

    private static final String BUCKET = "pari-frauddetectionassignment-audit";

    public void upload(Transaction tx) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(tx);

        File temp = File.createTempFile("fraud-", ".json");
        try (FileWriter writer = new FileWriter(temp)) {
            writer.write(json);
        }

        String key = "fraud/" + tx.getId() + ".json";

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(key)
                .contentType("application/json")
                .build();

        s3Client.putObject(request, temp.toPath());
        temp.delete();

        log.info("Uploaded flagged transaction {} to S3 bucket {}", tx.getId(), BUCKET);
    }

    public void uploadInvalid(String rawJson) {

        try {
            File temp = File.createTempFile("invalid-", ".json");
            try (FileWriter writer = new FileWriter(temp)) {
                writer.write(rawJson);
            }

            String key = "fraud/invalid-" + Instant.now().toEpochMilli() + ".json";

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(key)
                    .contentType("application/json")
                    .build();

            s3Client.putObject(request, temp.toPath());
            temp.delete();

            log.warn("Uploaded INVALID JSON to S3 under {}", key);

        } catch (Exception e) {
            log.error("Failed to upload invalid JSON to S3", e);
        }
    }

}
