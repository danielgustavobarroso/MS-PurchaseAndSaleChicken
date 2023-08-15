package com.retooling.pursalchi.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retooling.pursalchi.entity.Chicken;
import com.retooling.pursalchi.entity.Farm;
import com.retooling.pursalchi.entity.SaleChicken;
import com.retooling.pursalchi.exception.SaleChickenAmountException;
import com.retooling.pursalchi.repository.SaleChickenRepository;

@Service
public class SaleChickenServiceImpl implements SaleChickenService {

	private static final Logger logger = LoggerFactory.getLogger(SaleChickenServiceImpl.class);
	
	@Autowired
	SaleChickenRepository repository;

	@Autowired
	private ApiCall apiCall;
	
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
	public SaleChicken generateSaleChicken(SaleChicken saleChicken) throws SaleChickenAmountException, ParseException {
		logger.info("Service - Calling method generateSaleChicken...");
		
		List<Chicken> chickens = apiCall.getChickens(saleChicken.getFarmId());
		
		validateChickenAvailable(saleChicken.getUnits(), chickens.size());
		
		Date currentDate = apiCall.getDate();
			
		for(int indice=0;indice<saleChicken.getUnits();indice++) {
			ChickenState chickenSold = ChickenState.Sold;
			chickens.get(indice).setState(chickenSold.getState());
			chickens.get(indice).setLastStateChangeDate(currentDate);
			apiCall.updateChicken(chickens.get(indice));		
			logger.info("Venta - Se actualiza estado de la gallina con id [" + chickens.get(indice).getChickenId() + "] a Vendida");
		}

		Farm farm = apiCall.getFarm(saleChicken.getFarmId());
		farm.setMoney(farm.getMoney() + saleChicken.getTotalAmount());
		apiCall.updateFarm(farm);
		
		saleChicken.setSaleDate(currentDate);
		return this.saveSaleChicken(saleChicken);
	}

	private void validateChickenAvailable(long units, int chickensCount) throws SaleChickenAmountException {
		if (units > chickensCount) {
			logger.info("La cantidad de gallinas que se desea vender es mayor a la disponible");
			throw new SaleChickenAmountException("La cantidad de gallinas que se desea vender es mayor a la disponible.");
		}
	}
	
}
