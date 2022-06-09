package com.netcrecker.services;

import com.netcrecker.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerService extends JpaRepository<Buyer,Integer> {
}
