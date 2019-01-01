package com.example.jtriemstra.spring.webappolddemo.async.annotation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.example.jtriemstra.spring.webappolddemo.async.GeoLocModel;
import com.example.jtriemstra.spring.webappolddemo.async.WeatherModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherProxy2 {
	private final static String WEATHER_URL = "https://api.darksky.net/forecast/86fd68fca387e148241764663802943e/"; 
	
	public WeatherModel getConditions(String lat, String lon) {
		RestTemplate restTemplate = new RestTemplate();
		WeatherModel weatherInfo = restTemplate.getForObject(WEATHER_URL + lat + "," + lon, WeatherModel.class);
		return weatherInfo;
	}
	
	public WeatherModel getConditions(float lat, float lon) {
		return getConditions(Float.toString(lat), Float.toString(lon));
	}
	
	@Async
	public ListenableFuture<ResponseEntity<WeatherModel>> getConditionsAsync(float lat, float lon) {
		log.info("getConditionsAsync start");
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<WeatherModel> geoLocFuture = restTemplate.getForEntity(WEATHER_URL + lat + "," + lon, WeatherModel.class);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("getConditionsAsync returning");
		return new AsyncResult<ResponseEntity<WeatherModel>>(geoLocFuture);
	}
}
