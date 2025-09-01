/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package net.bounceme.chronos.paymentchain.customer.service;

import java.util.List;
import java.util.Optional;

import net.bounceme.chronos.paymentchain.customer.dto.CustomerDTO;

/**
 *
 * @author federico
 */
public interface CustomerService {

	/**
	 * @return
	 */
	List<CustomerDTO> list();

	/**
	 * @param id
	 * @return
	 */
	Optional<CustomerDTO> get(Long id);

	/**
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * @param input
	 * @return
	 */
	CustomerDTO save(CustomerDTO input);

	/**
	 * @param id
	 * @param input
	 */
	void update(Long id, CustomerDTO input);

	/**
	 * @param code
	 * @return
	 */
	Optional<CustomerDTO> findByCode(String code);
	
	/**
	 * @param id
	 * @return
	 */
	String getProductName(Long id);
    
	/**
	 * @param iban
	 * @return
	 */
	List<?> getTransactions(String iban);
    
}
