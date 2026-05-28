package io.github.maksluczak.keymanager.service;

import io.github.maksluczak.keymanager.dto.KeyRequest;
import io.github.maksluczak.keymanager.dto.KeyResponse;
import io.github.maksluczak.keymanager.exception.KeyAlreadyRotatedException;
import io.github.maksluczak.keymanager.exception.KeyNotFoundException;
import io.github.maksluczak.keymanager.model.CryptoKey;
import io.github.maksluczak.keymanager.repository.KeyRepository;
import io.github.maksluczak.keymanager.validator.KeyValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyServiceImplementationTest {

    @Mock
    private KeyRepository repository;

    @Mock
    private KeyValidator validator;

    @InjectMocks
    private KeyServiceImplementation keyService;

    @Test
    void shouldCreateKeySuccessfully() {
        KeyRequest request = new KeyRequest();
        request.setName("radio-key-1");

        when(repository.save(any(CryptoKey.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        KeyResponse response = keyService.createKey(request);

        assertThat(response.getName()).isEqualTo("radio-key-1");
        assertThat(response.getStatus()).isEqualTo(CryptoKey.KeyStatus.ACTIVE.toString());
        verify(repository).save(any(CryptoKey.class));
        verify(validator).validateUniqueName("radio-key-1");
    }

    @Test
    void shouldThrowWhenKeyNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(KeyNotFoundException.class, () -> keyService.getKey(id));
        verify(repository).findById(id);
    }

    @Test
    void shouldRotateActiveKey() {
        UUID id = UUID.randomUUID();
        CryptoKey key = activeKey(id);

        when(repository.findById(id)).thenReturn(Optional.of(key));

        KeyResponse response = keyService.rotateKey(id);
        assertThat(response.getStatus()).isEqualTo("ROTATED");
    }

    @Test
    void shouldThrowWhenRotatingAlreadyRotatedKey() {
        UUID id = UUID.randomUUID();
        CryptoKey key = activeKey(id);
        key.setStatus(CryptoKey.KeyStatus.ROTATED);

        when(repository.findById(id)).thenReturn(Optional.of(key));

        assertThrows(KeyAlreadyRotatedException.class, () -> keyService.rotateKey(id));
        verify(repository, never()).save(any());
    }

    @ParameterizedTest
    @EnumSource(value = CryptoKey.KeyStatus.class, names = {"ROTATED", "REVOKED"})
    void shouldThrowWhenRotatingNonActiveKey(CryptoKey.KeyStatus status) {
        UUID id = UUID.randomUUID();
        CryptoKey key = activeKey(id);
        key.setStatus(status);

        when(repository.findById(id)).thenReturn(Optional.of(key));

        assertThrows(RuntimeException.class, () -> keyService.rotateKey(id));
    }

    private CryptoKey activeKey(UUID id) {
        CryptoKey key = new CryptoKey();
        key.setId(id);
        key.setName("test-key");
        key.setStatus(CryptoKey.KeyStatus.ACTIVE);
        key.setCreatedAt(Instant.now());
        return key;
    }
}