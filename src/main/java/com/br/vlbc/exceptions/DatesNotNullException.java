package com.br.vlbc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DatesNotNullException extends RuntimeException {
    public DatesNotNullException(String message) {
        super(message);
    }
}
