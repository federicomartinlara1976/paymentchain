package net.bounceme.chronos.paymentchain.product.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7249205254631371436L;

	private Long id;
	
	private String name;
	
	private String phone;
}
