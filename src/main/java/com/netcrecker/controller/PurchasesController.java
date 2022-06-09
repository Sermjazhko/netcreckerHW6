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

import java.util.Date;
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
            Optional<Purchases> shop = purchasesService.findById(id);
            if (shop.isPresent()) {
                purchasesService.delete(shop.get());
            }
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/purchases/{idShop}/{idBuyer}/{idBook}")
    public ResponseEntity<Purchases> savePurchase( @PathVariable Integer idShop, @PathVariable Integer idBuyer, @PathVariable Integer idBook, @RequestBody Integer quantity){
        try {
            Date data = new Date();
            Book book = bookService.findById(idBook).get();
            Buyer buyer = buyerService.findById(idBuyer).get();
            Shop shop = shopService.findById(idShop).get();
            Integer sum = (int) ((quantity*book.getPrice()*(1+0.01*shop.getCommission()))*(1-0.01*buyer.getDiscount()));
            Purchases purchases = new Purchases(data, idShop, idBuyer, idBook, quantity, sum);
            return new ResponseEntity<>(purchasesService.save(purchases), HttpStatus.CREATED);
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

}
