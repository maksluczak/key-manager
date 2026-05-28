package io.github.maksluczak.keymanager.repository;

import io.github.maksluczak.keymanager.model.CryptoKey;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryKeyRepository implements KeyRepository {

    private final Map<UUID, CryptoKey> store = new ConcurrentHashMap<>();

    @Override
    public CryptoKey save(CryptoKey key) {
        store.put(key.getId(), key);
        return key;
    }

    @Override
    public Optional<CryptoKey> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<CryptoKey> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }

    @Override
    public boolean existsByName(String name) {
        return store.values().stream()
                .anyMatch(cryptoKey -> cryptoKey.getName().equals(name));
    }
}
