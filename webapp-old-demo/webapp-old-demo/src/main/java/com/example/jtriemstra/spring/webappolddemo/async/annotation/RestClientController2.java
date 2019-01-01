package com.example.jtriemstra.spring.webappolddemo.async.annotation;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.jtriemstra.spring.webappolddemo.async.ComboModel;
import com.example.jtriemstra.spring.webappolddemo.async.GeoLocModel;
import com.example.jtriemstra.spring.webappolddemo.async.WeatherModel;

import lombok.extern.slf4j.Slf4j;

@RestController
@EnableWebMvc
@Slf4j
public class RestClientController2 {
	
	@Autowired
	GeoLocProxy2 geoLocProxy;
	
	@Autowired
	WeatherProxy2 weatherProxy;
	
	//Basic sync call
	@RequestMapping(value = "/ann/coords", method = RequestMethod.GET)
	public GeoLocModel getCoords() {
		return geoLocProxy.getCoords();
	}

	//Returns quickly
	@RequestMapping(value = "/ann/coordsAsync", method = RequestMethod.GET)
	public GeoLocModel getCoordsAsync() {
		log.info("calling async proxy");
		Future<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		log.info("proxy returned");
		
		try {
			ResponseEntity<GeoLocModel> response = geoLocFuture.get();
			log.info("response ready");
			return response.getBody();
		}
		catch (ExecutionException ex) {
			log.error("Error waiting for async execution", ex);
			return null;
		}
		catch (InterruptedException ex) {
			log.error("Error waiting for async execution", ex);
			return null;
		}		
	}
	
	//Returns quickly
	@RequestMapping(value = "/ann/coordsAsync1", method = RequestMethod.GET)
	public GeoLocModel getCoordsAsync1() {
		log.info("calling async proxy 1");
		Future<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync1();
		log.info("proxy returned");
		
		try {
			ResponseEntity<GeoLocModel> response = geoLocFuture.get();
			log.info("response ready");
			return response.getBody();
		}
		catch (ExecutionException ex) {
			log.error("Error waiting for async execution", ex);
			return null;
		} catch (InterruptedException ex) {
			log.error("Error waiting for async execution", ex);
			return null;
		}
	}

	//Takes a long time to return
	@RequestMapping(value = "/ann/coordsAsync2", method = RequestMethod.GET)
	public CompletableFuture<ResponseEntity<GeoLocModel>> getCoordsAsync2() {
		CompletableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		
		log.info("ABOUT TO RETURN CONTROLLER");
		return geoLocFuture;		
	}
	
	//Takes a long time to return
	@RequestMapping(value = "/ann/coordsAsync3", method = RequestMethod.GET)
	public ResponseEntity<GeoLocModel> getCoordsAsync3() throws Exception {
		CompletableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		
		log.info("ABOUT TO RETURN CONTROLLER");
		return geoLocFuture.get();		
	}
	
	//Returns quickly
	@RequestMapping(value = "/ann/coordsAsync4", method = RequestMethod.GET)
	public GeoLocModel getCoordsAsync4() {
		log.info("calling async proxy 2");
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync2();
		log.info("proxy returned");
		
		try {
			ResponseEntity<GeoLocModel> response = geoLocFuture.get();
			log.info("response ready");
			return response.getBody();
		}
		catch (ExecutionException ex) {
			log.error("Error waiting for async execution", ex);
			return null;
		} catch (InterruptedException ex) {
			log.error("Error waiting for async execution", ex);
			return null;
		}		
	}
	

	@RequestMapping(value = "/ann/conditions", method = RequestMethod.GET)
	public WeatherModel getConditions() {
		log.info("calling async proxy 2");
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync2();
		log.info("proxy returned");
		
		try {
			ResponseEntity<GeoLocModel> response = geoLocFuture.get();
			log.info("response ready");
			
			GeoLocModel location = response.getBody();
			log.info("calling weather proxy 2");
			ListenableFuture<ResponseEntity<WeatherModel>> weatherFuture = weatherProxy.getConditionsAsync(location.getLat(), location.getLon());
			log.info("weather proxy returned");
			ListenableFuture<ResponseEntity<WeatherModel>> weatherFuture2 = weatherProxy.getConditionsAsync(location.getLat(), location.getLon());
			ListenableFuture<ResponseEntity<WeatherModel>> weatherFuture3 = weatherProxy.getConditionsAsync(location.getLat(), location.getLon());
			return weatherFuture.get().getBody();
		}
		catch (ExecutionException ex) {
			log.error("Error waiting for async execution", ex);
			return null;
		} catch (InterruptedException ex) {
			log.error("Error waiting for async execution", ex);
			return null;
		}		
	}
	
