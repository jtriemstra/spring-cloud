package com.example.jtriemstra.spring.webappdemo.async.deprecated;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.example.jtriemstra.spring.webappdemo.async.WeatherModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherProxy {
private final static String WEATHER_URL = "https://api.darksky.net/forecast/86fd68fca387e148241764663802943e/"; 
	
	public WeatherModel getConditions(String lat, String lon) {
		RestTemplate restTemplate = new RestTemplate();
		WeatherModel weatherInfo = restTemplate.getForObject(WEATHER_URL + lat + "," + lon, WeatherModel.class);
		return weatherInfo;
	}
	
	public WeatherModel getConditions(float lat, float lon) {
		return getConditions(Float.toString(lat), Float.toString(lon));
	}
	
	public ListenableFuture<ResponseEntity<WeatherModel>> getConditionsAsync(float lat, float lon) {
		AsyncRestTemplate restTemplate = new AsyncRestTemplate();
		ListenableFuture<ResponseEntity<WeatherModel>> geoLocFuture = restTemplate.getForEntity(WEATHER_URL + lat + "," + lon, WeatherModel.class);
		log.info("WEATHER PROXY CALL RETURNING");
		return geoLocFuture;
	}
}
