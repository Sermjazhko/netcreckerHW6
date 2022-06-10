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

    @Query(value = "select t1.surname, t2.name from purchase as t3 inner join buyer as t1 on t3.buyer = t1.id inner join shop as t2 on t3.seller = t2.id", nativeQuery = true)
    List<String> getSurnameName();

    @Query(value = "select t3.date, t1.surname, t1.discount, t2.name, t3.quantity from purchase as t3 inner join buyer as t1 on t3.buyer = t1.id inner join book as t2 on t3.book = t2.id", nativeQuery = true)
    List<String> getInfoBookAndBuyer();

    @Query(value = "select t3.number, t1.surname, t3.date  from purchase as t3 inner join buyer as t1 on t3.buyer = t1.id inner join book as t2 on t3.book = t2.id where t3.sum>60000", nativeQuery = true)
    List<String> getInfoNumberSum();

    @Query(value = "select t1.surname, t1.area ,t3.date from purchase as t3 inner join buyer as t1 on t3.buyer = t1.id inner join shop as t2 on t3.seller = t2.id where t2.area = t1.area and to_char(t3.date, 'MM') > to_char(make_date(2000,02,01), 'MM') order by t3.date", nativeQuery = true)
    List<String> getInfoSurnameAndArea();

    @Query(value = "select t2.name, t2.area from purchase as t3 inner join buyer as t1 on t3.buyer = t1.id inner join shop as t2 on t3.seller = t2.id where t2.area != 'Автозаводской район' and t1.discount>=10 and t1.discount <= 15", nativeQuery = true)
    List<String> getInfoShops();

    @Query(value = "select t1.name, t1.warehouse, t3.quantity, t3.sum from purchase as t3 inner join book as t1 on t3.book = t1.id inner join shop as t2 on t3.seller = t2.id where t2.area = t1.warehouse and t1.quantity>=10 order by t3.sum", nativeQuery = true)
    List<String> getInfoBookAndShop();
}
