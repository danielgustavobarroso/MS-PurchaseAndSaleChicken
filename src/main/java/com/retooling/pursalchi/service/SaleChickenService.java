package com.retooling.pursalchi.service;

import java.util.List;

import com.retooling.pursalchi.entity.SaleChicken;
import com.retooling.pursalchi.exception.SaleChickenAmountException;
import com.retooling.pursalchi.exception.SaleChickenException;

public interface SaleChickenService {
	
	public List<SaleChicken> getAllSaleChickens();

	public SaleChicken saveSaleChicken(SaleChicken saleChicken);
	
	public SaleChicken generateSaleChicken(SaleChicken saleChicken) throws SaleChickenException, SaleChickenAmountException;
	
}
