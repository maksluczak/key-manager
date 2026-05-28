package io.github.maksluczak.keymanager.exception;

import java.util.UUID;

public class KeyNotFoundException extends RuntimeException {

    public KeyNotFoundException(UUID id) {
        super("Key not found: " + id);
    }
}
