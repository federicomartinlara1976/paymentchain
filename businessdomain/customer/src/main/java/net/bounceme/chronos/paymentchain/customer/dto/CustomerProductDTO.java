package net.bounceme.chronos.paymentchain.customer.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerProductDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3894802972404827797L;

	private Long id;
	
	private Long productId;
	
	private String productName;

	@JsonIgnore
	private CustomerDTO customer;
}
