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

	List<CustomerDTO> list();

	Optional<CustomerDTO> get(Long id);

	void deleteById(Long id);

	CustomerDTO save(CustomerDTO input);

	void update(Long id, CustomerDTO input);

	CustomerDTO findByCode(String code);
    
}
