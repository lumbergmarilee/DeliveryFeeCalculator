package com.lumbergmarilee.delivery_fee_calculator.repository;

import com.lumbergmarilee.delivery_fee_calculator.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for accessing weather data in the H2 database.
 * Spring Data JPA automatically provides CRUD (Create, Update, Read, Delete) operations.
 */
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    /**
     * Finds the most recent weather observation for a given station.
     * Used to get the latest weather data for delivery fee calculations.
     *
     * @param stationName the weather station name (e.g., "Tallinn-Harku")
     * @return the most recent WeatherData entry, or null if none exists
     */
    WeatherData findTopByStationNameOrderByTimestampDesc(String stationName);
}