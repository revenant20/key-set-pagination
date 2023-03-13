package com.evilcorp.keysetpagination.controller;

import com.evilcorp.keysetpagination.dto.books.BookFilter;
import com.evilcorp.keysetpagination.dto.books.GetBookRequest;
import com.evilcorp.keysetpagination.dto.books.Sorting;
import com.evilcorp.keysetpagination.testcontainers.TestcontainersInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
@EnabledIf(expression = "${keysetpagination.testcontainers.enabled}", loadContext = true)
@Rollback(value = false)
@Transactional(propagation = Propagation.NEVER)

class BookDtoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void test() throws Exception {
        mockMvc.perform(
                post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "filter": {
                                    "limit": 1,
                                    "offset": 1
                                  },
                                  "sorting": {
                                    "direction": "ASC",
                                    "properties": ["id"]
                                  }
                                }"""
                        )
        ).andExpect(status().isOk());
    }

    @Test
    void testTokenExists() throws Exception {
        mockMvc.perform(
                post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "filter": {
                                    "limit": 1,
                                    "offset": 1
                                  },
                                  "sorting": {
                                    "direction": "ASC",
                                    "fieldName": "str"
                                  },
                                  "token": "ewogICJmaWx0ZXIiOiB7CiAgICAibGltaXQiOiAxLAogICAgIm9mZnNldCI6IDEKICB9LAogICJzb3J0aW5nIjogewogICAgImRpcmVjdGlvbiI6ICJBU0MiLAogICAgInByb3BlcnRpZXMiOiBbImlkIl0KICB9Cn0="
                                }"""

                        )
        ).andExpect(status().isOk());
    }
}