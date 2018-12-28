package com.example.jtriemstra.spring.webappolddemo.async;

import lombok.Data;

@Data
public class ComboModel {
	private GeoLocModel location;
	private WeatherModel weather;
}
