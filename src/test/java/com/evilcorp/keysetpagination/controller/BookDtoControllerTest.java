package com.evilcorp.keysetpagination.controller;

import com.evilcorp.keysetpagination.dto.books.GetBookRequest;
import com.evilcorp.keysetpagination.entity.Book;
import com.evilcorp.keysetpagination.repository.BookRepository;
import com.evilcorp.keysetpagination.testcontainers.TestcontainersInitializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        bookRepository.saveAll(
                Stream.generate(this::generateBooks)
                        .limit(9).toList()
        );
    }

    @Test
    void test() throws Exception {
        assertEquals(9, bookRepository.count());

        var resultOfFirstRequest = mockMvc.perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "filter": {
                                            "limit": 3
                                          }
                                        }"""
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").hasJsonPath())
                .andExpect(jsonPath("books").isArray())
                .andReturn();
        JsonNode jsonNode = objectMapper.readTree(resultOfFirstRequest.getResponse().getContentAsString());
        var token = jsonNode.get("token").textValue();

        var resultOfSecondRequest = mockMvc.perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(GetBookRequest.builder().token(token).build()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").hasJsonPath())
                .andExpect(jsonPath("books").isArray())
                .andReturn();

        var secondToken = objectMapper.readTree(resultOfSecondRequest.getResponse().getContentAsString()).get("token").textValue();

        var resultOfThirdRequest = mockMvc.perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(GetBookRequest.builder().token(secondToken).build()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").hasJsonPath())
                .andExpect(jsonPath("books").isArray())
                .andExpect(jsonPath("token").isEmpty())
                .andDo(print())
                .andReturn();

        var thirdToken = objectMapper.readTree(resultOfThirdRequest.getResponse().getContentAsString()).get("token").textValue();
        mockMvc.perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(GetBookRequest.builder().token(thirdToken).build()))
                )
                .andExpect(status().is4xxClientError());
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @NotNull
    private Book generateBooks() {
        var book = new Book();
        book.setRating(ThreadLocalRandom.current().nextInt(100));
        return book;
    }
}