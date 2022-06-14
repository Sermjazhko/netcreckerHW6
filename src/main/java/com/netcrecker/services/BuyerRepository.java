package com.netcrecker.services;

import com.netcrecker.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer,Integer> {
    @Query(value = "select distinct area from buyer", nativeQuery = true)
    List<String> getVariousArea();

    @Query(value = "select surname, discount from buyer  b where b.area= :area ", nativeQuery = true)
    List<String> getSurname(@Param("area") String area);

}
