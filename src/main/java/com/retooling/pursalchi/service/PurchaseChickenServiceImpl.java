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
import com.retooling.pursalchi.entity.PurchaseChicken;
import com.retooling.pursalchi.exception.PurchaseChickenLimitException;
import com.retooling.pursalchi.exception.PurchaseChickenMoneyException;
import com.retooling.pursalchi.repository.PurchaseChickenRepository;

@Service
public class PurchaseChickenServiceImpl implements PurchaseChickenService {

	private static final Logger logger = LoggerFactory.getLogger(PurchaseChickenServiceImpl.class);
	
	@Autowired
	PurchaseChickenRepository repository;

	@Autowired
	private ApiCall apiCall;
	
	@Override
	public List<PurchaseChicken> getAllPurchaseChickens() {
		logger.info("Service - Calling method getAllChickens...");
		return repository.findAll();	
	}

	@Override
	public PurchaseChicken savePurchaseChicken(PurchaseChicken purchaseChicken) {
		logger.info("Service - Calling method saveSaleChicken...");
		return repository.save(purchaseChicken);
	}

	@Override
	public PurchaseChicken generatePurchaseChicken(PurchaseChicken purchaseChicken) throws PurchaseChickenMoneyException,
			PurchaseChickenLimitException, ParseException {
		logger.info("Service - Calling method generatePurchaseChicken...");
		
		Farm farm = apiCall.getFarm(purchaseChicken.getFarmId());
		List<Chicken> chickens = apiCall.getChickens(purchaseChicken.getFarmId());
		
		validateChickenLimit(purchaseChicken.getUnits(), chickens.size(), farm.getChickenLimit());
		validateFarmMoney(purchaseChicken.getTotalAmount(), farm.getMoney());
			
		Date currentDate = apiCall.getDate();
		
		//Se agregan gallinas
		for(int indice=0;indice<purchaseChicken.getUnits();indice++) {
			Chicken chicken = new Chicken();
			copyChickenData(chicken, purchaseChicken, currentDate);
			chicken = apiCall.insertChicken(chicken);
			logger.info("Compra - Se agrega pollo: [" + chicken.getChickenId() + "]");
		}

		//Se actualiza dinero de granja
		farm.setMoney(farm.getMoney() - purchaseChicken.getTotalAmount());
		apiCall.updateFarm(farm);
		
		//Se genera reporte en caso de alcanzar el limite
		if ((purchaseChicken.getUnits() + chickens.size()) == farm.getChickenLimit()) {
			apiCall.generateReport(purchaseChicken.getFarmId());
		}

		purchaseChicken.setPurchaseDate(currentDate);
		
		return this.savePurchaseChicken(purchaseChicken);
	}
	
	private void validateChickenLimit(long units, int eggsCount, long eggLimit) throws PurchaseChickenLimitException {
		if ((units + eggsCount) > eggLimit) {
			logger.info("La cantidad de huevos a comprar supera el límite de la granja.");
			throw new PurchaseChickenLimitException("La cantidad de huevos a comprar supera el límite de la granja.");
		}	
	}
	
	private void validateFarmMoney(double totalAmount, double money) throws PurchaseChickenMoneyException {
		if (totalAmount > money) {
			logger.info("La cantidad de dinero utilizada supera el monto disponible.");
			throw new PurchaseChickenMoneyException("La cantidad de dinero utilizada supera el monto disponible.");
		}
	}
	
	private void copyChickenData(Chicken chicken, PurchaseChicken purchaseChicken, Date currentDate) {
		chicken.setFarmId(purchaseChicken.getFarmId());
		ChickenState chickenAvailable = ChickenState.Available;
		chicken.setState(chickenAvailable.getState());		
		chicken.setCreationDate(currentDate);
		ChickenOrigin chickenOrigin = ChickenOrigin.Bought;
		chicken.setOrigin(chickenOrigin.getOrigin());
		chicken.setLastEggDate(chicken.getCreationDate());
		chicken.setLastStateChangeDate(chicken.getCreationDate());
	}
	
}
