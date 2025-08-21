/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bounceme.chronos.paymentchain.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.bounceme.chronos.paymentchain.billing.entities.Invoice;

/**
 *
 * @author sotobotero
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
}
