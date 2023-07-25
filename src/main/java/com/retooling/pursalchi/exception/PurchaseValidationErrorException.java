package com.retooling.pursalchi.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PurchaseValidationErrorException extends Exception {

	public PurchaseValidationErrorException() {
		super();
	}
	
	public PurchaseValidationErrorException(String message) {
		super(message);
	}
	
}
