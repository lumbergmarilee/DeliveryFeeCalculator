package com.lumbergmarilee.delivery_fee_calculator.service;

import com.lumbergmarilee.delivery_fee_calculator.exception.VehicleUsageForbiddenException;
import com.lumbergmarilee.delivery_fee_calculator.model.WeatherData;
import com.lumbergmarilee.delivery_fee_calculator.model.enums.City;
import com.lumbergmarilee.delivery_fee_calculator.model.enums.VehicleType;
import com.lumbergmarilee.delivery_fee_calculator.repository.WeatherDataRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryFeeService {

    private final WeatherDataRepository weatherDataRepository;

    public DeliveryFeeService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    private double calculateRBF(City city, VehicleType vehicleType){
        return switch (city){
            case TALLINN -> switch(vehicleType){
                case CAR -> 4.0;
                case SCOOTER -> 3.5;
                case BIKE -> 3.0;
            };
            case TARTU -> switch(vehicleType){
                case CAR -> 3.5;
                case SCOOTER -> 3.0;
                case BIKE -> 2.5;
            };
            case PÄRNU -> switch(vehicleType){
                case CAR -> 3.0;
                case SCOOTER -> 2.5;
                case BIKE -> 2.0;
            };

        };
    }

    private double calculateATEF(VehicleType vehicleType, double airTemperature){
        if (vehicleType == VehicleType.CAR){ return 0;}
        // Otherwise must be type bike or scooter
        if (airTemperature < -10){return 1.0;}
        if (airTemperature >= -10 && airTemperature <= 0){return 0.5;}

        return 0.0;
    }

    private double calculateWPEF(VehicleType vehicleType, String weatherPhenomenon){
        if (vehicleType == VehicleType.CAR){ return 0.0;}
        if (weatherPhenomenon == null || weatherPhenomenon.isEmpty()){return 0.0;}
        weatherPhenomenon = weatherPhenomenon.trim().toLowerCase();

        if (weatherPhenomenon.contains("hail") || weatherPhenomenon.contains("glaze") || weatherPhenomenon.contains("thunder")){
            throw new VehicleUsageForbiddenException("Usage of selected vehicle type is forbidden due to wheather phenomenon "+weatherPhenomenon);
        }
        if (weatherPhenomenon.contains("snow") || weatherPhenomenon.contains("sleet")){
            return 1.0;
        }
        if (weatherPhenomenon.contains("rain")){
            return 0.5;
        }
        return 0.0;
    }


    private double calculateWSEF(VehicleType vehicleType, double windSpeed){
        if (vehicleType == VehicleType.CAR || vehicleType == VehicleType.SCOOTER){ return 0;}
        if (windSpeed > 20){throw new VehicleUsageForbiddenException("Usage of selected vehicle type is forbidden due to wind speeds");}

        if (windSpeed > 10) return 0.5;
        return 0;

    }

    public double calculateDeliveryFee(City city, VehicleType vehicleType, double airTemperature, double windSpeed, String weatherPhenomenon) {

        double rbf = calculateRBF(city, vehicleType);

        double atef = calculateATEF(vehicleType, airTemperature);

        double wsef = calculateWSEF(vehicleType, windSpeed);

        double wpef = calculateWPEF(vehicleType, weatherPhenomenon);

        return rbf+atef+wsef+wpef;
    }

    public double calculateFee(City city, VehicleType vehicleType){
        String cityName = city.getName();
        WeatherData weatherData = weatherDataRepository.findTopByStationNameOrderByTimestampDesc(cityName);

        if (weatherData == null){
            throw new RuntimeException("No weather data for "+cityName);
        }

        return calculateDeliveryFee(city, vehicleType,
                weatherData.getAirTemperature(),
                weatherData.getWindSpeed(),
                weatherData.getWeatherPhenomenon());
    }
}
