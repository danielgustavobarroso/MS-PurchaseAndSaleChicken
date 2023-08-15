package com.retooling.pursalchi.service;

import java.text.ParseException;
import java.util.List;

import com.retooling.pursalchi.entity.SaleChicken;
import com.retooling.pursalchi.exception.SaleChickenAmountException;

public interface SaleChickenService {
	
	public List<SaleChicken> getAllSaleChickens();

	public SaleChicken saveSaleChicken(SaleChicken saleChicken);
	
	public SaleChicken generateSaleChicken(SaleChicken saleChicken) throws SaleChickenAmountException, ParseException;
	
}
