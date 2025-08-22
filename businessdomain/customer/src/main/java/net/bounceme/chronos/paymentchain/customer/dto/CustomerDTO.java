package net.bounceme.chronos.paymentchain.customer.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7249205254631371436L;

	private Long id;
	
	private String code;
	
	private String name;
	
	private String phone;
	
	private String iban;
	
	private String surname;
	
	private String address;
	
	private List<CustomerProductDTO> products;
	
	private List<?> transactions;
}
