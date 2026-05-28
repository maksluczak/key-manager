package io.github.maksluczak.keymanager.exception;

public class KeyAlreadyExistsException extends RuntimeException {

    public KeyAlreadyExistsException(String name) {
        super("Key with this name already exists: " + name);
    }
}
