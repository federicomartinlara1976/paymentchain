/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.bounceme.chronos.paymentchain.customer.service.impl;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import net.bounceme.chronos.paymentchain.customer.dto.CustomerDTO;
import net.bounceme.chronos.paymentchain.customer.entities.Customer;
import net.bounceme.chronos.paymentchain.customer.repository.CustomerRepository;
import net.bounceme.chronos.paymentchain.customer.service.CustomerService;
import reactor.netty.http.client.HttpClient;

/**
 *
 * @author federico
 */
@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Value("${application.product.service}")
    private String productService;
    
    @Value("${application.transaction.service}")
    private String transactionService;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
    private WebClient.Builder webClientBuilder;
	
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

	@Override
	@Transactional(readOnly = true)
	public List<CustomerDTO> list() {
		return customerRepository.findAll()
				.stream()
				.map(customer -> modelMapper.map(customer, CustomerDTO.class))
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CustomerDTO> get(Long id) {
		return customerRepository.findById(id).map(c -> modelMapper.map(c, CustomerDTO.class));
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		customerRepository.deleteById(id);
	}

	@Override
	@Transactional
	public CustomerDTO save(CustomerDTO input) {
		Customer customer = modelMapper.map(input, Customer.class);
		customer = customerRepository.save(customer);
		return modelMapper.map(customer, CustomerDTO.class);
	}

	@Override
	@Transactional
	public void update(Long id, CustomerDTO input) {
		customerRepository.findById(id).ifPresent(c -> {
			c.setName(input.getName());
			c.setPhone(input.getPhone());

			customerRepository.save(c);
		});
	}

	@Override
	public Optional<CustomerDTO> findByCode(String code) {
		Customer c = customerRepository.findByCode(code);
		
		return (!Objects.isNull(c)) ? Optional.of(modelMapper.map(c, CustomerDTO.class)) : Optional.empty();
	}
	
	/**
     * Call Product Microservice , find a product by Id and return it name
     *
     * @param id of product to find
     * @return name of product if it was find
     */
	@Override
    public String getProductName(Long id) {
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
	@Override
    public List<?> getTransactions(String iban) {
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
