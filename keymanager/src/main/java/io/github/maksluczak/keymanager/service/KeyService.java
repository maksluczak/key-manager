package io.github.maksluczak.keymanager.service;

import io.github.maksluczak.keymanager.dto.KeyRequest;
import io.github.maksluczak.keymanager.dto.KeyResponse;

import java.util.UUID;

public interface KeyService {

    KeyResponse createKey(KeyRequest request);
    KeyResponse getKey(UUID id);
    KeyResponse rotateKey(UUID id);
    void revokeKey(UUID id);
}
