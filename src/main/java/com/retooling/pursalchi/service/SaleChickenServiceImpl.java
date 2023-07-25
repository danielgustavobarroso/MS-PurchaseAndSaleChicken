package com.retooling.pursalchi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.retooling.pursalchi.entity.Chicken;
import com.retooling.pursalchi.entity.Farm;
import com.retooling.pursalchi.entity.SaleChicken;
import com.retooling.pursalchi.exception.SaleChickenAmountException;
import com.retooling.pursalchi.exception.SaleChickenException;
import com.retooling.pursalchi.repository.SaleChickenRepository;

@Service
public class SaleChickenServiceImpl implements SaleChickenService {

	private static final Logger logger = LoggerFactory.getLogger(SaleChickenServiceImpl.class);
	
	@Autowired
	SaleChickenRepository repository;

	@Autowired
	private ApiCall apiCall;

	@Value("${api.microservice.use-date-simulator}")
	private boolean useDateSimulator;
	
	@Override
	public List<SaleChicken> getAllSaleChickens() {
		logger.info("Service - Calling method getSaleAllChickens...");
		return repository.findAll();			
	}

	@Override
	public SaleChicken saveSaleChicken(SaleChicken saleChicken) {
		logger.info("Service - Calling method saveSaleChicken...");
		return repository.save(saleChicken);
	}

	@Override
	public SaleChicken generateSaleChicken(SaleChicken saleChicken) throws SaleChickenException, SaleChickenAmountException {
		logger.info("Service - Calling method generateSaleChicken...");
		
		List<Chicken> chickens = new ArrayList<Chicken>();
		chickens = apiCall.getChickens(saleChicken.getFarmId());
		
		if (saleChicken.getUnits() > chickens.size()) {
			logger.info("La cantidad de pollos que se desea vender es mayor a la disponible");
			throw new SaleChickenAmountException("La cantidad de pollos que se desea vender es mayor a la disponible.");
		}
		
		Date currentDate;
		if (useDateSimulator) {
			try {
				currentDate = apiCall.getDate();
			} catch (Exception ex) {
				throw new SaleChickenException(ex.getMessage());
			}
		} else {
			currentDate = new Date();
		}
		
		for(int indice=0;indice<saleChicken.getUnits();indice++) {
			ChickenState chickenSold = ChickenState.Sold;
			chickens.get(indice).setState(chickenSold.getState());
			chickens.get(indice).setLastStateChangeDate(currentDate);
			apiCall.updateChicken(chickens.get(indice));		
			logger.info("Venta - Se actualiza estado de la gallina con id [" + chickens.get(indice).getChickenId() + "] a Vendida");
		}
		
		Farm farm = null;
		try {
			farm = apiCall.getFarm(saleChicken.getFarmId());
		} catch (Exception ex) {
			throw new SaleChickenException(ex.getMessage());
		}

		farm.setMoney(farm.getMoney() + saleChicken.getTotalAmount());
		
		try {
			apiCall.updateFarm(farm);
		} catch (Exception ex) {
			throw new SaleChickenException(ex.getMessage());
		}

		if (useDateSimulator) {
			try {
				saleChicken.setSaleDate(apiCall.getDate());
			} catch (Exception ex) {
				throw new SaleChickenException(ex.getMessage());
			}
		}
		
		return this.saveSaleChicken(saleChicken);
	}
	
}
