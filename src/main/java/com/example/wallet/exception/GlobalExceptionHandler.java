package com.example.wallet.exception;

import com.example.wallet.web.dto.WalletBalanceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.validation.FieldError;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<WalletBalanceResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation exception occurred", e);

        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(new WalletBalanceResponse(errorMessage, null, BAD_REQUEST));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ResponseEntity<WalletBalanceResponse> handleMediaTypeException(HttpMediaTypeNotSupportedException e) {
        log.error("Media type exception occurred", e);

        return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE).body(new WalletBalanceResponse("Unsupported media type", null, UNSUPPORTED_MEDIA_TYPE));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ResponseBody
    public ResponseEntity<WalletBalanceResponse> handleMethodException(HttpRequestMethodNotSupportedException e) {
        log.error("Method exception occurred", e);

        return ResponseEntity.status(METHOD_NOT_ALLOWED).body(new WalletBalanceResponse("Method not allowed", null, METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public ResponseEntity<WalletBalanceResponse> handleNoSuchElementException(NoSuchElementException e) {
        log.error("No such element exception occurred", e);

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<WalletBalanceResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid argument exception occurred", e);

        return ResponseEntity.badRequest().body(new WalletBalanceResponse("Invalid argument", null, BAD_REQUEST));
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<WalletBalanceResponse> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e) {
        log.error("Invalid data access api usage exception occurred", e);

        return ResponseEntity.badRequest().body(new WalletBalanceResponse("Invalid request data", null, BAD_REQUEST));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public ResponseEntity<WalletBalanceResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("No resource found exception occurred", e);

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<WalletBalanceResponse> handleException(Exception e) {
        log.error("Exception occurred", e);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new WalletBalanceResponse("Error occurred", null, INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<WalletBalanceResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("Method argument type mismatch exception occurred", e);

        return ResponseEntity.badRequest().body(new WalletBalanceResponse("Invalid wallet ID", null, BAD_REQUEST));
    }
}