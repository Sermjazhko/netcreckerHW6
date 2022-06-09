package com.netcrecker.model;

import javax.persistence.*;

@Entity
@Table(name = "shop")
public class Shop {
    @Id // первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // указывает, что свойство будет создаваться согласно указанной стратегии
    private Integer id;
    private String name;
    private String area;
    private Integer commission;

    public Shop(){}

    public Shop(String name, String area, Integer commission) {
        this.name = name;
        this.area = area;
        this.commission = commission;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getCommission() {
        return commission;
    }

    public void setCommission(Integer commission) {
        this.commission = commission;
    }
}
