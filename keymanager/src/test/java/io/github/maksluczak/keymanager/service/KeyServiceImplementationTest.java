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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
        // given
        KeyRequest request = new KeyRequest();
        request.setName("radio-key-1");

        given(repository.save(any(CryptoKey.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        KeyResponse response = keyService.createKey(request);

        // then
        assertThat(response.getName()).isEqualTo("radio-key-1");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");

        verify(repository).save(any(CryptoKey.class));
        verify(validator).validateUniqueName("radio-key-1");
    }

    @Test
    void shouldThrowWhenKeyNotFound() {
        // given
        UUID id = UUID.randomUUID();

        given(repository.findById(id))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> keyService.getKey(id))
                .isInstanceOf(KeyNotFoundException.class);

        verify(repository).findById(id);
    }

    @Test
    void shouldRotateActiveKey() {
        // given
        UUID id = UUID.randomUUID();
        CryptoKey key = activeKey(id);

        given(repository.findById(id))
                .willReturn(Optional.of(key));

        // when
        KeyResponse response = keyService.rotateKey(id);

        // then
        assertThat(response.getStatus()).isEqualTo("ROTATED");

        verify(repository).save(key);
    }

    @Test
    void shouldThrowWhenRotatingAlreadyRotatedKey() {
        // given
        UUID id = UUID.randomUUID();
        CryptoKey key = activeKey(id);
        key.setStatus(CryptoKey.KeyStatus.ROTATED);

        given(repository.findById(id))
                .willReturn(Optional.of(key));

        // when
        // then
        assertThatThrownBy(() -> keyService.rotateKey(id))
                .isInstanceOf(KeyAlreadyRotatedException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void shouldRevokeActiveKey() {
        // given
        UUID id = UUID.randomUUID();
        CryptoKey key = activeKey(id);

        given(repository.findById(id))
                .willReturn(Optional.of(key));

        // when
        keyService.revokeKey(id);

        // then
        assertThat(key.getStatus())
                .isEqualTo(CryptoKey.KeyStatus.REVOKED);

        verify(repository).save(key);
    }

    @ParameterizedTest
    @EnumSource(value = CryptoKey.KeyStatus.class, names = {"ROTATED", "REVOKED"})
    void shouldThrowWhenRotatingNonActiveKey(CryptoKey.KeyStatus status) {
        // given
        UUID id = UUID.randomUUID();
        CryptoKey key = activeKey(id);
        key.setStatus(status);

        given(repository.findById(id))
                .willReturn(Optional.of(key));

        // when
        // then
        assertThatThrownBy(() -> keyService.rotateKey(id))
                .isInstanceOf(RuntimeException.class);

        verify(repository, never()).save(any());
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