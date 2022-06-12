package com.netcrecker.controller;

import com.netcrecker.model.Book;
import com.netcrecker.model.Buyer;
import com.netcrecker.model.Purchases;
import com.netcrecker.model.Shop;
import com.netcrecker.services.BookRepository;
import com.netcrecker.services.BuyerRepository;
import com.netcrecker.services.PurchasesRepository;
import com.netcrecker.services.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/purchase")
public class PurchasesController {
    private final PurchasesRepository purchasesRepository;
    private final BookRepository bookRepository;
    private final BuyerRepository buyerRepository;
    private final ShopRepository shopRepository;

    @Autowired
    public PurchasesController(PurchasesRepository purchasesRepository, BookRepository bookRepository, BuyerRepository buyerRepository, ShopRepository shopRepository) {
        this.purchasesRepository = purchasesRepository;
        this.bookRepository = bookRepository;
        this.buyerRepository = buyerRepository;
        this.shopRepository = shopRepository;
    }

    @GetMapping("/purchases")
    public List<Purchases> getAllPurchases() {
        return purchasesRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deletePurchase(@PathVariable(value = "id") Integer id){
        try {
            Optional<Purchases> purchases = purchasesRepository.findById(id);
            if (purchases.isPresent()) {
                purchasesRepository.delete(purchases.get());
            }
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/purchases/{idShop}/{idBuyer}/{idBook}")
    public ResponseEntity<Purchases> savePurchase(@PathVariable Integer idShop, @PathVariable Integer idBuyer, @PathVariable Integer idBook,  @RequestBody Purchases purchases){
        try {
            // заполняем только дату и количество
            Integer quantity = purchases.getQuantity();
            Date date = purchases.getDate();
            Book book = bookRepository.findById(idBook).get();
            Buyer buyer = buyerRepository.findById(idBuyer).get();
            Shop shop = shopRepository.findById(idShop).get();
            Integer sum = (int) ((quantity*book.getPrice()*(1+0.01*shop.getCommission()))*(1-0.01*buyer.getDiscount()));
            Purchases purchases1 = new Purchases(date, idShop, idBuyer, idBook, quantity, sum);
            return new ResponseEntity<>(purchasesRepository.save(purchases1), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Purchases> updateId( @RequestBody Purchases purchases){
        try {
            return new ResponseEntity<Purchases>(purchasesRepository.save(purchases), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/purchases/{id}")
    public ResponseEntity<Purchases> getPurchase(@PathVariable(value = "id") Integer id) {
        try {
            Purchases buyer = purchasesRepository.findById(id).get();
            return new ResponseEntity<Purchases>(buyer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/update/{id}/{idBook}")
    public ResponseEntity<Purchases> updateId(@PathVariable(value = "id") Integer id, @PathVariable Integer idBook){
        try {
            Purchases purchases = purchasesRepository.findById(id).get();
            purchases.setBook(idBook);
            return new ResponseEntity<Purchases>(purchasesRepository.save(purchases), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/various")
    public List<String> getVarious() {
        return purchasesRepository.getVariousMonth();
    }

    @GetMapping("/surname")
    public List<String> getSurnameName() {
        return purchasesRepository.getSurnameName();
    }

    @GetMapping("/information")
    public List<String> getInfoBookAndName() {
        return purchasesRepository.getInfoBookAndBuyer();
    }

    @GetMapping("/infoSum")
    public List<String> getInfoSum() {
        return purchasesRepository.getInfoNumberSum();
    }

    @GetMapping("/infoArea")
    public List<String> getInfoArea() {
        return purchasesRepository.getInfoSurnameAndArea();
    }

    @GetMapping("/infoShops")
    public List<String> getInfoShops() {
        return purchasesRepository.getInfoShops();
    }

    @GetMapping("/infoPurchBook")
    public List<String> getInfoPurchBook() {
        return purchasesRepository.getInfoBookAndShop();
    }
}
