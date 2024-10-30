package com.br.vlbc.exceptions.handler;

import com.br.vlbc.exceptions.CategoriaExistException;
import com.br.vlbc.exceptions.CategoriaNotFoundException;
import com.br.vlbc.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizeResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handlerException(Exception ex, WebRequest req) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                req.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CategoriaNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handlerCategoriaNotFoundException(Exception ex, WebRequest req) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                req.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoriaExistException.class)
    public final ResponseEntity<ExceptionResponse> handlerCategoriaExistException(Exception ex, WebRequest req) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                req.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
