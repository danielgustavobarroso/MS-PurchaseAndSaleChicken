package com.retooling.pursalchi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PurchaseChickenException extends Exception {

	public PurchaseChickenException() {
		super();
	}
	
	public PurchaseChickenException(String message) {
		super(message);
	}
	
}
