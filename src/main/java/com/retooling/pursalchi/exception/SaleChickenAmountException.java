package com.retooling.pursalchi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.MULTI_STATUS)
public class SaleChickenAmountException extends Exception {

	public SaleChickenAmountException() {
		super();
	}
	
	public SaleChickenAmountException(String message) {
		super(message);
	}
	
}
