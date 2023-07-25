package com.retooling.pursalchi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.retooling.pursalchi.entity.Chicken;
import com.retooling.pursalchi.entity.Farm;
import com.retooling.pursalchi.entity.PurchaseChicken;
import com.retooling.pursalchi.exception.PurchaseChickenException;
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

	@Value("${api.microservice.use-date-simulator}")
	private boolean useDateSimulator;
	
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
	public PurchaseChicken generatePurchaseChicken(PurchaseChicken purchaseChicken) throws PurchaseChickenException,
			PurchaseChickenMoneyException, PurchaseChickenLimitException {
		logger.info("Service - Calling method generatePurchaseChicken...");
		
		Farm farm = null;
		try {
			farm = apiCall.getFarm(purchaseChicken.getFarmId());
		} catch (Exception ex) {
			throw new PurchaseChickenException(ex.getMessage());
		}

		List<Chicken> chickens = null;
		try {
			chickens = apiCall.getChickens(purchaseChicken.getFarmId());
		} catch (HttpClientErrorException.NotFound ex) {
			chickens = new ArrayList<>();
		} catch (Exception ex) {
			throw new PurchaseChickenException(ex.getMessage());
		}
		
		if ((purchaseChicken.getUnits() + chickens.size()) > farm.getChickenLimit()) {
			logger.info("La cantidad de pollos a comprar supera el límite de la granja.");
			throw new PurchaseChickenLimitException("La cantidad de pollos a comprar supera el límite de la granja.");
		}
		
		if (purchaseChicken.getTotalAmount() > farm.getMoney()) {
			logger.info("La cantidad de dinero utilizada supera el monto disponible.");
			throw new PurchaseChickenMoneyException("La cantidad de dinero utilizada supera el monto disponible.");
		}

		Date currentDate;
		if (useDateSimulator) {
			try {
				currentDate = apiCall.getDate();
			} catch (Exception ex) {
				throw new PurchaseChickenException(ex.getMessage());
			}
		} else {
			currentDate = new Date();
		}
		
		//Se agregan gallinas
		for(int indice=0;indice<purchaseChicken.getUnits();indice++) {
			Chicken chicken = new Chicken();
			chicken.setFarmId(purchaseChicken.getFarmId());
			ChickenState chickenAvailable = ChickenState.Available;
			chicken.setState(chickenAvailable.getState());		
			chicken.setCreationDate(currentDate);
			ChickenOrigin chickenOrigin = ChickenOrigin.Bought;
			chicken.setOrigin(chickenOrigin.getOrigin());
			chicken.setLastEggDate(chicken.getCreationDate());
			chicken.setLastStateChangeDate(chicken.getCreationDate());
			try {
				chicken = apiCall.insertChicken(chicken);
				logger.info("Compra - Se agrega pollo: [" + chicken.getChickenId() + "]");
			} catch (Exception ex) {
				throw new PurchaseChickenException(ex.getMessage());
			}
		}

		//Se actualiza dinero de granja
		farm.setMoney(farm.getMoney() - purchaseChicken.getTotalAmount());
		try {
			apiCall.updateFarm(farm);
		} catch (Exception ex) {
			throw new PurchaseChickenException(ex.getMessage());
		}
		
		//Se genera reporte en caso de alcanzar el limite
		if ((purchaseChicken.getUnits() + chickens.size()) == farm.getChickenLimit()) {
			try {
				apiCall.generateReport(purchaseChicken.getFarmId());
			} catch (Exception ex) {
				throw new PurchaseChickenException(ex.getMessage());
			}
		}

		purchaseChicken.setPurchaseDate(currentDate);
		
		return this.savePurchaseChicken(purchaseChicken);
	}
	
}
