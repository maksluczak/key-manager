package io.github.maksluczak.keymanager.dto;

import io.github.maksluczak.keymanager.model.CryptoKey;

import java.time.Instant;
import java.util.UUID;

public class KeyResponse {

    private UUID id;
    private String name;
    private String status;
    private Instant createdAt;

    public static KeyResponse from(CryptoKey key) {
        KeyResponse response = new KeyResponse();
        response.id = key.getId();
        response.name = key.getName();
        response.status = key.getStatus().name();
        return response;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
