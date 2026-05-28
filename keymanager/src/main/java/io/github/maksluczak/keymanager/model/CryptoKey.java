package io.github.maksluczak.keymanager.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class CryptoKey {

    private UUID id;
    private String name;
    private KeyStatus status;
    private Instant createdAt;
    private Instant rotatedAt;

    public enum KeyStatus {
        ACTIVE, ROTATED, REVOKED
    }

    public CryptoKey() {
    }

    public CryptoKey(UUID id, String name, KeyStatus status, Instant createdAt, Instant rotatedAt) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.rotatedAt = rotatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KeyStatus getStatus() {
        return status;
    }

    public void setStatus(KeyStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getRotatedAt() {
        return rotatedAt;
    }

    public void setRotatedAt(Instant rotatedAt) {
        this.rotatedAt = rotatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CryptoKey cryptoKey = (CryptoKey) o;
        return Objects.equals(id, cryptoKey.id) && Objects.equals(name, cryptoKey.name) && status == cryptoKey.status && Objects.equals(createdAt, cryptoKey.createdAt) && Objects.equals(rotatedAt, cryptoKey.rotatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, createdAt, rotatedAt);
    }
}
