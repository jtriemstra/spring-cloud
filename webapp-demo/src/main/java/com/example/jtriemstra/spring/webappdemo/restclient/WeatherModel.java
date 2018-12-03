package com.example.jtriemstra.spring.webappdemo.restclient;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WeatherModel {
	
	private double temperature;
	private double dewPoint;
	
	@JsonProperty("currently")
	private void unpackCurrently(Map<String,Object> currently) {
		temperature = (double) currently.get("temperature");
		dewPoint = (double) currently.get("dewPoint");
	}
}
