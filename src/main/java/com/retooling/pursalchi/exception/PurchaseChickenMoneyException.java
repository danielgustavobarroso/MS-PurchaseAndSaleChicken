package com.retooling.pursalchi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.MULTI_STATUS)
public class PurchaseChickenMoneyException extends Exception {

	public PurchaseChickenMoneyException(String message) {
		super(message);
	}
	
}
