package com.example.pmsforesight.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicatedIdException extends RuntimeException {
    public DuplicatedIdException(String uid) {
        super("Conflict: you can't add task with the same id. Task #" + uid + " already exists");
    }
}
