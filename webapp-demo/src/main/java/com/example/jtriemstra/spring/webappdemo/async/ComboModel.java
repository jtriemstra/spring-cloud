package com.example.jtriemstra.spring.webappdemo.async;

import lombok.Data;

@Data
public class ComboModel {
	private GeoLocModel location;
	private WeatherModel weather;
}
