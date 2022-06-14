package com.netcrecker.services;

import com.netcrecker.model.Buyer;
import com.netcrecker.model.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PurchasesRepository extends JpaRepository<Purchases,Integer> {
    @Query(value = "select distinct to_char(purchase.date, 'MM') from purchase", nativeQuery = true)
    List<String> getVariousMonth();

    @Query(value = "select t1.surname, t2.name from purchase as t3 inner join buyer as t1 on t3.buyer = t1.id inner join shop as t2 on t3.seller = t2.id", nativeQuery = true)
    List<String> getSurnameName();

    @Query(value = "select t3.date, t1.surname, t1.discount, t2.name, t3.quantity from purchase as t3 inner join buyer as t1 on t3.buyer = t1.id inner join book as t2 on t3.book = t2.id", nativeQuery = true)
    List<String> getInfoBookAndBuyer();

    @Query(value = "select t3.number, t1.surname, t3.date  from purchase as t3 inner join buyer as t1 on t3.buyer = t1.id inner join book as t2 on t3.book = t2.id where t3.sum> :sum", nativeQuery = true)
    List<String> getInfoNumberSum(@Param("sum") Integer sum);

    @Query(value = "select t1.surname, t1.area ,t3.date from purchase as t3 inner join buyer as t1 on t3.buyer = t1.id inner join shop as t2 on t3.seller = t2.id where t2.area = t1.area and to_char(t3.date, 'MM') > to_char(make_date(2000,:datem,01), 'MM') order by t3.date", nativeQuery = true)
    List<String> getInfoSurnameAndArea(@Param("datem") Integer datem);

    @Query(value = "select t2.name from purchase as t3 inner join buyer as t1 on t3.buyer = t1.id inner join shop as t2 on t3.seller = t2.id where t2.area != :area and t1.discount>= :discount1 and t1.discount <= :discount2", nativeQuery = true)
    List<String> getInfoShops(@Param("area") String area, @Param("discount1") Integer discount1, @Param("discount2") Integer discount2);

    @Query(value = "select t1.name, t1.warehouse, t3.quantity, t3.sum from purchase as t3 inner join book as t1 on t3.book = t1.id inner join shop as t2 on t3.seller = t2.id where t2.area = t1.warehouse and t1.quantity>= :quantity order by t3.sum", nativeQuery = true)
    List<String> getInfoBookAndShop(@Param("quantity") Integer quantity);
}
