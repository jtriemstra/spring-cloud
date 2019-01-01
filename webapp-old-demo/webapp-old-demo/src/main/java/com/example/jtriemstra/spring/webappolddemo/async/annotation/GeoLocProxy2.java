package com.example.jtriemstra.spring.webappolddemo.async.annotation;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.example.jtriemstra.spring.webappolddemo.async.GeoLocModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GeoLocProxy2 {
	
	private final static String IP_URL = "http://ip-api.com/json/"; 
	
	public GeoLocModel getCoords() {
		RestTemplate restTemplate = new RestTemplate();
		GeoLocModel geoInfo = restTemplate.getForObject(IP_URL, GeoLocModel.class);
		return geoInfo;
	}
	
	@Async
	public CompletableFuture<ResponseEntity<GeoLocModel>> getCoordsAsync() {
		log.info("getCoordsAsync starting");
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<GeoLocModel> geoLocFuture = restTemplate.getForEntity(IP_URL, GeoLocModel.class);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("getCoordsAsync returning");
		return CompletableFuture.completedFuture(geoLocFuture);
	}
	
	@Async
	public Future<ResponseEntity<GeoLocModel>> getCoordsAsync1() {
		log.info("getCoordsAsync2 start");
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<GeoLocModel> geoLocFuture = restTemplate.getForEntity(IP_URL, GeoLocModel.class);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("getCoordsAsync2 returning");
		return new AsyncResult<ResponseEntity<GeoLocModel>>(geoLocFuture);
	}

	@Async
	public ListenableFuture<ResponseEntity<GeoLocModel>> getCoordsAsync2() {
		log.info("getCoordsAsync3 start");
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<GeoLocModel> geoLocFuture = restTemplate.getForEntity(IP_URL, GeoLocModel.class);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("getCoordsAsync3 returning");
		return new AsyncResult<ResponseEntity<GeoLocModel>>(geoLocFuture);
	}
	
}
