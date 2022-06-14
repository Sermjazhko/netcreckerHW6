package com.netcrecker.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcrecker.model.*;
import com.netcrecker.services.BookRepository;
import com.netcrecker.services.BuyerRepository;
import com.netcrecker.services.PurchasesRepository;
import com.netcrecker.services.ShopRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class PurchasesControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PurchasesRepository repository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private ShopRepository shopRepository;


    @AfterEach
    public void resetDb() {
        repository.deleteAll();
        bookRepository.deleteAll();
        buyerRepository.deleteAll();
        shopRepository.deleteAll();
    }

    @Test
    public void givenId_whenGetPurchases_thenReturn200() throws Exception {
        Purchases purchases = new Purchases(Date.valueOf("2022-11-03"), 1, 1, 1, 20, 2939);

        long id = repository.save(purchases).getNumber();
        mvc.perform(get("http://localhost:9090/purchase/purchases/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(String.valueOf(purchases.getDate())))
                .andExpect(jsonPath("$.seller").value(purchases.getSeller()))
                .andExpect(jsonPath("$.buyer").value(purchases.getBuyer()))
                .andExpect(jsonPath("$.quantity").value(purchases.getQuantity()))
                .andExpect(jsonPath("$.sum").value(purchases.getSum()));
    }

    @Test
    public void whenGetAllPurchases_thenReturn200() throws Exception {
        Purchases purchases1 = new Purchases(Date.valueOf("2022-11-03"), 1, 1, 1, 20, 2939);
        Purchases purchases2 = new Purchases(Date.valueOf("2022-10-11"), 1, 2, 3, 15, 3000);
        Purchases purchases3 = new Purchases(Date.valueOf("2022-03-12"), 2, 1, 3, 13, 2939);

        repository.save(purchases1);
        repository.save(purchases2);
        repository.save(purchases3);

        mvc.perform(get("http://localhost:9090/purchase/purchases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value(String.valueOf(purchases1.getDate())))
                .andExpect(jsonPath("$[1].seller").value(purchases2.getSeller()))
                .andExpect(jsonPath("$[2].buyer").value(purchases3.getBuyer()))
                .andExpect(jsonPath("$[0].quantity").value(purchases1.getQuantity()))
                .andExpect(jsonPath("$[1].sum").value(purchases2.getSum()));
    }

    @Test
    public void givenId_deletePurchase_thenReturn204() throws Exception {
        Purchases purchases1 = new Purchases(Date.valueOf("2022-11-03"), 1, 1, 1, 20, 2939);

        long id = repository.save(purchases1).getNumber();
        mvc.perform(delete("http://localhost:9090/purchase/delete/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenIdAndPurchase_putPurchase_thenReturn200() throws Exception {

        Purchases purchases1 = new Purchases(Date.valueOf("2022-11-03"), 1, 1, 1, 20, 2939);
        Purchases purchases2 = new Purchases(Date.valueOf("2022-10-11"), 1, 2, 3, 15, 3000);

        long id = repository.save(purchases1).getNumber();
        mvc.perform(put("http://localhost:9090/purchase/update/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(purchases2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(String.valueOf(purchases2.getDate())))
                .andExpect(jsonPath("$.seller").value(purchases2.getSeller()))
                .andExpect(jsonPath("$.buyer").value(purchases2.getBuyer()))
                .andExpect(jsonPath("$.quantity").value(purchases2.getQuantity()))
                .andExpect(jsonPath("$.sum").value(purchases2.getSum()));;
    }

    @Test
    public void whenPurchaseCreated_thenReturn201() throws Exception {
        Purchases purchases1 = new Purchases();
        purchases1.setDate(Date.valueOf("2022-11-03"));
        purchases1.setQuantity(20);

        Shop shop1 = new Shop("Дом книги", "Сормовский район", 20);
        Buyer buyer1 = new Buyer("Петров", "Нижегородский район", 20);
        Book book1 = new Book("Гарри Поттер и философский камень", 2120, "Автозаводской район", 10);
        shopRepository.save(shop1);
        bookRepository.save(book1);
        buyerRepository.save(buyer1);
        mvc.perform(post("http://localhost:9090/purchase/purchases/{idShop}/{idBuyer}/{idBook}", shop1.getId(), buyer1.getId(), book1.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(purchases1)))
                .andExpect(status().isCreated());
    }

    // ------------------------------------------------------------------------------------------------
    @Test
    public void whenGetDifferentNamePurchases_thenReturn200() throws Exception {
        Purchases purchases1 = new Purchases(Date.valueOf("2022-11-03"), 1, 1, 1, 20, 2939);
        Purchases purchases2 = new Purchases(Date.valueOf("2022-10-11"), 1, 2, 3, 15, 3000);
        Purchases purchases3 = new Purchases(Date.valueOf("2022-03-12"), 2, 1, 3, 13, 2939);
        Purchases purchases4 = new Purchases(Date.valueOf("2022-10-12"), 2, 1, 3, 13, 2939);

        repository.save(purchases1);
        repository.save(purchases2);
        repository.save(purchases3);
        repository.save(purchases4);
        mvc.perform(get("http://localhost:9090/purchase/various"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]").value("03"))
                .andExpect(jsonPath("$[1]").value("10"))
                .andExpect(jsonPath("$[2]").value("11"));
    }

    @Test
    public void whenGetSurnamePurchasesAndNameShop_thenReturn200() throws Exception {

        Shop shop1 = new Shop("Дом книги", "Сормовский район", 20);
        Buyer buyer1 = new Buyer("Петров", "Нижегородский район", 20);
        Buyer buyer2 = new Buyer("Сидоров", "Нижегородский район", 15);
        shopRepository.save(shop1);
        buyerRepository.save(buyer1);
        buyerRepository.save(buyer2);
        Purchases purchases1 = new Purchases(Date.valueOf("2022-11-03"), shop1.getId(), buyer1.getId(), 1, 20, 2939);
        Purchases purchases2 = new Purchases(Date.valueOf("2022-10-11"), shop1.getId(), buyer2.getId(), 3, 15, 3000);

        repository.save(purchases1);
        repository.save(purchases2);
        mvc.perform(get("http://localhost:9090/purchase/surname"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(buyer1.getSurname()+","+shop1.getName()))
                .andExpect(jsonPath("$[1]").value(buyer2.getSurname()+","+shop1.getName()));
    }

    @Test
    public void whenGetDateSurnameDiscountPurchasesNameQuantityBook_thenReturn200() throws Exception {

        Book book1 = new Book("Гарри Поттер и философский камень", 2120, "Автозаводской район", 10);
        Book book2 = new Book("Гарри Поттер и тайная комната", 3210, "Сормовский район", 100);
        Buyer buyer1 = new Buyer("Петров", "Нижегородский район", 20);
        Buyer buyer2 = new Buyer("Сидоров", "Нижегородский район", 15);
        buyerRepository.save(buyer1);
        buyerRepository.save(buyer2);

        bookRepository.save(book1);
        bookRepository.save(book2);
        Purchases purchases1 = new Purchases(Date.valueOf("2022-11-03"), 1, buyer1.getId(), book2.getId(), 20, 2939);
        Purchases purchases2 = new Purchases(Date.valueOf("2022-10-11"), 1, buyer2.getId(), book1.getId(), 15, 3000);

        repository.save(purchases1);
        repository.save(purchases2);
        mvc.perform(get("http://localhost:9090/purchase/information"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(purchases1.getDate()+","+buyer1.getSurname()+","+buyer1.getDiscount()+","+book2.getName()+","+purchases1.getQuantity()))
                .andExpect(jsonPath("$[1]").value(purchases2.getDate()+","+buyer2.getSurname()+","+buyer2.getDiscount()+","+book1.getName()+","+purchases2.getQuantity()));
    }

    @Test
    public void whenGetNumberSurnamePurchasesDate_thenReturn200() throws Exception {
        Book book1 = new Book("Гарри Поттер и философский камень", 6030, "Автозаводской район", 10);
        bookRepository.save(book1);
        Buyer buyer1 = new Buyer("Петров", "Нижегородский район", 20);
        Buyer buyer2 = new Buyer("Сидоров", "Нижегородский район", 15);
        buyerRepository.save(buyer1);
        buyerRepository.save(buyer2);
        Purchases purchases1 = new Purchases(Date.valueOf("2022-11-03"), 1, buyer1.getId(), book1.getId(), 20, 2939);
        Purchases purchases2 = new Purchases(Date.valueOf("2022-10-11"), 1, buyer2.getId(), book1.getId(), 15, 60300);

        repository.save(purchases1);
        repository.save(purchases2);
        mvc.perform(get("http://localhost:9090/purchase/infoSum")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(60000)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(purchases2.getNumber()+","+buyer2.getSurname()+","+purchases2.getDate()));
    }

    @Test
    public void whenGetPurchasesNotEarlierThanMarch_thenReturn200() throws Exception {
        //в своем районе
        Shop shop1 = new Shop("Дом книги", "Сормовский район", 20);
        Buyer buyer1 = new Buyer("Петров", "Сормовский район", 20);
        Buyer buyer3 = new Buyer("Иванов", "Нижегородский район", 10);
        Buyer buyer2 = new Buyer("Сидоров", "Нижегородский район", 15);
        shopRepository.save(shop1);
        buyerRepository.save(buyer1);
        buyerRepository.save(buyer2);
        buyerRepository.save(buyer3);
        Purchases purchases1 = new Purchases(Date.valueOf("2022-11-03"), shop1.getId(), buyer1.getId(), 1, 20, 2939);
        //не пройдет по дате
        Purchases purchases2 = new Purchases(Date.valueOf("2022-02-11"), shop1.getId(), buyer2.getId(), 3, 15, 3000);
        // не совпадают районы
        Purchases purchases3 = new Purchases(Date.valueOf("2022-04-11"), shop1.getId(), buyer3.getId(), 1, 15, 3000);

        repository.save(purchases1);
        repository.save(purchases2);
        repository.save(purchases3);
        mvc.perform(get("http://localhost:9090/purchase/infoArea")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(3)
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(buyer1.getSurname()+","+buyer1.getArea()+","+purchases1.getDate()));
    }

    @Test
    public void whenGetShopsExceptADistrictDndBuyersWithDiscountFrom10To15_thenReturn200() throws Exception {
        //в своем районе
        Shop shop1 = new Shop("Дом книги", "Сормовский район", 20);
        Shop shop2 = new Shop("Дом книги", "Автозаводской район", 20);
        Buyer buyer1 = new Buyer("Петров", "Сормовский район", 20);
        Buyer buyer3 = new Buyer("Иванов", "Автозаводской район", 10);
        Buyer buyer2 = new Buyer("Сидоров", "Нижегородский район", 15);
        shopRepository.save(shop1);
        shopRepository.save(shop2);
        buyerRepository.save(buyer1);
        buyerRepository.save(buyer2);
        buyerRepository.save(buyer3);
        // не пройдет по скидке
        Purchases purchases1 = new Purchases(Date.valueOf("2022-11-03"), shop1.getId(), buyer1.getId(), 1, 20, 2939);
        // не пройдет по району магазина
        Purchases purchases2 = new Purchases(Date.valueOf("2022-02-11"), shop2.getId(), buyer2.getId(), 3, 15, 3000);
        Purchases purchases3 = new Purchases(Date.valueOf("2022-04-11"), shop1.getId(), buyer3.getId(), 1, 15, 3000);
        repository.save(purchases1);
        repository.save(purchases2);
        repository.save(purchases3);
        StringAndInteger stringAndInteger = new StringAndInteger("Автозаводской район",15);
        mvc.perform(get("http://localhost:9090/purchase/infoShops")
                        .contentType("application/json")
                        .content("Автозаводской район, 10, 15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(shop1.getName()));
    }

    @Test
    public void whenGetBooksInStorageAreaAndStockIsMoreThan10_thenReturn200() throws Exception {
        Book book1 = new Book("Гарри Поттер и философский камень", 6030, "Автозаводской район", 9);
        Book book2 = new Book("Гарри Поттер и тайная комната", 1030, "Нижегородский район", 20);
        Book book3 = new Book("Гарри Поттер и узник Азкабана", 1030, "Автозаводской район", 20);
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        Shop shop1 = new Shop("Дом книги", "Сормовский район", 20);
        Shop shop2 = new Shop("Читай город", "Нижегородский район", 20);
        Shop shop3 = new Shop("Читай город", "Автозаводской район", 20);
        shopRepository.save(shop1);
        shopRepository.save(shop2);
        shopRepository.save(shop3);
        // не пройдет по количеству книг на складе и району
        Purchases purchases1 = new Purchases(Date.valueOf("2022-11-03"), shop1.getId(), 1, book1.getId(), 20, 2939);
        Purchases purchases2 = new Purchases(Date.valueOf("2022-10-11"), shop2.getId(), 1, book2.getId(), 15, 5300);
        Purchases purchases3 = new Purchases(Date.valueOf("2022-10-11"), shop3.getId(), 1, book3.getId(), 15, 2300);
        repository.save(purchases1);
        repository.save(purchases2);
        repository.save(purchases3);

        mvc.perform(get("http://localhost:9090/purchase/infoPurchBook")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(10)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value(book3.getName()+","+book3.getWarehouse()+","+purchases3.getQuantity()+","+purchases3.getSum()))
                .andExpect(jsonPath("$[1]").value(book2.getName()+","+book2.getWarehouse()+","+purchases2.getQuantity()+","+purchases2.getSum()));
    }
}
