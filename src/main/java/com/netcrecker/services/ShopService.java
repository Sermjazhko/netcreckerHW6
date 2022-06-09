package com.netcrecker.services;

import com.netcrecker.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopService extends JpaRepository<Shop,Integer> {
    @Query(value = "select name from shop where area = 'Советский район' or  area = 'Сормовский район'", nativeQuery = true)
    List<String> getName();
}
