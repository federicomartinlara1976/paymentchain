/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bounceme.chronos.paymentchain.transactions.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.bounceme.chronos.paymentchain.transactions.dto.TransactionDTO;
import net.bounceme.chronos.paymentchain.transactions.service.TransactionService;

/**
 *
 * @author Federico Martín
 */
@RestController
@RequestMapping("/transaction")
public class TransactionRestController {
    
    @Autowired
    TransactionService transactionService;
      
    /**
     * @return
     */
    @GetMapping()
    public List<TransactionDTO> list() {
        return transactionService.list();
    }
    
    /**
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> get(@PathVariable("id") Long id) {      
         return transactionService.getById(id).map(x -> ResponseEntity.ok(x)).orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * @param ibanAccount
     * @return
     */
    @GetMapping("/customer/transactions")
    public List<TransactionDTO> get(@RequestParam("ibanAccount") String ibanAccount) {
      return transactionService.getByIban(ibanAccount);
    }
    
    /**
     * @param id
     * @param input
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> put(@PathVariable("id") Long id, @RequestBody TransactionDTO input) {
        return transactionService.update(id, input).map(x -> ResponseEntity.ok(x)).orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * @param input
     * @return
     */
    @PostMapping
    public ResponseEntity<?> post(@RequestBody TransactionDTO input) {
    	return transactionService.save(input).map(x -> ResponseEntity.ok(x)).orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        transactionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
}
