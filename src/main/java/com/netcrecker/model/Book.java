package com.netcrecker.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "book")
@Data
public class Book {
    @Id // первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // указывает, что свойство будет создаваться согласно указанной стратегии
    private Integer id;

    private String name;
    private Integer price;
    private Integer warehouse;
    private Integer quantity;

    public Book(){}

    public Book(String name, Integer price, Integer warehouse, Integer quantity) {
        this.name = name;
        this.price = price;
        this.warehouse = warehouse;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Integer warehouse) {
        this.warehouse = warehouse;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", warehouse=" + warehouse +
                ", quantity=" + quantity +
                '}';
    }
}
