package com.ecomhub.user.service.exception;

import com.ecomhub.user.service.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * /auth/register - Handle UserAlreadyExistException
     */
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException exception, WebRequest request) {

        log.error("UserAlreadyExistException: {}", exception.getMessage(), exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.toString())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * /auth/login - Handle UserNotFoundException
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {

        log.error("UserNotFoundException: {}", exception.getMessage(), exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.toString())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * /auth/login - Handle InvalidCredentialsException
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException exception, WebRequest request) {

        log.error("InvalidCredentialsException: {}", exception.getMessage(), exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.toString())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

}
