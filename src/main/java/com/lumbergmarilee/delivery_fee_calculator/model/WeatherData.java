package com.lumbergmarilee.delivery_fee_calculator.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the weather station (e.g., "Tallinn-Harku", "Tartu-Tõravere", "Pärnu") */
    private String stationName;

    /** WMO (World Meteorological Organization) station code */
    private String wmoCode;

    /** Air temperature in degrees Celsius */
    private double airTemperature;

    /** Wind speed in meters per second (m/s) */
    private double windSpeed;

    /** Weather phenomenon description (e.g., "Light snow shower", "Moderate rain") */
    private String weatherPhenomenon;

    /** Timestamp of the weather observation. Mapped to avoid H2 reserved word conflict. */
    @Column(name = "observation_timestamp")
    private LocalDateTime timestamp;

    public WeatherData() {}

    public WeatherData(String stationName, String wmoCode, double airTemperature, double windSpeed, String weatherPhenomenon, LocalDateTime timestamp) {
        this.stationName = stationName;
        this.wmoCode = wmoCode;
        this.airTemperature = airTemperature;
        this.windSpeed = windSpeed;
        this.weatherPhenomenon = weatherPhenomenon;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getWmoCode() {
        return wmoCode;
    }

    public void setWmoCode(String wmoCode) {
        this.wmoCode = wmoCode;
    }

    public double getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWeatherPhenomenon() {
        return weatherPhenomenon;
    }

    public void setWeatherPhenomenon(String weatherPhenomenon) {
        this.weatherPhenomenon = weatherPhenomenon;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

