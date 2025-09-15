/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bounceme.chronos.paymentchain.billing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author sotobotero
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "InvoiceResponse", description = "Model represent a invoice on database")
public class InvoiceResponse {
	
	private Long invoiceId;
	
	@Schema(name = "customerId", requiredMode = RequiredMode.REQUIRED, example = "2", defaultValue = "1", description = "Unique id of customer that represent the owner of invoice")
	private Long customer;
	
	@Schema(name = "number", requiredMode = RequiredMode.REQUIRED, example = "3", defaultValue = "8", description = "Number given on fisical invoice")
	private String number;
	
	private String detail;
	
	private Double amount;
}
