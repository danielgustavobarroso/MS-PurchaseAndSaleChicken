package com.retooling.pursalchi.controller;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retooling.pursalchi.entity.SaleChicken;
import com.retooling.pursalchi.exception.SaleChickenAmountException;
import com.retooling.pursalchi.exception.SaleValidationErrorException;
import com.retooling.pursalchi.service.SaleChickenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SaleChickenController {

	private static final Logger logger = LoggerFactory.getLogger(SaleChickenController.class);
	
	@Autowired
	SaleChickenService service;

	//Obtener todas las ventas de gallinas
	@GetMapping("sale-chicken")
	public ResponseEntity<List<SaleChicken>> getAllSaleChickens() {
		logger.info("Controller - Calling method getAllSaleChickens...");
		return new ResponseEntity<>(service.getAllSaleChickens(), HttpStatus.OK);
	}

	//Guardar una venta de gallinas
	@PostMapping("sale-chicken")
	public ResponseEntity<SaleChicken> generateSaleChicken(@Valid @RequestBody SaleChicken saleChicken,
			BindingResult bindingResult) throws SaleChickenAmountException, SaleValidationErrorException, ParseException {		
		logger.info("Controller - Calling method generateSaleChicken...");
		if (bindingResult.hasErrors()) {
			String message = new String();
			for(FieldError error : bindingResult.getFieldErrors()) {
				if (message.isEmpty()) {
					message = message + error.getField() + " : " + error.getDefaultMessage();
				} else {
					message = message + ", " + error.getField() + " : " + error.getDefaultMessage();
				}
			}
			throw new SaleValidationErrorException(message);
		}
		return new ResponseEntity<>(service.generateSaleChicken(saleChicken), HttpStatus.OK);
	}

}