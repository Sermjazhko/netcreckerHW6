package com.netcrecker.services;

import com.netcrecker.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopService extends JpaRepository<Shop,Integer> {
}
