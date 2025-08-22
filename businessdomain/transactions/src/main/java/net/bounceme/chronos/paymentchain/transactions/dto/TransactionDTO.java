/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bounceme.chronos.paymentchain.transactions.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author sotobotero
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class TransactionDTO {

	private long id;
	
	private String reference;
	
	private String ibanAccount;
	
	private LocalDateTime date;
	
	private double amount;
	
	private double fee;
	
	private String description;
	
	private String status;
	
	private String channel;
}
