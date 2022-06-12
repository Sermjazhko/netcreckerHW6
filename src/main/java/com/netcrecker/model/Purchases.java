package com.netcrecker.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "purchase")
public class Purchases {
    @Id // первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // указывает, что свойство будет создаваться согласно указанной стратегии
    private Integer number;
    private Date date;
    private Integer seller;
    private Integer buyer;
    private Integer book;
    private Integer quantity;
    private Integer sum;

    public Purchases(){}

    public Purchases(Date date, Integer seller, Integer buyer, Integer book, Integer quantity, Integer sum) {
        this.date = date;
        this.seller = seller;
        this.buyer = buyer;
        this.book = book;
        this.quantity = quantity;
        this.sum = sum;
    }


    public Integer getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getSeller() {
        return seller;
    }

    public void setSeller(Integer seller) {
        this.seller = seller;
    }

    public Integer getBuyer() {
        return buyer;
    }

    public void setBuyer(Integer buyer) {
        this.buyer = buyer;
    }

    public Integer getBook() {
        return book;
    }

    public void setBook(Integer book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }
}
