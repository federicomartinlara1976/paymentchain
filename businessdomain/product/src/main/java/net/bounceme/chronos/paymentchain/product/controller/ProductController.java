/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package net.bounceme.chronos.paymentchain.product.controller;

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

import net.bounceme.chronos.paymentchain.product.dto.ProductDTO;
import net.bounceme.chronos.paymentchain.product.service.ProductService;

/**
 *
 * @author federico
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * @return
     */
    @GetMapping()
    public ResponseEntity<List<ProductDTO>> list() {
        return ResponseEntity.ok(productService.list());
    }
    
    /**
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.get(id));
    }
    
    /**
     * @param id
     * @param input
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> put(@PathVariable("id") Long id, @RequestBody ProductDTO input) {
        return ResponseEntity.ok(productService.update(id, input));
    }
    
    /**
     * @param input
     * @return
     */
    @PostMapping
    public ResponseEntity<ProductDTO> post(@RequestBody ProductDTO input) {
        return ResponseEntity.ok(productService.save(input));
    }
    
    /**
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    	productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
