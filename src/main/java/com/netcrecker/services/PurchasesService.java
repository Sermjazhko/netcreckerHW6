package com.netcrecker.services;

import com.netcrecker.model.Buyer;
import com.netcrecker.model.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasesService extends JpaRepository<Purchases,Integer> {
      @Query(value = "select distinct to_char(purchase.date, 'MM') from purchase", nativeQuery = true)
      List<String> getVariousMonth();

    }
