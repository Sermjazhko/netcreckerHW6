package com.netcrecker.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcrecker.model.Book;
import com.netcrecker.model.Buyer;
import com.netcrecker.services.BuyerRepository;
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
public class BuyerControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BuyerRepository repository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void givenId_whenGetBuyer_thenReturn200() throws Exception {
        Buyer buyer = new Buyer("Петров", "Сормовский район", 20);

        long id = repository.save(buyer).getId();
        mvc.perform(get("http://localhost:9090/buyer/buyer/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.surname").value(buyer.getSurname()))
                .andExpect(jsonPath("$.area").value(buyer.getArea()))
                .andExpect(jsonPath("$.discount").value(buyer.getDiscount()));
    }

    @Test
    public void whenGetAllBuyers_thenReturn200() throws Exception {
        Buyer buyer1 = new Buyer("Петров", "Сормовский район", 20);
        Buyer buyer2 = new Buyer("Сидоров", "Автозаводской район", 10);
        Buyer buyer3 = new Buyer("Иванов", "Сормовский район", 15);
        repository.save(buyer1);
        repository.save(buyer2);
        repository.save(buyer3);

        mvc.perform(get("http://localhost:9090/buyer/buyers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].surname").value(buyer2.getSurname()))
                .andExpect(jsonPath("$[2].area").value(buyer3.getArea()))
                .andExpect(jsonPath("$[0].discount").value(buyer1.getDiscount()));
    }

    @Test
    public void givenId_deleteBuyer_thenReturn204() throws Exception {

        Buyer buyer1 = new Buyer("Петров", "Сормовский район", 20);

        long id = repository.save(buyer1).getId();
        mvc.perform(delete("http://localhost:9090/buyer/delete/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenIdAndBuyer_putBuyer_thenReturn200() throws Exception {

        Buyer buyer1 = new Buyer("Петров", "Сормовский район", 20);
        Buyer buyer2 = new Buyer("Сидоров", "Автозаводской район", 10);

        long id = repository.save(buyer1).getId();
        mvc.perform(put("http://localhost:9090/buyer/update/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(buyer2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.surname").value(buyer2.getSurname()))
                .andExpect(jsonPath("$.area").value(buyer2.getArea()))
                .andExpect(jsonPath("$.discount").value(buyer2.getDiscount()));
    }

    @Test
    public void whenBuyerCreated_thenReturn201() throws Exception {
        Buyer buyer1 = new Buyer("Петров", "Сормовский район", 20);

        mvc.perform(post("http://localhost:9090/buyer/buyers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(buyer1)))
                .andExpect(status().isCreated());
    }

    // ------------------------------------------------------------------------------------------------
    @Test
    public void whenGetDifferentArea_thenReturn200() throws Exception {
        Buyer buyer1 = new Buyer("Петров", "Сормовский район", 20);
        Buyer buyer2 = new Buyer("Сидоров", "Автозаводской район", 10);
        Buyer buyer3 = new Buyer("Иванов", "Сормовский район", 15);
        repository.save(buyer1);
        repository.save(buyer2);
        repository.save(buyer3);

        mvc.perform(get("http://localhost:9090/buyer/various"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void whenGetSurnameAndDiscountBuyers_thenReturn200() throws Exception {
        Buyer buyer1 = new Buyer("Петров", "Нижегородский район", 20);
        Buyer buyer2 = new Buyer("Сидоров", "Автозаводской район", 10);
        Buyer buyer3 = new Buyer("Иванов", "Нижегородский район", 15);
        repository.save(buyer1);
        repository.save(buyer2);
        repository.save(buyer3);

        mvc.perform(get("http://localhost:9090/buyer/district")
                        .contentType("application/json")
                        .content("Нижегородский район"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value(buyer1.getSurname()+","+buyer1.getDiscount()))
                .andExpect(jsonPath("$[1]").value(buyer3.getSurname()+","+buyer3.getDiscount()));
    }
}
