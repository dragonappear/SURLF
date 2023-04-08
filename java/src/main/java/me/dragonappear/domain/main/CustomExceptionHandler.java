package me.dragonappear.domain.main;


import me.dragonappear.domain.main.exception.Custom4xxException;
import me.dragonappear.domain.main.exception.Custom5xxException;
import me.dragonappear.domain.main.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static me.dragonappear.domain.main.exception.CustomExceptionError.INTERNAL_SERVER_ERROR;
import static me.dragonappear.domain.main.exception.CustomExceptionError.NOT_VALID_ARGUMENT;


@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        ErrorResponse errorResponse = getErrorResponse(NOT_VALID_ARGUMENT.getErrorCode(), methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Custom4xxException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(Custom4xxException custom4xxException) {
        ErrorResponse errorResponse = getErrorResponse(custom4xxException.getError().getErrorCode(), custom4xxException.getError().getErrorMsg());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
        ErrorResponse errorResponse = getErrorResponse(NOT_VALID_ARGUMENT.getErrorCode(), NOT_VALID_ARGUMENT.getErrorMsg());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Custom5xxException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(Custom5xxException custom5xxException) {
        ErrorResponse errorResponse = getErrorResponse(custom5xxException.getError().getErrorCode(), custom5xxException.getError().getErrorMsg());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException runtimeException) {
        ErrorResponse errorResponse = getErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(), INTERNAL_SERVER_ERROR.getErrorMsg());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private ErrorResponse getErrorResponse(int errorCode, String errorMsg) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(errorCode);
        errorResponse.setErrorMsg(errorMsg);
        return errorResponse;
    }


}
