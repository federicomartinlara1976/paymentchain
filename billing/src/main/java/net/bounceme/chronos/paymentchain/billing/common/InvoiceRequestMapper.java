package net.bounceme.chronos.paymentchain.billing.common;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import net.bounceme.chronos.paymentchain.billing.dto.InvoiceRequest;
import net.bounceme.chronos.paymentchain.billing.entities.Invoice;

public interface InvoiceRequestMapper {

	@Mappings({
		@Mapping(source = "customer", target = "customerId")
	})
	Invoice InvoiceRequestToInvoice(InvoiceRequest source);
	
	List<Invoice> InvoiceRequestListToInvoiceList(List<InvoiceRequest> source);
	
	@InheritInverseConfiguration
	InvoiceRequest InvoiceToInvoiceRequest(Invoice source);
	
	@InheritInverseConfiguration
	List<InvoiceRequest> InvoiceListToInvoiceRequestList(List<Invoice> source);
}
