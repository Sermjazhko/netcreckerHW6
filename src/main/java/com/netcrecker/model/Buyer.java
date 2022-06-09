package com.netcrecker.model;

import javax.persistence.*;

@Entity
@Table(name = "buyer")
public class Buyer {
    @Id // первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // указывает, что свойство будет создаваться согласно указанной стратегии
    private Integer id;
    private String surname;
    private String area;
    private Integer discount;

    public Buyer(){}

    public Buyer(String surname, String area, Integer discount) {
        this.surname = surname;
        this.area = area;
        this.discount = discount;
    }

    public Integer getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
