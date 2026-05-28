package io.github.maksluczak.keymanager.validator;

import io.github.maksluczak.keymanager.exception.KeyAlreadyExistsException;
import io.github.maksluczak.keymanager.repository.KeyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeyValidatorTest {

    @Mock
    private KeyRepository repository;

    @InjectMocks
    private KeyValidator validator;

    @Test
    void shouldPassWhenNameIsUnique() {
        when(repository.existsByName("radio-key-1")).thenReturn(false);

        assertDoesNotThrow(() -> validator.validateUniqueName("radio-key-1"));
    }

    @Test
    void shouldThrowWhenNameAlreadyExists() {
        when(repository.existsByName("radio-key-1")).thenReturn(true);

        assertThrows(KeyAlreadyExistsException.class, () -> validator.validateUniqueName("radio-key-1"));
    }
}