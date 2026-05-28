package io.github.maksluczak.keymanager.repository;

import io.github.maksluczak.keymanager.model.CryptoKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryKeyRepositoryTest {

    private InMemoryKeyRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryKeyRepository();
    }

    @Test
    void shouldSaveKey() {
        // given
        CryptoKey key = new CryptoKey();
        key.setId(UUID.randomUUID());
        key.setName("radio-key-1");
        key.setStatus(CryptoKey.KeyStatus.ACTIVE);
        key.setCreatedAt(Instant.now());

        // when
        repository.save(key);

        // then
        assertTrue(repository.findById(key.getId()).isPresent());
    }

    @Test
    void shouldFindKeyById() {
        // given
        CryptoKey key = new CryptoKey();
        key.setId(UUID.randomUUID());
        key.setName("radio-key-1");
        key.setStatus(CryptoKey.KeyStatus.ACTIVE);
        key.setCreatedAt(Instant.now());

        // when
        repository.save(key);
        CryptoKey foundKey = repository.findById(key.getId())
                .orElse(null);

        // then
        assertNotNull(foundKey);
        assertEquals("radio-key-1", foundKey.getName());
    }

    @Test
    void shouldReturnAllKeys() {
        // given
        CryptoKey key1 = new CryptoKey();
        key1.setId(UUID.randomUUID());
        key1.setName("radio-key-1");
        key1.setStatus(CryptoKey.KeyStatus.ACTIVE);
        key1.setCreatedAt(Instant.now());

        CryptoKey key2 = new CryptoKey();
        key2.setId(UUID.randomUUID());
        key2.setName("radio-key-2");
        key2.setStatus(CryptoKey.KeyStatus.ACTIVE);
        key2.setCreatedAt(Instant.now());

        // when
        repository.save(key1);
        repository.save(key2);

        // then
        assertEquals(2, repository.findAll().size());
    }

    @Test
    void shouldDeleteKey() {
        CryptoKey key = new CryptoKey();
        key.setId(UUID.randomUUID());
        key.setName("radio-key-1");

        repository.save(key);

        repository.deleteById(key.getId());

        assertFalse(repository.findById(key.getId()).isPresent());
    }

    @Test
    void shouldCheckIfNameExists() {
        CryptoKey key = new CryptoKey();
        key.setId(UUID.randomUUID());
        key.setName("radio-key-1");

        repository.save(key);

        assertTrue(repository.existsByName("radio-key-1"));
        assertFalse(repository.existsByName("radio-key-x"));
    }
}