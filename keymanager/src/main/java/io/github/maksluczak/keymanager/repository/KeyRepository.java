package io.github.maksluczak.keymanager.repository;

import io.github.maksluczak.keymanager.model.CryptoKey;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KeyRepository {

    CryptoKey save(CryptoKey key);
    Optional<CryptoKey> findById(UUID id);
    List<CryptoKey> findAll();
    void deleteById(UUID id);
    boolean existsByName(String name);
}
