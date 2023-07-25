package com.retooling.pursalchi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SaleChickenException extends Exception {

	public SaleChickenException() {
		super();
	}
	
	public SaleChickenException(String message) {
		super(message);
	}
	
}
