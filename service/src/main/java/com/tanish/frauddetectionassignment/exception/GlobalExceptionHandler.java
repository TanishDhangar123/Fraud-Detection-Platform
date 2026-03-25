package com.tanish.frauddetectionassignment.exception;

import com.tanish.frauddetectionassignment.service.AuditS3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final AuditS3Service auditS3Service;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String rawBody = null;

        if (request instanceof ContentCachingRequestWrapper wrapper) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                rawBody = new String(buf, StandardCharsets.UTF_8);
            }
        }

        log.error("Validation failed at {} | payload={}",
                request.getRequestURI(), rawBody);

        try {
            auditS3Service.uploadInvalid(rawBody);
        } catch (Exception e) {
            log.error("Failed to upload validation JSON to S3", e);
        }

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("VALIDATION_FAILED")
                .path(request.getRequestURI())
                .message("Request validation failed.")
                .fieldErrors(errors)
                .transactionId(MDC.get("transactionId"))
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableBody(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String rawBody = null;

        if (request instanceof ContentCachingRequestWrapper wrapper) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                rawBody = new String(buf, StandardCharsets.UTF_8);
            }
        }

        log.error("Malformed JSON at {} : {}", request.getRequestURI(), rawBody);

        try {
            auditS3Service.uploadInvalid(rawBody);
        } catch (Exception e) {
            log.error("Failed to upload invalid JSON to S3", e);
        }

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("MALFORMED_JSON")
                .path(request.getRequestURI())
                .message("Request body is invalid JSON.")
                .transactionId(MDC.get("transactionId"))
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {

        log.error("Unexpected error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .code("INTERNAL_ERROR")
                .path(request.getRequestURI())
                .message("Something went wrong. Please try again later.")
                .transactionId(MDC.get("transactionId"))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
