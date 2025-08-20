/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymentchain.customer.dto.CustomerDTO;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.repository.CustomerRepository;
import com.paymentchain.customer.service.CustomerService;

/**
 *
 * @author federico
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	@Transactional(readOnly = true)
	public List<CustomerDTO> list() {
		List<Customer> customers = customerRepository.findAll();
		return CollectionUtils.isNotEmpty(customers) ? customers.stream()
				.map(customer -> modelMapper.map(customer, CustomerDTO.class)).collect(Collectors.toList())
				: Collections.emptyList();
	}

	@Override
	@Transactional(readOnly = true)
	public CustomerDTO get(Long id) {
		Optional<Customer> oCustomer = customerRepository.findById(id);
		return oCustomer.isPresent() ? modelMapper.map(oCustomer.get(), CustomerDTO.class) : null;
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		customerRepository.deleteById(id);
	}

	@Override
	@Transactional
	public CustomerDTO save(CustomerDTO input) {
		Customer customer = modelMapper.map(input, Customer.class);
		customer = customerRepository.save(customer);
		return modelMapper.map(customer, CustomerDTO.class);
	}

	@Override
	@Transactional
	public CustomerDTO update(Long id, CustomerDTO input) {
		Optional<Customer> oCustomer = customerRepository.findById(id);
		
		if (oCustomer.isPresent()) {
			Customer customer = oCustomer.get();
			customer.setName(input.getName());
			customer.setPhone(input.getPhone());

			customer = customerRepository.save(customer);
			return modelMapper.map(customer, CustomerDTO.class);
		}
		
		return null;
	}
}
