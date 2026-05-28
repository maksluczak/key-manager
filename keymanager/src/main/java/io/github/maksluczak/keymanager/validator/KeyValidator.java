package io.github.maksluczak.keymanager.validator;

import io.github.maksluczak.keymanager.exception.KeyAlreadyExistsException;
import io.github.maksluczak.keymanager.repository.KeyRepository;
import org.springframework.stereotype.Component;

@Component
public class KeyValidator {

    private final KeyRepository repository;

    public KeyValidator(KeyRepository repository) {
        this.repository = repository;
    }

    public void validateUniqueName(String name) {
        if (repository.existsByName(name)) {
            throw new KeyAlreadyExistsException(name);
        }
    }
}
