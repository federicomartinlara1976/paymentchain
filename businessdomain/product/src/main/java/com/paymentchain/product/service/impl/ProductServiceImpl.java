/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.product.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymentchain.product.dto.ProductDTO;
import com.paymentchain.product.entities.Product;
import com.paymentchain.product.repository.ProductRepository;
import com.paymentchain.product.service.ProductService;

/**
 *
 * @author federico
 */
@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	@Transactional(readOnly = true)
	public List<ProductDTO> list() {
		List<Product> customers = productRepository.findAll();
		return CollectionUtils.isNotEmpty(customers) ? customers.stream()
				.map(customer -> modelMapper.map(customer, ProductDTO.class)).collect(Collectors.toList())
				: Collections.emptyList();
	}

	@Override
	@Transactional(readOnly = true)
	public ProductDTO get(Long id) {
		Optional<Product> oCustomer = productRepository.findById(id);
		return oCustomer.isPresent() ? modelMapper.map(oCustomer.get(), ProductDTO.class) : null;
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}

	@Override
	@Transactional
	public ProductDTO save(ProductDTO input) {
		Product customer = modelMapper.map(input, Product.class);
		customer = productRepository.save(customer);
		return modelMapper.map(customer, ProductDTO.class);
	}

	@Override
	@Transactional
	public ProductDTO update(Long id, ProductDTO input) {
		Optional<Product> oCustomer = productRepository.findById(id);
		if (oCustomer.isPresent()) {
			Product customer = oCustomer.get();
			customer.setName(input.getName());
			customer.setPhone(input.getPhone());
			
			customer = productRepository.save(customer);
			return modelMapper.map(customer, ProductDTO.class);
		}
		return null;
	}
}
