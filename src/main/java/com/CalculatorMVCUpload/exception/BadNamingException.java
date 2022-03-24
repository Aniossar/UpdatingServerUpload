package com.CalculatorMVCUpload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Filename should be like 'KPD_*.zip'. Rename the file")
public class BadNamingException extends RuntimeException{
    public BadNamingException(String message) {
        super(message);
    }

    public BadNamingException(String message, Throwable cause) {
        super(message, cause);
    }
}
