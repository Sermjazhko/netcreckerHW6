package com.netcrecker.controller;

import com.netcrecker.model.Book;
import com.netcrecker.model.Buyer;
import com.netcrecker.model.Purchases;
import com.netcrecker.model.Shop;
import com.netcrecker.services.BookService;
import com.netcrecker.services.BuyerService;
import com.netcrecker.services.PurchasesService;
import com.netcrecker.services.ShopService;
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
    private final PurchasesService purchasesService;
    private final BookService bookService;
    private final BuyerService buyerService;
    private final ShopService shopService;

    @Autowired
    public PurchasesController(PurchasesService purchasesService, BookService bookService, BuyerService buyerService, ShopService shopService) {
        this.purchasesService = purchasesService;
        this.bookService = bookService;
        this.buyerService = buyerService;
        this.shopService = shopService;
    }

    @GetMapping("/purchases")
    public List<Purchases> getAllPurchases() {
        return purchasesService.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deletePurchase(@PathVariable(value = "id") Integer id){
        try {
            Optional<Purchases> purchases = purchasesService.findById(id);
            if (purchases.isPresent()) {
                purchasesService.delete(purchases.get());
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
            Book book = bookService.findById(idBook).get();
            Buyer buyer = buyerService.findById(idBuyer).get();
            Shop shop = shopService.findById(idShop).get();
            Integer sum = (int) ((quantity*book.getPrice()*(1+0.01*shop.getCommission()))*(1-0.01*buyer.getDiscount()));
            Purchases purchases1 = new Purchases(date, idShop, idBuyer, idBook, quantity, sum);
            return new ResponseEntity<>(purchasesService.save(purchases1), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Purchases> updateId( @RequestBody Purchases purchases){
        try {
            return new ResponseEntity<Purchases>(purchasesService.save(purchases), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/purchases/{id}")
    public ResponseEntity<Purchases> getPurchase(@PathVariable(value = "id") Integer id) {
        try {
            Purchases buyer = purchasesService.findById(id).get();
            return new ResponseEntity<Purchases>(buyer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/update/{id}/{idBook}")
    public ResponseEntity<Purchases> updateId(@PathVariable(value = "id") Integer id, @PathVariable Integer idBook){
        try {
            Purchases purchases = purchasesService.findById(id).get();
            purchases.setBook(idBook);
            return new ResponseEntity<Purchases>(purchasesService.save(purchases), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/various")
    public List<String> getVarious() {
        return purchasesService.getVariousMonth();
    }

    @GetMapping("/surname")
    public List<String> getSurnameName() {
        return purchasesService.getSurnameName();
    }

    @GetMapping("/information")
    public List<String> getInfoBookAndName() {
        return purchasesService.getInfoBookAndBuyer();
    }

    @GetMapping("/infoSum")
    public List<String> getInfoSum() {
        return purchasesService.getInfoNumberSum();
    }

    @GetMapping("/infoArea")
    public List<String> getInfoArea() {
        return purchasesService.getInfoSurnameAndArea();
    }

    @GetMapping("/infoShops")
    public List<String> getInfoShops() {
        return purchasesService.getInfoShops();
    }

    @GetMapping("/infoPurchBook")
    public List<String> getInfoPurchBook() {
        return purchasesService.getInfoBookAndShop();
    }
}
