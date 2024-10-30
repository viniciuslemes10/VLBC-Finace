package com.br.vlbc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoriaExistException extends RuntimeException {
    public CategoriaExistException(String message) {
        super(message);
    }
}
