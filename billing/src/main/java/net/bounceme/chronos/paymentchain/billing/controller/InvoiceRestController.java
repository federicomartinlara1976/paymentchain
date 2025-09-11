/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bounceme.chronos.paymentchain.billing.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.bounceme.chronos.paymentchain.billing.entities.Invoice;
import net.bounceme.chronos.paymentchain.billing.repository.InvoiceRepository;

/**
 *
 * @author sotobotero
 */
@Tag(name = "Billing API", description = "This API serve all funcionality for management invoices")
@RestController
@RequestMapping("/billing")
public class InvoiceRestController {
    
    @Autowired
    InvoiceRepository billingRepository;
    
    @Operation(description = "Return all invoices bundled into Response", summary = "Return 204 if no data found")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
    		@ApiResponse(responseCode = "500", description = "Internal error")})
    @GetMapping()
    public List<Invoice> list() {
        return billingRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?>  get(@PathVariable long id) {
          Optional<Invoice> invoice = billingRepository.findById(id);
        if (invoice.isPresent()) {
            return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody Invoice input) {
        return null;
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Invoice input) {
        Invoice save = billingRepository.save(input);
        return ResponseEntity.ok(save);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
    	return billingRepository.findById(Long.valueOf(id))
                .map(invoice -> {
                    billingRepository.delete(invoice);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
}