	//Async, request and processing handled on different threads
	/*@RequestMapping(value = "/ann/conditionsAsync", method = RequestMethod.GET)
	public DeferredResult<ResponseEntity<WeatherModel>> getConditionsAsync() {
		DeferredResult<ResponseEntity<WeatherModel>> deferredResult = new DeferredResult<>();
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		geoLocFuture.addCallback(new CoordsWeatherCallback(deferredResult));
		
		log.info("ABOUT TO RETURN CONTROLLER");
		return deferredResult;		
	}*/
	
	//Two async calls in parallel
	/*@RequestMapping(value = "/old/parallelHardCoded", method = RequestMethod.GET)
	public ComboModel parallelHardCoded() {
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		ListenableFuture<ResponseEntity<WeatherModel>> weatherFuture = weatherProxy.getConditionsAsync((float) 37.8267,(float)-122.4233);
		log.info("ASYNC CALLS MADE");
		CompletableFuture<ResponseEntity<GeoLocModel>> locResponse = geoLocFuture.completable();
		CompletableFuture<ResponseEntity<WeatherModel>> weatherResponse = weatherFuture.completable();
		
		CompletableFuture.allOf(locResponse, weatherResponse);
		log.info("ASYNC CALLS COMPLETE?");
		try {
			ComboModel objReturn = new ComboModel();
			objReturn.setLocation(locResponse.get().getBody());
			objReturn.setWeather(weatherResponse.get().getBody());
			log.info("ASYNC CALLS DEFINITELY COMPLETE");
			return objReturn;
		}
		catch (ExecutionException ex) {
			log.error("Error waiting for async execution", ex);
			return null;
		}
		catch (InterruptedException ex) {
			log.error("Error waiting for async execution", ex);
			return null;
		}		
	}*/
	
	public class CoordsCallback implements ListenableFutureCallback<ResponseEntity<GeoLocModel>> {
		private DeferredResult deferredResult; 
		
		public CoordsCallback(DeferredResult deferredResult) {
			this.deferredResult = deferredResult;			
		}
		
		@Override
        public void onSuccess(ResponseEntity<GeoLocModel> result) {
            //ResponseEntity<GeoLocModel> responseEntity = 
            //    new ResponseEntity<>(result, HttpStatus.OK);
            deferredResult.setResult(ResponseEntity.ok(result.getBody()));
            log.info("SUCCESS CALLBACK COMPLETE");
        }

        @Override
        public void onFailure(Throwable t) {
            log.error("Failed to fetch result from remote service", t);
            ResponseEntity<Void> responseEntity = 
                new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            deferredResult.setResult(responseEntity);
        }
	}

	public class CoordsWeatherCallback implements ListenableFutureCallback<ResponseEntity<GeoLocModel>> {
		private DeferredResult deferredResult; 
		
		public CoordsWeatherCallback(DeferredResult deferredResult) {
			this.deferredResult = deferredResult;			
		}
		
		@Override
        public void onSuccess(ResponseEntity<GeoLocModel> result) {
			GeoLocModel location = result.getBody();
			ListenableFuture<ResponseEntity<WeatherModel>> conditionsFuture = weatherProxy.getConditionsAsync(location.getLat(), location.getLon());
			conditionsFuture.addCallback(new WeatherCallback(deferredResult));	
			//deferredResult.setResult(ResponseEntity.ok(result.getBody()));
            log.info("SUCCESS CALLBACK COMPLETE");
        }

        @Override
        public void onFailure(Throwable t) {
            log.error("Failed to fetch result from remote service", t);
            ResponseEntity<Void> responseEntity = 
                new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            deferredResult.setResult(responseEntity);
        }
	}
	
	public class WeatherCallback implements ListenableFutureCallback<ResponseEntity<WeatherModel>> {
		private DeferredResult deferredResult; 
		
		public WeatherCallback(DeferredResult deferredResult) {
			this.deferredResult = deferredResult;			
		}
		
		@Override
        public void onSuccess(ResponseEntity<WeatherModel> result) {
            //ResponseEntity<GeoLocModel> responseEntity = 
            //    new ResponseEntity<>(result, HttpStatus.OK);
            deferredResult.setResult(ResponseEntity.ok(result.getBody()));
            log.info("SUCCESS CALLBACK COMPLETE");
        }

        @Override
        public void onFailure(Throwable t) {
            log.error("Failed to fetch result from remote service", t);
            ResponseEntity<Void> responseEntity = 
                new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            deferredResult.setResult(responseEntity);
        }
	}
}
