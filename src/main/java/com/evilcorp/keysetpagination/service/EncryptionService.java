package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.dto.books.NextBookPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EncryptionService {

    private final ObjectMapper mapper;

    @SneakyThrows
    public NextBookPage decode(String token) {
        var rawJson = Base64.getDecoder().decode(token);
        return mapper.readValue(rawJson, NextBookPage.class);
    }

    @SneakyThrows
    public String encode(NextBookPage page) {
        return Base64.getEncoder().encodeToString(mapper.writeValueAsBytes(page));
    }

}
