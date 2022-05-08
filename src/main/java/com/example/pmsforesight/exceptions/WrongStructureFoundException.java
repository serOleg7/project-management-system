package com.example.pmsforesight.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class WrongStructureFoundException extends RuntimeException {
    public WrongStructureFoundException(){
        super("Unsupported Media Type: wrong request body");
    }
}
