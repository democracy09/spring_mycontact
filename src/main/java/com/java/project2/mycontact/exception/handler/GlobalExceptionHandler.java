package com.java.project2.mycontact.exception.handler;

import com.java.project2.mycontact.exception.PersonNotFoundException;
import com.java.project2.mycontact.exception.RenameNotPermittedException;
import com.java.project2.mycontact.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.hql.internal.ast.ErrorReporter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RenameNotPermittedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRenameNotPermittedException(RenameNotPermittedException ex){
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePersonNotFoundException(PersonNotFoundException ex){
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getBindingResult().getFieldError());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(RuntimeException ex){
        log.error("server error: {}", ex.getMessage(), ex);

        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "unknown internal server error");
    }



}
