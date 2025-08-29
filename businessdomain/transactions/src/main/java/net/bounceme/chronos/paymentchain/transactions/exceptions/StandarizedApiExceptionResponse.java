package net.bounceme.chronos.paymentchain.transactions.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "This model is used to return errors in RFC 7807 which created a generalized error-handling schema composed by five parts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandarizedApiExceptionResponse {

	private String type;
	
	private String title;
	
	private String code;
	
	private String detail;
	
	private String instance;
}
