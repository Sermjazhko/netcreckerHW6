package com.netcrecker.services;

import com.netcrecker.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Integer> {
    @Query(value = "select name from shop s where s.area = :area1 or  s.area = :area2", nativeQuery = true)
    List<String> getName(@Param("area1") String area1, @Param("area2") String area2);
}
