/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.paymentchain.product.service;

import java.util.List;

import com.paymentchain.product.dto.ProductDTO;

/**
 *
 * @author federico
 */
public interface ProductService {

	List<ProductDTO> list();

	ProductDTO get(Long id);

	void deleteById(Long id);

	ProductDTO save(ProductDTO input);

	ProductDTO update(Long id, ProductDTO input);
    
}
