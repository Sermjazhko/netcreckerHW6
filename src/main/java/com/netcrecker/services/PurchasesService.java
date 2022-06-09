package com.netcrecker.services;

import com.netcrecker.model.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasesService extends JpaRepository<Purchases,Integer> {
}
