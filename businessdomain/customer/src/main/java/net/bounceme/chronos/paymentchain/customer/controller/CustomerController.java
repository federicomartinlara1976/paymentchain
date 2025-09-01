/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package net.bounceme.chronos.paymentchain.customer.controller;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.bounceme.chronos.paymentchain.customer.dto.CustomerDTO;
import net.bounceme.chronos.paymentchain.customer.service.CustomerService;

/**
 *
 * @author federico
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;

	/**
	 * @return
	 */
	@GetMapping()
    public ResponseEntity<List<CustomerDTO>> list() {
		List<CustomerDTO> customers = customerService.list();
		
		return (CollectionUtils.isNotEmpty(customers))
			? ResponseEntity.ok(customers)
			: ResponseEntity.noContent().build();
    }
    
    /**
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> get(@PathVariable("id") Long id) {
    	Optional<CustomerDTO> oCustomer = customerService.get(id);
    	
    	return (oCustomer.isPresent())
    		? ResponseEntity.ok(oCustomer.get())
    		: ResponseEntity.notFound().build();
    }
    
    /**
     * @param id
     * @param input
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> put(@PathVariable("id") Long id, @RequestBody CustomerDTO input) {
        customerService.update(id, input);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * @param input
     * @return
     */
    @PostMapping
    public ResponseEntity<CustomerDTO> post(@RequestBody CustomerDTO input) {
    	return ResponseEntity.status(HttpStatus.CREATED).body(customerService.save(input));
    }
    
    /**
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    	customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/full")
    public ResponseEntity<CustomerDTO> getByCode(@RequestParam("code") String code) {
    	Optional<CustomerDTO> oCustomer = customerService.getByCode(code);
    	return (oCustomer.isPresent())
    		? ResponseEntity.ok(oCustomer.get())
    		: ResponseEntity.notFound().build();
    }
    
    
}
