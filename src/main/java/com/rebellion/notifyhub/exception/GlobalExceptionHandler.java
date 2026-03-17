package com.rebellion.notifyhub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(Exception ex, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponseDto response = new ErrorResponseDto(status, ex.getMessage(), request);
        return ResponseEntity.status(status).body(response);
    }

	@ExceptionHandler({MethodArgumentNotValidException.class, CustomInvalidJsonStructure.class})
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(Exception ex, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDto response = new ErrorResponseDto(status, ex.getMessage(), request);
        return ResponseEntity.status(status).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex, HttpServletRequest request){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponseDto response = new ErrorResponseDto(status, ex.getMessage(), request);
        return ResponseEntity.status(status).body(response);
    }
}
