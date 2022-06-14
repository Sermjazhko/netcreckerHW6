package com.netcrecker.model;

import javax.persistence.criteria.CriteriaBuilder;

public class StringAndInteger {
    private  String string;
    private Integer integer;

    public StringAndInteger(String string, Integer integer) {
        this.string = string;
        this.integer = integer;
    }



    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }
}
