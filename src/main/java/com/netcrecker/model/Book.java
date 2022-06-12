package com.netcrecker.model;

import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book {
    @Id // первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // указывает, что свойство будет создаваться согласно указанной стратегии
    private Integer id;

    private String name;
    private Integer price;
    private String warehouse;
    private Integer quantity;

    public Book(){}

    public Book(String name, Integer price, String warehouse, Integer quantity) {
        this.name = name;
        this.price = price;
        this.warehouse = warehouse;
        this.quantity = quantity;
    }
    public Book(Integer id, String name, Integer price, String warehouse, Integer quantity) {
        this.id = id;
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

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
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
