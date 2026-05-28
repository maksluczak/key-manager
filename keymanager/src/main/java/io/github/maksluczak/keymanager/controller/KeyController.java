package io.github.maksluczak.keymanager.controller;

import io.github.maksluczak.keymanager.dto.KeyRequest;
import io.github.maksluczak.keymanager.dto.KeyResponse;
import io.github.maksluczak.keymanager.service.KeyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/keys")
public class KeyController {

    private final KeyService service;

    public KeyController(KeyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<KeyResponse> create(@Valid @RequestBody KeyRequest request) {
        KeyResponse response = service.createKey(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeyResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getKey(id));
    }

    @PostMapping("/{id}/rotate")
    public ResponseEntity<KeyResponse> rotate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.rotateKey(id));
    }

    @DeleteMapping("/{id}/revoke")
    public ResponseEntity<Void> revoke(@PathVariable UUID id) {
        service.revokeKey(id);
        return ResponseEntity.noContent().build();
    }
}
