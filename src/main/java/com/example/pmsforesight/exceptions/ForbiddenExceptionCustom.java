package com.example.pmsforesight.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbiddenExceptionCustom extends RuntimeException {
    public ForbiddenExceptionCustom(String str){
        super(str);
    }
}
