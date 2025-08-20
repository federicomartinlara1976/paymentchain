/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.paymentchain.customer.service;

import java.util.List;

import com.paymentchain.customer.dto.CustomerDTO;

/**
 *
 * @author federico
 */
public interface CustomerService {

	List<CustomerDTO> list();

	CustomerDTO get(Long id);

	void deleteById(Long id);

	CustomerDTO save(CustomerDTO input);

	CustomerDTO update(Long id, CustomerDTO input);
    
}
