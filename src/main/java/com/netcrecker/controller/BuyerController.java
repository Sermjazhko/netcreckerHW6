package com.netcrecker.controller;

import com.netcrecker.model.Buyer;
import com.netcrecker.services.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/buyer")
public class BuyerController {
    private final BuyerRepository buyerRepository;

    @Autowired
    public BuyerController(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    @GetMapping("/buyers")
    public List<Buyer> getAllBuyers() {
        return buyerRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBuyer(@PathVariable(value = "id") Integer id){
        try {
            Optional<Buyer> buyer = buyerRepository.findById(id);
            if (buyer.isPresent()) {
                buyerRepository.delete(buyer.get());
            }
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/buyers")
    public ResponseEntity<Buyer> saveBuyer(@RequestBody Buyer buyer){
        try {
            return new ResponseEntity<>(buyerRepository.save(buyer), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Buyer> updateId( @RequestBody Buyer buyer){
        try {
            return new ResponseEntity<Buyer>(buyerRepository.save(buyer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/buyer/{id}")
    public ResponseEntity<Buyer> getBuyer(@PathVariable(value = "id") Integer id) {
        try {
            Buyer buyer = buyerRepository.findById(id).get();
            return new ResponseEntity<Buyer>(buyer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/update/{id}/{discount}")
    public ResponseEntity<Buyer> updateId(@PathVariable(value = "id") Integer id, @PathVariable Integer discount){
        try {
            Buyer book = buyerRepository.findById(id).get();
            book.setDiscount(discount);
            return new ResponseEntity<Buyer>(buyerRepository.save(book), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/various")
    public List<String> getVarious() {
        return buyerRepository.getVariousArea();
    }

    @GetMapping("/district")
    public List<String> getSurname(@RequestBody String district) {
        return buyerRepository.getSurname(district);
    }

}
