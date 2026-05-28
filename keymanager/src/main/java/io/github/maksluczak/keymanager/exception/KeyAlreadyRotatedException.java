package io.github.maksluczak.keymanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.CONFLICT)
public class KeyAlreadyRotatedException extends RuntimeException {
    public KeyAlreadyRotatedException(UUID id) {
        super("Key with this id already got rotated: " + id);
    }
}
