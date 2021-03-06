package com.example.jtriemstra.spring.webappolddemo.async.asyncresttemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class RestClientController {
	
	@Autowired
	GeoLocProxy geoLocProxy;
	
	@Autowired
	WeatherProxy weatherProxy;
	
	//Basic sync call
	@RequestMapping(value = "/art/coords", method = RequestMethod.GET)
	public GeoLocModel getCoords() {
		return geoLocProxy.getCoords();
	}

	//Call using async classes, but blocking
	@RequestMapping(value = "/art/coordsAsync", method = RequestMethod.GET)
	public GeoLocModel getCoordsAsync() {
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		
		try {
			ResponseEntity<GeoLocModel> response = geoLocFuture.get();			
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
	
	//Call using async classes, but blocking
	/*@RequestMapping(value = "/old/coordsAsync1", method = RequestMethod.GET)
	public GeoLocModel getCoordsAsync1() {
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		CompletableFuture<ResponseEntity<GeoLocModel>> response = geoLocFuture.completable();
		CompletableFuture.allOf(response);
		
		try {
			return response.get().getBody();
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

	//Async, request and processing handled on different threads
	@RequestMapping(value = "/art/coordsAsync2", method = RequestMethod.GET)
	public DeferredResult<ResponseEntity<GeoLocModel>> getCoordsAsync2() {
		DeferredResult<ResponseEntity<GeoLocModel>> deferredResult = new DeferredResult<>();
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		geoLocFuture.addCallback(new CoordsCallback(deferredResult));
		
		log.info("ABOUT TO RETURN CONTROLLER");
		return deferredResult;		
	}
	
	//Async, request and processing handled on different threads, but how do I tell it that processing is complete? Takes forever to return
	@RequestMapping(value = "/art/coordsAsync3", method = RequestMethod.GET)
	public ListenableFuture<ResponseEntity<GeoLocModel>> getCoordsAsync3() throws Exception {
		DeferredResult<ResponseEntity<GeoLocModel>> deferredResult = new DeferredResult<>();
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		geoLocFuture.addCallback(new CoordsCallback(deferredResult));
		
		log.info("ABOUT TO RETURN CONTROLLER");
		return geoLocFuture;		
	}
		
	//Async, request and processing handled on different threads, but how do I tell it that processing is complete? Takes forever to return
	@RequestMapping(value = "/art/coordsAsync4", method = RequestMethod.GET)
	public ListenableFuture<ResponseEntity<GeoLocModel>> getCoordsAsync4() {
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		
		return geoLocFuture;		
	}
	

	//Async, request and processing handled on different threads, but how do I tell it that processing is complete? Takes forever to return
	@RequestMapping(value = "/art/coordsAsync5", method = RequestMethod.GET)
	public ListenableFuture<ResponseEntity<GeoLocModel>> getCoordsAsync5() throws Exception {
		DeferredResult<ResponseEntity<GeoLocModel>> deferredResult = new DeferredResult<>();
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		geoLocFuture.addCallback(new CoordsCallback(deferredResult));
		
		log.info("ABOUT TO RETURN CONTROLLER");
		geoLocFuture.get();
		return geoLocFuture;		
	}
	
	//Basic sync call
	@RequestMapping(value="/art/conditionsHardCoded", method = RequestMethod.GET)
	public WeatherModel getConditionsHardCoded() {
		return weatherProxy.getConditions("37.8267","-122.4233");
	}
	
	//Call using async classes but blocking
	@RequestMapping(value = "/art/conditions", method = RequestMethod.GET)
	public WeatherModel getConditions() {
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		
		try {
			ResponseEntity<GeoLocModel> response = geoLocFuture.get();			
			GeoLocModel location = response.getBody();
			
			ListenableFuture<ResponseEntity<WeatherModel>> conditionsFuture = weatherProxy.getConditionsAsync(location.getLat(), location.getLon());
			WeatherModel conditions = conditionsFuture.get().getBody();
			
			return conditions;
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
	
	//Async, request and processing handled on different threads
	@RequestMapping(value = "/art/conditionsAsync", method = RequestMethod.GET)
	public DeferredResult<ResponseEntity<WeatherModel>> getConditionsAsync() {
		DeferredResult<ResponseEntity<WeatherModel>> deferredResult = new DeferredResult<>();
		ListenableFuture<ResponseEntity<GeoLocModel>> geoLocFuture = geoLocProxy.getCoordsAsync();
		geoLocFuture.addCallback(new CoordsWeatherCallback(deferredResult));
		
		log.info("ABOUT TO RETURN CONTROLLER");
		return deferredResult;		
	}
	
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
