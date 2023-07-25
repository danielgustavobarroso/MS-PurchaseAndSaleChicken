package com.retooling.pursalchi.controller;

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
import com.retooling.pursalchi.entity.PurchaseChicken;
import com.retooling.pursalchi.exception.PurchaseChickenException;
import com.retooling.pursalchi.exception.PurchaseChickenLimitException;
import com.retooling.pursalchi.exception.PurchaseChickenMoneyException;
import com.retooling.pursalchi.exception.PurchaseValidationErrorException;
import com.retooling.pursalchi.service.PurchaseChickenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class PurchaseChickenController {

	private static final Logger logger = LoggerFactory.getLogger(PurchaseChickenController.class);

	@Autowired
	PurchaseChickenService service;

	//Obtener todas las compras de gallinas
	@GetMapping("purchase-chicken")
	public ResponseEntity<List<PurchaseChicken>> getAllPurchaseChickens() {
		logger.info("Controller - Calling method getAllPurchaseChickens...");
		return new ResponseEntity<>(service.getAllPurchaseChickens(), HttpStatus.OK);
	}

	//Guardar una compra de gallinas
	@PostMapping("purchase-chicken")
	public ResponseEntity<PurchaseChicken> generatePurchaseChicken(@Valid @RequestBody PurchaseChicken purchaseChicken,
			BindingResult bindingResult) throws PurchaseChickenException, PurchaseChickenMoneyException,
			PurchaseChickenLimitException, PurchaseValidationErrorException {		
		logger.info("Controller - Calling method generatePurchaseChicken...");
		if (bindingResult.hasErrors()) {
			String message = new String();
			for(FieldError error : bindingResult.getFieldErrors()) {
				if (message.isEmpty()) {
					message = message + error.getField() + " : " + error.getDefaultMessage();
				} else {
					message = message + ", " + error.getField() + " : " + error.getDefaultMessage();
				}
			}
			throw new PurchaseValidationErrorException(message);
		}
		return new ResponseEntity<>(service.generatePurchaseChicken(purchaseChicken), HttpStatus.OK);
	}
		
}