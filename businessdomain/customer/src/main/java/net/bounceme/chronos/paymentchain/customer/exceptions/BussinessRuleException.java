package net.bounceme.chronos.paymentchain.customer.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BussinessRuleException extends Exception {

	private static final long serialVersionUID = 426261490121942404L;

	private Long id;
	
	private String code;
	
	private HttpStatus status;

	public BussinessRuleException(Long id, String code, String message, HttpStatus status) {
		super(message);
		this.id = id;
		this.code = code;
		this.status = status;
	}

	public BussinessRuleException(String code, String message, HttpStatus status) {
		super(message);
		this.code = code;
		this.status = status;
	}
	
	public BussinessRuleException(String message, Throwable cause) {
		super(message, cause);
	}
}
