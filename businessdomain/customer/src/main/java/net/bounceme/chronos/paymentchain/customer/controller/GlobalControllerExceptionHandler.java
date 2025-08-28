package net.bounceme.chronos.paymentchain.customer.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
class GlobalControllerExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Map<String, String>> handleException(Exception ex) {
		String msg = ex.getMessage();
		
		log.error("ERROR:", ex);

		Map<String, String> errors = new HashMap<>();
		errors.put("mensaje", msg);

		return ResponseEntity.internalServerError().body(errors);
	}
}
