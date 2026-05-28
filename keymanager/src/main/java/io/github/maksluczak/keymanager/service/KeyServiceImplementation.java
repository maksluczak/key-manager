package io.github.maksluczak.keymanager.service;

import io.github.maksluczak.keymanager.dto.KeyRequest;
import io.github.maksluczak.keymanager.dto.KeyResponse;
import io.github.maksluczak.keymanager.exception.KeyAlreadyRotatedException;
import io.github.maksluczak.keymanager.exception.KeyNotFoundException;
import io.github.maksluczak.keymanager.model.CryptoKey;
import io.github.maksluczak.keymanager.repository.KeyRepository;
import io.github.maksluczak.keymanager.validator.KeyValidator;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class KeyServiceImplementation implements KeyService {

    private final KeyRepository repository;
    private final KeyValidator keyValidator;

    public KeyServiceImplementation(KeyRepository repository, KeyValidator keyValidator) {
        this.repository = repository;
        this.keyValidator = keyValidator;
    }

    @Override
    public KeyResponse createKey(KeyRequest request) {
        keyValidator.validateUniqueName(request.getName());

        CryptoKey cryptoKey = new CryptoKey();
        cryptoKey.setId(UUID.randomUUID());
        cryptoKey.setName(request.getName());
        cryptoKey.setStatus(CryptoKey.KeyStatus.ACTIVE);
        cryptoKey.setCreatedAt(Instant.now());

        repository.save(cryptoKey);
        return KeyResponse.from(cryptoKey);
    }

    @Override
    public KeyResponse getKey(UUID id) {
        return KeyResponse.from(repository.findById(id)
                .orElseThrow(() -> new KeyNotFoundException(id)));
    }

    @Override
    public KeyResponse rotateKey(UUID id) {
        CryptoKey key = repository.findById(id)
                .orElseThrow();

        if (key.getStatus().equals(CryptoKey.KeyStatus.ROTATED)) {
            throw new KeyAlreadyRotatedException(id);
        }

        if (key.getStatus().equals(CryptoKey.KeyStatus.REVOKED)) {
            throw new IllegalStateException("Cannot rotate a revoked key: " + id);
        }

        key.setStatus(CryptoKey.KeyStatus.ROTATED);
        key.setRotatedAt(Instant.now());
        return KeyResponse.from(key);
    }

    @Override
    public void revokeKey(UUID id) {
        CryptoKey key = repository.findById(id)
                .orElseThrow();

        key.setStatus(CryptoKey.KeyStatus.REVOKED);
    }
}
