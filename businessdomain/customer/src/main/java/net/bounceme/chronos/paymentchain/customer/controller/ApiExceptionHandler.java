package net.bounceme.chronos.paymentchain.customer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.paymentchain.customer.exceptions.BussinessRuleException;
import net.bounceme.chronos.paymentchain.customer.exceptions.StandarizedApiExceptionResponse;

@RestControllerAdvice
@Slf4j
class ApiExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception ex) {
		StandarizedApiExceptionResponse exception = new StandarizedApiExceptionResponse("TECNICO", "Input Ouput error", "1024", ex.getMessage(), null);
		
		log.error("ERROR:", ex);

		return ResponseEntity.internalServerError().body(exception);
	}
	
	@ExceptionHandler(BussinessRuleException.class)
	public ResponseEntity<?> handleBussinessRuleException(BussinessRuleException ex) {
		StandarizedApiExceptionResponse exception = new StandarizedApiExceptionResponse("TECNICO", "Input Ouput error", "1024", ex.getMessage(), null);
		
		log.error("ERROR:", ex);

		return ResponseEntity.status(ex.getStatus()).body(exception);
	}
}
