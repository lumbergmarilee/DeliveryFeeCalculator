package com.lumbergmarilee.delivery_fee_calculator.model.enums;

public enum City {
    TALLINN("Tallinn-Harku"),
    TARTU("Tartu-Tõravere"),
    PÄRNU("Pärnu");

    private final String name;

    City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
