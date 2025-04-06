package com.ecomhub.product.service.exception;

import com.ecomhub.product.service.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.FileSystemNotFoundException;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException exception, WebRequest request) {
        log.error("CategoryNotFoundException: {}", exception.getMessage(), exception);
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.toString())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ImageNotProcessedException.class)
    public ResponseEntity<ErrorResponse> handleImageNotProcessedException(ImageNotProcessedException exception, WebRequest request) {
        log.error("ImageNotProcessedException: {}", exception.getMessage(), exception);
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.toString())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception, WebRequest request) {
        log.error("ProductNotFoundException: {}", exception.getMessage(), exception);
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.toString())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(ImageNotPresentException.class)
    public ResponseEntity<ErrorResponse> handleImageNotPresentException(ImageNotPresentException exception, WebRequest request) {
        log.error("ImageNotPresentException: {}", exception.getMessage(), exception);
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.toString())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(FileSystemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileSystemNotFoundException(FileSystemNotFoundException exception, WebRequest request) {
        log.error("FileSystemNotFoundException: {}", exception.getMessage(), exception);
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.toString())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }
}
