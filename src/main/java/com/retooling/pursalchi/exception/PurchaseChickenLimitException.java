package com.retooling.pursalchi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.MULTI_STATUS)
public class PurchaseChickenLimitException extends Exception {

	public PurchaseChickenLimitException() {
		super();
	}
	
	public PurchaseChickenLimitException(String message) {
		super(message);
	}
	
}
