package io.github.maksluczak.keymanager.controller;

import io.github.maksluczak.keymanager.dto.KeyRequest;
import io.github.maksluczak.keymanager.dto.KeyResponse;
import io.github.maksluczak.keymanager.exception.KeyAlreadyRotatedException;
import io.github.maksluczak.keymanager.exception.KeyNotFoundException;
import io.github.maksluczak.keymanager.service.KeyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(KeyController.class)
class KeyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KeyService keyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturn201WhenCreatingKey() throws Exception {
        KeyRequest request = new KeyRequest();
        request.setName("radio-key-2");

        KeyResponse response = new KeyResponse();
        response.setId(UUID.randomUUID());
        response.setName("radio-key-2");
        response.setStatus("ACTIVE");

        when(keyService.createKey(any())).thenReturn(response);

        mockMvc.perform(post("/api/keys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("radio-key-2"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        KeyRequest request = new KeyRequest();
        request.setName("");

        mockMvc.perform(post("/api/keys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldReturn404WhenKeyNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(keyService.getKey(id)).thenThrow(new KeyNotFoundException(id));

        mockMvc.perform(get("/api/keys/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void shouldReturn409WhenRotatingAlreadyRotatedKey() throws Exception {
        UUID id = UUID.randomUUID();
        when(keyService.rotateKey(id)).thenThrow(new KeyAlreadyRotatedException(id));

        mockMvc.perform(post("/api/keys/" + id + "/rotate"))
                .andExpect(status().isConflict());
    }
}