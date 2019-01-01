package com.example.jtriemstra.spring.webappolddemo.async.asyncresttemplate;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.example.jtriemstra.spring.webappolddemo.async.GeoLocModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GeoLocProxy {
	
	private final static String IP_URL = "http://ip-api.com/json/"; 
	
	public GeoLocModel getCoords() {
		RestTemplate restTemplate = new RestTemplate();
		GeoLocModel geoInfo = restTemplate.getForObject(IP_URL, GeoLocModel.class);
		return geoInfo;
	}
	
	public ListenableFuture<ResponseEntity<GeoLocModel>> getCoordsAsync() {
		AsyncRestTemplate restTemplate = new AsyncRestTemplate();
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = restTemplate.getForEntity(IP_URL, GeoLocModel.class);
		log.info("GEO PROXY CALL RETURNING");
		return geoLocFuture;
	}
	
	
}
