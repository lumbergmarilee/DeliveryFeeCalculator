package com.lumbergmarilee.delivery_fee_calculator.model.enums;


/**
 * Enum representing the cities supported by the delivery fee calculator.
 * Each city is mapped to its corresponding weather station name used by the Estonian Weather Service.
 */
public enum City {
    TALLINN("Tallinn-Harku"),
    TARTU("Tartu-Tõravere"),
    PÄRNU("Pärnu");

    /** Weather station name used to fetch weather data for this city */
    private final String name;

    City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
