package com.tanish.frauddetectionassignment.exception;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.Map;

@Data
@Builder
public class ApiError {

    @Builder.Default
    private Instant timestamp = Instant.now();

    private int status;
    private String error;
    private String code;
    private String path;
    private String message;
    private Map<String, String> fieldErrors;

    private String transactionId;
}
