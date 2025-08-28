/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package net.bounceme.chronos.paymentchain.customer.controller;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import net.bounceme.chronos.paymentchain.customer.dto.CustomerDTO;
import net.bounceme.chronos.paymentchain.customer.dto.CustomerProductDTO;
import net.bounceme.chronos.paymentchain.customer.service.CustomerService;
import reactor.netty.http.client.HttpClient;

/**
 *
 * @author federico
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    @Value("${application.product.service}")
    private String productService;
    
    @Value("${application.transaction.service}")
    private String transactionService;
	
	//webClient requires HttpClient library to work propertly       
    HttpClient client = HttpClient.create()
            //Connection Timeout: is a period within which a connection between a client and a server must be established
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            //Response Timeout: The maximun time we wait to receive a response after sending a request
            .responseTimeout(Duration.ofSeconds(1))
            // Read and Write Timeout: A read timeout occurs when no data was read within a certain 
            //period of time, while the write timeout when a write operation cannot finish at a specific time
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

	@GetMapping()
    public ResponseEntity<List<CustomerDTO>> list() {
		List<CustomerDTO> customers = customerService.list();
		if (CollectionUtils.isNotEmpty(customers)) {
			return ResponseEntity.ok(customers);
		}
		else {
			return ResponseEntity.noContent().build();
		}
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> get(@PathVariable("id") Long id) {
    	Optional<CustomerDTO> oCustomer = customerService.get(id);
    	if (oCustomer.isPresent()) {
    		return ResponseEntity.ok(oCustomer.get());
    	}
    	else {
    		return ResponseEntity.notFound().build();
    	}
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> put(@PathVariable("id") Long id, @RequestBody CustomerDTO input) {
        customerService.update(id, input);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping
    public ResponseEntity<CustomerDTO> post(@RequestBody CustomerDTO input) {
    	input.getProducts().forEach(x -> x.setCustomer(input));
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.save(input));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    	customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/full")
    public CustomerDTO getByCode(@RequestParam("code") String code) {
        CustomerDTO customer = customerService.findByCode(code);
        
        if (!Objects.isNull(customer)) {
            List<CustomerProductDTO> products = customer.getProducts();

            //for each product find it name
            products.forEach(x -> {
                String productName = getProductName(x.getProductId());
                x.setProductName(productName);
            });
            
            //find all transactions that belong this account number
            List<?> transactions = getTransactions(customer.getIban());
            customer.setTransactions(transactions);
        }
        
        return customer;
    }
    
    /**
     * Call Product Microservice , find a product by Id and return it name
     *
     * @param id of product to find
     * @return name of product if it was find
     */
    private String getProductName(Long id) {
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl(productService)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", productService))
                .build();
        
        JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();
        
        return (!Objects.isNull(block)) ? block.get("name").asText() : StringUtils.EMPTY;
    }
    
    /**
     * Call Transaction Microservice and Find all transaction that belong to the
     * account give
     *
     * @param iban account number of the customer
     * @return All transaction that belong this account
     */
    private List<?> getTransactions(String iban) {
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl(transactionService)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();       
        
        Optional<List<?>> transactionsOptional = Optional.ofNullable(build.method(HttpMethod.GET)
        .uri(uriBuilder -> uriBuilder
                .path("/customer/transactions")
                .queryParam("ibanAccount", iban)
                .build())
        .retrieve()
        .bodyToFlux(Object.class)
        .collectList()
        .block());       

        return transactionsOptional.orElse(Collections.emptyList());
    }
}
