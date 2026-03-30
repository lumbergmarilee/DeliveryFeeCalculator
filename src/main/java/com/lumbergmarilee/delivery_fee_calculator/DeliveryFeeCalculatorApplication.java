package com.lumbergmarilee.delivery_fee_calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Main entry point for the Delivery Fee Calculator application.
 * Enables scheduled tasks for periodic weather data imports.
 */
@EnableScheduling
@SpringBootApplication
public class DeliveryFeeCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryFeeCalculatorApplication.class, args);
	}

}
