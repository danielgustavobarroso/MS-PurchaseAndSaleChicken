package com.retooling.pursalchi.service;

import java.text.ParseException;
import java.util.List;

import com.retooling.pursalchi.entity.PurchaseChicken;
import com.retooling.pursalchi.exception.PurchaseChickenLimitException;
import com.retooling.pursalchi.exception.PurchaseChickenMoneyException;

public interface PurchaseChickenService {
	
	public List<PurchaseChicken> getAllPurchaseChickens();
	
	public PurchaseChicken savePurchaseChicken(PurchaseChicken purchaseChicken);

	public PurchaseChicken generatePurchaseChicken(PurchaseChicken purchaseChicken) throws PurchaseChickenMoneyException,
		PurchaseChickenLimitException, ParseException;
	
}
