package me.dragonappear.domain.main;


import me.dragonappear.domain.main.exception.Custom4xxException;
import me.dragonappear.domain.main.exception.Custom5xxException;
import me.dragonappear.domain.main.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static me.dragonappear.domain.main.exception.CustomExceptionError.*;

/**
 * ExceptionHandler
 */

@RestController
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse(NOT_VALID_ARGUMENT.getErrorCode(), ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Custom4xxException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(Custom4xxException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getError().getErrorCode(), ex.getError().getErrorMsg());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = new ErrorResponse(NOT_VALID_ARGUMENT.getErrorCode(), NOT_VALID_ARGUMENT.getErrorMsg());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(NOT_SUPPORTED_METHOD.getErrorCode(), NOT_SUPPORTED_METHOD.getErrorMsg());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @RequestMapping("/api/error/404")
    public ResponseEntity<ErrorResponse> error404() {
        ErrorResponse errorResponse = new ErrorResponse(NOT_FOUND_RESOURCE.getErrorCode(), NOT_FOUND_RESOURCE.getErrorMsg());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Custom5xxException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(Custom5xxException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getError().getErrorCode(), ex.getError().getErrorMsg());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(), INTERNAL_SERVER_ERROR.getErrorMsg());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @RequestMapping("/api/error/500")
    public ResponseEntity<ErrorResponse> error500() {
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(), INTERNAL_SERVER_ERROR.getErrorMsg());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


}
