package com.netcrecker.controller;

import com.netcrecker.model.Shop;
import com.netcrecker.model.StringAndInteger;
import com.netcrecker.services.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shop")
public class ShopController {
    private final ShopRepository shopRepository;

    @Autowired
    public ShopController(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @GetMapping("/shops")
    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteShop(@PathVariable(value = "id") Integer id){
        try {
            Optional<Shop> shop = shopRepository.findById(id);
            if (shop.isPresent()) {
                shopRepository.delete(shop.get());
            }
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/shops")
    public ResponseEntity<Shop> saveShop(@RequestBody Shop shop){
        try {
            return new ResponseEntity<>(shopRepository.save(shop), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Shop> updateId( @RequestBody Shop shop){
        try {
            return new ResponseEntity<Shop>(shopRepository.save(shop), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/shop/{id}")
    public ResponseEntity<Shop> getShop(@PathVariable(value = "id") Integer id) {
        try {
            Shop buyer = shopRepository.findById(id).get();
            return new ResponseEntity<Shop>(buyer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/update/{id}/{commission}")
    public ResponseEntity<Shop> updateId(@PathVariable(value = "id") Integer id, @PathVariable Integer commission){
        try {
            Shop shop = shopRepository.findById(id).get();
            shop.setCommission(commission);
            return new ResponseEntity<Shop>(shopRepository.save(shop), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/name")
    public List<String> getVarious(@RequestBody String area) {
        String[] areas = area.split(", ");
        return shopRepository.getName(areas[0],areas[1]);
    }

}
