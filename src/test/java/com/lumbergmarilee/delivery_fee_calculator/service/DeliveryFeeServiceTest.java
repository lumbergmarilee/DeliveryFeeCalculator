package com.lumbergmarilee.delivery_fee_calculator.service;

import com.lumbergmarilee.delivery_fee_calculator.exception.VehicleUsageForbiddenException;
import com.lumbergmarilee.delivery_fee_calculator.model.enums.City;
import com.lumbergmarilee.delivery_fee_calculator.model.enums.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


// Claude Opus 4.6 was used to create tests
class DeliveryFeeServiceTest {

    private DeliveryFeeService service;

    @BeforeEach
    void setUp() {
        service = new DeliveryFeeService();
    }

    // ========================
    // EXAMPLE FROM TASK
    // ========================

    @Test
    void testTaskExample_TartuBikeLightSnowShower() {
        // Tartu + Bike: RBF=2.5, temp=-2.1 -> ATEF=0.5, wind=4.7 -> WSEF=0, "Light snow shower" -> WPEF=1.0
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.BIKE, -2.1, 4.7, "Light snow shower");
        assertEquals(4.0, result);
    }

    // ========================
    // REGIONAL BASE FEE (RBF)
    // ========================

    @Test
    void testRBF_TallinnCar() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.CAR, 15.0, 5.0, "");
        assertEquals(4.0, result);
    }

    @Test
    void testRBF_TallinnScooter() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.SCOOTER, 15.0, 5.0, "");
        assertEquals(3.5, result);
    }

    @Test
    void testRBF_TallinnBike() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.BIKE, 15.0, 5.0, "");
        assertEquals(3.0, result);
    }

    @Test
    void testRBF_TartuCar() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.CAR, 15.0, 5.0, "");
        assertEquals(3.5, result);
    }

    @Test
    void testRBF_TartuScooter() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.SCOOTER, 15.0, 5.0, "");
        assertEquals(3.0, result);
    }

    @Test
    void testRBF_TartuBike() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.BIKE, 15.0, 5.0, "");
        assertEquals(2.5, result);
    }

    @Test
    void testRBF_ParnuCar() {
        double result = service.calculateDeliveryFee(City.PÄRNU, VehicleType.CAR, 15.0, 5.0, "");
        assertEquals(3.0, result);
    }

    @Test
    void testRBF_ParnuScooter() {
        double result = service.calculateDeliveryFee(City.PÄRNU, VehicleType.SCOOTER, 15.0, 5.0, "");
        assertEquals(2.5, result);
    }

    @Test
    void testRBF_ParnuBike() {
        double result = service.calculateDeliveryFee(City.PÄRNU, VehicleType.BIKE, 15.0, 5.0, "");
        assertEquals(2.0, result);
    }

    // ========================
    // CAR IGNORES ALL WEATHER
    // ========================

    @Test
    void testCar_IgnoresExtremeWeather() {
        // Car should only pay RBF regardless of terrible weather
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.CAR, -15.0, 25.0, "Heavy snow shower");
        assertEquals(4.0, result);
    }

    @Test
    void testCar_IgnoresRain() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.CAR, 5.0, 8.0, "Moderate rain");
        assertEquals(3.5, result);
    }

    @Test
    void testCar_IgnoresGlaze() {
        // Car should NOT throw exception for glaze/hail/thunder
        double result = service.calculateDeliveryFee(City.PÄRNU, VehicleType.CAR, -5.0, 15.0, "Glaze");
        assertEquals(3.0, result);
    }

    @Test
    void testCar_IgnoresThunder() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.CAR, 10.0, 5.0, "Thunder");
        assertEquals(4.0, result);
    }

    // ========================
    // AIR TEMPERATURE EXTRA FEE (ATEF)
    // ========================

    // Edge: exactly -10 should be 0.5, not 1.0 (task says "less than -10")
    @Test
    void testATEF_ExactlyMinusTen_Scooter() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.SCOOTER, -10.0, 5.0, "");
        assertEquals(4.0, result); // RBF 3.5 + ATEF 0.5
    }

    @Test
    void testATEF_ExactlyMinusTen_Bike() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.BIKE, -10.0, 5.0, "");
        assertEquals(3.5, result); // RBF 3.0 + ATEF 0.5
    }

    // Edge: exactly 0 should be 0.5
    @Test
    void testATEF_ExactlyZero_Scooter() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.SCOOTER, 0.0, 5.0, "");
        assertEquals(3.5, result); // RBF 3.0 + ATEF 0.5
    }

    // Edge: just above 0 should be 0
    @Test
    void testATEF_JustAboveZero_Bike() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.BIKE, 0.1, 5.0, "");
        assertEquals(2.5, result); // RBF 2.5 + ATEF 0
    }

    // Middle: -5 is between -10 and 0
    @Test
    void testATEF_MiddleRange_Scooter() {
        double result = service.calculateDeliveryFee(City.PÄRNU, VehicleType.SCOOTER, -5.0, 5.0, "");
        assertEquals(3.0, result); // RBF 2.5 + ATEF 0.5
    }

    // Below -10
    @Test
    void testATEF_BelowMinusTen_Bike() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.BIKE, -15.0, 5.0, "");
        assertEquals(4.0, result); // RBF 3.0 + ATEF 1.0
    }

    @Test
    void testATEF_BelowMinusTen_Scooter() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.SCOOTER, -11.0, 5.0, "");
        assertEquals(4.5, result); // RBF 3.5 + ATEF 1.0
    }

    // Warm temperature, no fee
    @Test
    void testATEF_WarmWeather_Bike() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.BIKE, 20.0, 5.0, "");
        assertEquals(2.5, result); // RBF 2.5 + ATEF 0
    }

    // ========================
    // WIND SPEED EXTRA FEE (WSEF)
    // ========================

    // Edge: exactly 10 should be 0 (task says "between 10 and 20")
    @Test
    void testWSEF_ExactlyTen_Bike() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.BIKE, 15.0, 10.0, "");
        assertEquals(3.0, result); // RBF 3.0 + WSEF 0
    }

    // Edge: just above 10
    @Test
    void testWSEF_JustAboveTen_Bike() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.BIKE, 15.0, 10.1, "");
        assertEquals(3.5, result); // RBF 3.0 + WSEF 0.5
    }

    // Middle: 15 m/s
    @Test
    void testWSEF_MiddleRange_Bike() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.BIKE, 15.0, 15.0, "");
        assertEquals(3.0, result); // RBF 2.5 + WSEF 0.5
    }

    // Edge: exactly 20 should be 0.5 (task says "greater than 20" is forbidden)
    @Test
    void testWSEF_ExactlyTwenty_Bike() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.BIKE, 15.0, 20.0, "");
        assertEquals(3.5, result); // RBF 3.0 + WSEF 0.5
    }

    // Edge: just above 20 should throw exception
    @Test
    void testWSEF_AboveTwenty_Bike_ThrowsException() {
        assertThrows(VehicleUsageForbiddenException.class, () ->
                service.calculateDeliveryFee(City.TALLINN, VehicleType.BIKE, 15.0, 20.1, "")
        );
    }

    // High wind but scooter — should NOT throw, WSEF only applies to bike
    @Test
    void testWSEF_HighWind_Scooter_NoException() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.SCOOTER, 15.0, 25.0, "");
        assertEquals(3.5, result); // RBF 3.5 + WSEF 0
    }

    // Low wind, no fee
    @Test
    void testWSEF_LowWind_Bike() {
        double result = service.calculateDeliveryFee(City.PÄRNU, VehicleType.BIKE, 15.0, 5.0, "");
        assertEquals(2.0, result); // RBF 2.0 + WSEF 0
    }

    // ========================
    // WEATHER PHENOMENON EXTRA FEE (WPEF)
    // ========================

    // Snow variations
    @Test
    void testWPEF_Snow_Scooter() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.SCOOTER, 15.0, 5.0, "Light snow");
        assertEquals(4.5, result); // RBF 3.5 + WPEF 1.0
    }

    @Test
    void testWPEF_HeavySnowShower_Bike() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.BIKE, 15.0, 5.0, "Heavy snow shower");
        assertEquals(3.5, result); // RBF 2.5 + WPEF 1.0
    }

    // Sleet
    @Test
    void testWPEF_Sleet_Scooter() {
        double result = service.calculateDeliveryFee(City.PÄRNU, VehicleType.SCOOTER, 15.0, 5.0, "Light sleet");
        assertEquals(3.5, result); // RBF 2.5 + WPEF 1.0
    }

    // Rain variations
    @Test
    void testWPEF_Rain_Bike() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.BIKE, 15.0, 5.0, "Light rain");
        assertEquals(3.5, result); // RBF 3.0 + WPEF 0.5
    }

    @Test
    void testWPEF_ModerateRain_Scooter() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.SCOOTER, 15.0, 5.0, "Moderate rain");
        assertEquals(3.5, result); // RBF 3.0 + WPEF 0.5
    }

    // Forbidden phenomena — Scooter
    @Test
    void testWPEF_Glaze_Scooter_ThrowsException() {
        assertThrows(VehicleUsageForbiddenException.class, () ->
                service.calculateDeliveryFee(City.TALLINN, VehicleType.SCOOTER, 15.0, 5.0, "Glaze")
        );
    }

    @Test
    void testWPEF_Hail_Scooter_ThrowsException() {
        assertThrows(VehicleUsageForbiddenException.class, () ->
                service.calculateDeliveryFee(City.TARTU, VehicleType.SCOOTER, 15.0, 5.0, "Hail")
        );
    }

    @Test
    void testWPEF_Thunder_Scooter_ThrowsException() {
        assertThrows(VehicleUsageForbiddenException.class, () ->
                service.calculateDeliveryFee(City.PÄRNU, VehicleType.SCOOTER, 15.0, 5.0, "Thunder")
        );
    }

    // Forbidden phenomena — Bike
    @Test
    void testWPEF_Glaze_Bike_ThrowsException() {
        assertThrows(VehicleUsageForbiddenException.class, () ->
                service.calculateDeliveryFee(City.TALLINN, VehicleType.BIKE, 15.0, 5.0, "Glaze")
        );
    }

    @Test
    void testWPEF_Thunder_Bike_ThrowsException() {
        assertThrows(VehicleUsageForbiddenException.class, () ->
                service.calculateDeliveryFee(City.PÄRNU, VehicleType.BIKE, 15.0, 5.0, "Thunderstorm")
        );
    }

    // Clear weather, no phenomenon fee
    @Test
    void testWPEF_ClearWeather_Bike() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.BIKE, 15.0, 5.0, "Clear");
        assertEquals(3.0, result); // RBF only
    }

    @Test
    void testWPEF_EmptyPhenomenon_Scooter() {
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.SCOOTER, 15.0, 5.0, "");
        assertEquals(3.5, result); // RBF only
    }

    @Test
    void testWPEF_NullPhenomenon_Bike() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.BIKE, 15.0, 5.0, null);
        assertEquals(2.5, result); // RBF only
    }

    // ========================
    // COMBINED EXTRA FEES
    // ========================

    @Test
    void testCombined_AllExtraFees_Bike() {
        // Cold + windy + snow
        double result = service.calculateDeliveryFee(City.TALLINN, VehicleType.BIKE, -15.0, 15.0, "Light snow");
        assertEquals(5.5, result); // RBF 3.0 + ATEF 1.0 + WSEF 0.5 + WPEF 1.0
    }

    @Test
    void testCombined_TempAndRain_Scooter() {
        double result = service.calculateDeliveryFee(City.PÄRNU, VehicleType.SCOOTER, -5.0, 5.0, "Moderate rain");
        assertEquals(3.5, result); // RBF 2.5 + ATEF 0.5 + WPEF 0.5
    }

    @Test
    void testCombined_ColdAndSnow_Bike() {
        double result = service.calculateDeliveryFee(City.TARTU, VehicleType.BIKE, -12.0, 5.0, "Heavy snow shower");
        assertEquals(4.5, result); // RBF 2.5 + ATEF 1.0 + WPEF 1.0
    }
}