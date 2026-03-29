package com.lumbergmarilee.delivery_fee_calculator.repository;

import com.lumbergmarilee.delivery_fee_calculator.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    WeatherData findTopByStationNameOrderByTimestampDesc(String stationName);
}