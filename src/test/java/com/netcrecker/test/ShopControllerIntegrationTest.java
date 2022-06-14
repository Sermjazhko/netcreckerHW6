package com.netcrecker.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcrecker.model.Shop;
import com.netcrecker.services.ShopRepository;
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
public class ShopControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShopRepository repository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void givenId_whenGetShop_thenReturn200() throws Exception {
        Shop shop = new Shop("Дом книги", "Сормовский район", 20);

        long id = repository.save(shop).getId();
        mvc.perform(get("http://localhost:9090/shop/shop/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(shop.getName()))
                .andExpect(jsonPath("$.area").value(shop.getArea()))
                .andExpect(jsonPath("$.commission").value(shop.getCommission()));
    }

    @Test
    public void whenGetAllShops_thenReturn200() throws Exception {
        Shop shop1 = new Shop("Дом книги", "Сормовский район", 20);
        Shop shop2 = new Shop("Читай город", "Автозаводской район", 10);
        Shop shop3 = new Shop("Чакона", "Сормовский район", 15);
        repository.save(shop1);
        repository.save(shop2);
        repository.save(shop3);

        mvc.perform(get("http://localhost:9090/shop/shops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name").value(shop2.getName()))
                .andExpect(jsonPath("$[2].area").value(shop3.getArea()))
                .andExpect(jsonPath("$[0].commission").value(shop1.getCommission()));
    }

    @Test
    public void givenId_deleteShop_thenReturn204() throws Exception {

        Shop shop1 = new Shop("Дом книги", "Сормовский район", 20);

        long id = repository.save(shop1).getId();
        mvc.perform(delete("http://localhost:9090/shop/delete/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenIdAndShop_putShop_thenReturn200() throws Exception {

        Shop shop1 = new Shop("Дом книги", "Сормовский район", 20);
        Shop shop2 = new Shop("Читай город", "Автозаводской район", 10);

        long id = repository.save(shop1).getId();
        mvc.perform(put("http://localhost:9090/shop/update/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(shop2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(shop2.getName()))
                .andExpect(jsonPath("$.area").value(shop2.getArea()))
                .andExpect(jsonPath("$.commission").value(shop2.getCommission()));
    }

    @Test
    public void whenShopCreated_thenReturn201() throws Exception {
        Shop shop1 = new Shop("Дом книги", "Сормовский район", 20);

        mvc.perform(post("http://localhost:9090/shop/shops")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(shop1)))
                .andExpect(status().isCreated());
    }

    // ------------------------------------------------------------------------------------------------
    @Test
    public void whenGetDifferentNameShops_thenReturn200() throws Exception {
        Shop shop1 = new Shop("Дом книги", "Сормовский район", 20);
        Shop shop2 = new Shop("Читай город", "Автозаводской район", 10);
        Shop shop3 = new Shop("Чакона", "Советский район", 15);
        repository.save(shop1);
        repository.save(shop2);
        repository.save(shop3);


        mvc.perform(get("http://localhost:9090/shop/name")
                        .contentType("application/json")
                        .content("Сормовский район, Советский район"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value(shop1.getName()))
                .andExpect(jsonPath("$[1]").value(shop3.getName()));
    }


}
