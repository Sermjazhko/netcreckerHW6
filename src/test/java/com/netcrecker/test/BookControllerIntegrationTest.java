package com.netcrecker.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcrecker.model.Book;
import com.netcrecker.services.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class BookControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository repository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void givenId_whenGetBook_thenReturn200() throws Exception {
        Book book = new Book("Гарри Поттер и философский камень", 2120, "Автозаводской район", 10);

        long id = repository.save(book).getId();
        mvc.perform(get("http://localhost:9090/book/book/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(book.getName()))
                .andExpect(jsonPath("$.price").value(book.getPrice()))
                .andExpect(jsonPath("$.warehouse").value(book.getWarehouse()))
                .andExpect(jsonPath("$.quantity").value(book.getQuantity()));
    }

    @Test
    public void whenGetAllBooks_thenReturn200() throws Exception {
        Book book1 = new Book("Гарри Поттер и философский камень", 2120, "Автозаводской район", 10);
        Book book2 = new Book("Гарри Поттер и тайная комната", 3210, "Сормовский район", 100);
        Book book3 = new Book("Гарри Поттер и узник азкабана", 2500, "Советский район", 12);

        repository.save(book1);
        repository.save(book2);
        repository.save(book3);

        mvc.perform(get("http://localhost:9090/book/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name").value(book2.getName()))
                .andExpect(jsonPath("$[2].price").value(book3.getPrice()))
                .andExpect(jsonPath("$[0].warehouse").value(book1.getWarehouse()))
                .andExpect(jsonPath("$[1].quantity").value(book2.getQuantity()));
    }

    @Test
    public void givenId_deleteBook_thenReturn204() throws Exception {
        Book book = new Book("Гарри Поттер и философский камень", 2120, "Автозаводской район", 10);

        long id = repository.save(book).getId();
        mvc.perform(delete("http://localhost:9090/book/delete/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenIdAndBook_putBook_thenReturn200() throws Exception {
        Book book = new Book("Гарри Поттер и философский камень", 2120, "Автозаводской район", 10);
        Book book2 = new Book("Гарри Поттер и тайная комната", 2130, "Сормовский район", 240);

        long id = repository.save(book).getId();
        mvc.perform(put("http://localhost:9090/book/update/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(book2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(book2.getName()))
                .andExpect(jsonPath("$.price").value(book2.getPrice()))
                .andExpect(jsonPath("$.warehouse").value(book2.getWarehouse()))
                .andExpect(jsonPath("$.quantity").value(book2.getQuantity()));
    }

    @Test
    public void whenBookCreated_thenReturn201() throws Exception {
        Book book = new Book("Гарри Поттер и философский камень", 2120, "Автозаводской район", 10);

        mvc.perform(post("http://localhost:9090/book/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());
    }

    // ------------------------------------------------------------------------------------------------
    @Test
    public void whenGetDifferentBooks_thenReturn200() throws Exception {
        Book book1 = new Book("Гарри Поттер и философский камень", 2120, "Автозаводской район", 10);
        Book book2 = new Book("Гарри Поттер и тайная комната", 3210, "Сормовский район", 100);
        Book book3 = new Book("Гарри Поттер и философский камень", 2500, "Советский район", 12);

        repository.save(book1);
        repository.save(book2);
        repository.save(book3);

        mvc.perform(get("http://localhost:9090/book/various"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void whenGetNameAndPriceBooks_thenReturn200() throws Exception {
        Book book1 = new Book("Книги по устройству windows", 2120, "Автозаводской район", 10);
        Book book2 = new Book("Гарри Поттер и тайная комната", 20210, "Сормовский район", 100);
        Book book3 = new Book("Гарри Поттер и философский камень", 2500, "Советский район", 12);

        repository.save(book1);
        repository.save(book2);
        repository.save(book3);

        mvc.perform(get("http://localhost:9090/book/name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1]").value(book1.getName()+","+book1.getPrice()))
                .andExpect(jsonPath("$[0]").value(book2.getName()+","+book2.getPrice()));
    }
}
