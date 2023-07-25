package com.retooling.pursalchi.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.retooling.pursalchi.entity.Chicken;
import com.retooling.pursalchi.entity.Farm;

@Service
public class ApiCall {

	private static final Logger logger = LoggerFactory.getLogger(ApiCall.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${api.microservice.farm}")
	private String urlFarm;
	
	@Value("${api.microservice.chicken}")
	private String urlChicken;

	@Value("${api.microservice.report}")
	private String urlReport;
	
	@Value("${api.microservice.date-simulator}")
	private String urlDateSimulator;
	
	public ApiCall() {
		super();
	}
	
	public Farm getFarm(String id) {
		logger.info("Service - Calling getFarm...");
		Farm farm = restTemplate.getForObject(urlFarm+"/{id}", Farm.class, id);
		return farm;
	}

	public void updateFarm(Farm farm) {
		logger.info("Service - Calling updateFarm...");
		restTemplate.put(urlFarm, farm, Farm.class);
	}

	public List<Chicken> getChickens(String idFarm) {
		logger.info("Service - Calling getChickens...");
		ChickenState chickenAvailable = ChickenState.Available;
		return Arrays.asList(restTemplate.getForObject(urlChicken+"/farms/{idFarm}", Chicken[].class, idFarm))
				.stream().filter(c -> c.getState().equals(chickenAvailable.getState())).collect(Collectors.toList());
	}

	public Chicken insertChicken(Chicken chicken) {
		logger.info("Service - Calling insertChicken...");
		return restTemplate.postForObject(urlChicken, chicken, Chicken.class);
	}

	public void updateChicken(Chicken chicken) {
		logger.info("Service - Calling updateChicken...");
		restTemplate.put(urlChicken, chicken, Chicken.class);
	}

	public void generateReport(String idFarm) {
		logger.info("Service - Calling generateReport...");
		restTemplate.postForObject(urlReport+"/currentStatusReport/generateFile", idFarm, String.class);
	}
	
	public Date getDate() throws ParseException {
		logger.info("Service - Calling getDate...");
		String dateStr = restTemplate.getForObject(urlDateSimulator+"/get-date", String.class);
		return (new SimpleDateFormat("yyyyMMddHHmmss").parse(dateStr));
	}
	
}
