package com.microservice.glologic.exceptios;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setCodigo(e.getStatus().value());
        errorDetails.setDetail(e.getMessage());
        errorResponse.setError(Collections.singletonList(errorDetails));
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }

    private static class ErrorResponse {
        private List<ErrorDetails> error;

        public List<ErrorDetails> getError() {
            return error;
        }

        public void setError(List<ErrorDetails> error) {
            this.error = error;
        }
    }

    private static class ErrorDetails {
        private LocalDateTime timestamp;
        private int codigo;
        private String detail;

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public int getCodigo() {
            return codigo;
        }

        public void setCodigo(int codigo) {
            this.codigo = codigo;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }

}
