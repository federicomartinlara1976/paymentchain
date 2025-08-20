/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package net.bounceme.chronos.paymentchain.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@GetMapping()
    public ResponseEntity<List<CustomerDTO>> list() {
        return ResponseEntity.ok(customerService.list());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.get(id).orElse(null));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> put(@PathVariable Long id, @RequestBody CustomerDTO input) {
        customerService.update(id, input);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping
    public ResponseEntity<CustomerDTO> post(@RequestBody CustomerDTO input) {
    	input.getProducts().forEach(x -> x.setCustomer(input));
        return ResponseEntity.ok(customerService.save(input));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
    	customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
