package com.lumbergmarilee.delivery_fee_calculator.scheduler;

import com.lumbergmarilee.delivery_fee_calculator.model.WeatherData;
import com.lumbergmarilee.delivery_fee_calculator.repository.WeatherDataRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;



@Component
public class WeatherDataImporter {

    private final WeatherDataRepository weatherDataRepository;

    private static final String WEATHER_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private static final List<String> STATIONS = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");


    @jakarta.annotation.PostConstruct // Right after startup import weather
    public void init() {
        importWeatherData();
    }

    public WeatherDataImporter(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    @Scheduled(cron = "0 15 * * * *") // import weather every hour at minute 15
    public void importWeatherData() {
        // 1. Fetch XML from the URL
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document document = builder.parse(new URL(WEATHER_URL).openStream());

            // get timestamp from root element
            long unixTimestamp = Long.parseLong(document.getDocumentElement().getAttribute("timestamp"));
            LocalDateTime timestamp = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(unixTimestamp), ZoneId.of("Europe/Tallinn"));

            // Loop through all station elements
            NodeList stationNodes = document.getElementsByTagName("station");

            for (int i = 0; i < stationNodes.getLength(); i++) {
                Element station = (Element) stationNodes.item(i);
                String stationName = getTagValue(station, "name");

                // Only process our three stations
                if (STATIONS.contains(stationName)) {
                    WeatherData weatherData = new WeatherData();
                    weatherData.setStationName(stationName);
                    weatherData.setWmoCode(getTagValue(station, "wmocode"));
                    weatherData.setAirTemperature(parseDouble(getTagValue(station, "airtemperature")));
                    weatherData.setWindSpeed(parseDouble(getTagValue(station, "windspeed")));
                    weatherData.setWeatherPhenomenon(getTagValue(station, "phenomenon"));
                    weatherData.setTimestamp(timestamp);

                    weatherDataRepository.save(weatherData);
                }
            }

            System.out.println("Weather data imported successfully at " + LocalDateTime.now());

        } catch (Exception e) {
            System.err.println("Failed to import weather data: " + e.getMessage());
        }
    }

    private String getTagValue(Element element, String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        if (nodes.getLength() > 0 && nodes.item(0).getTextContent() != null) {
            return nodes.item(0).getTextContent().trim();
        }
        return "";
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
