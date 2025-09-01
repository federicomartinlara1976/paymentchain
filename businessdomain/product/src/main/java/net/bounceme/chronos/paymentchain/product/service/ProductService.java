/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package net.bounceme.chronos.paymentchain.product.service;

import java.util.List;
import java.util.Optional;

import net.bounceme.chronos.paymentchain.product.dto.ProductDTO;

/**
 *
 * @author federico
 */
public interface ProductService {

	/**
	 * @return
	 */
	List<ProductDTO> list();

	/**
	 * @param id
	 * @return
	 */
	Optional<ProductDTO> get(Long id);

	/**
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * @param input
	 * @return
	 */
	ProductDTO save(ProductDTO input);

	/**
	 * @param id
	 * @param input
	 * @return
	 */
	ProductDTO update(Long id, ProductDTO input);
    
}
