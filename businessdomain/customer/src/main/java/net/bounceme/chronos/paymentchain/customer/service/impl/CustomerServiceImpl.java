/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.bounceme.chronos.paymentchain.customer.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.bounceme.chronos.paymentchain.customer.dto.CustomerDTO;
import net.bounceme.chronos.paymentchain.customer.entities.Customer;
import net.bounceme.chronos.paymentchain.customer.repository.CustomerRepository;
import net.bounceme.chronos.paymentchain.customer.service.CustomerService;

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
		return customerRepository.findAll()
				.stream()
				.map(customer -> modelMapper.map(customer, CustomerDTO.class))
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CustomerDTO> get(Long id) {
		return customerRepository.findById(id).map(c -> modelMapper.map(c, CustomerDTO.class));
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
