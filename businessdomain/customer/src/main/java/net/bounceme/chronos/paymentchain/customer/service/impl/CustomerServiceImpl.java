/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.bounceme.chronos.paymentchain.customer.service.impl;

import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import lombok.SneakyThrows;
import net.bounceme.chronos.paymentchain.customer.dto.CustomerDTO;
import net.bounceme.chronos.paymentchain.customer.dto.CustomerProductDTO;
import net.bounceme.chronos.paymentchain.customer.entities.Customer;
import net.bounceme.chronos.paymentchain.customer.exceptions.BussinessRuleException;
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
    private String productServiceUrl;
    
    @Value("${application.transaction.service}")
    private String transactionServiceUrl;

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
	@SneakyThrows(BussinessRuleException.class)
	public CustomerDTO save(CustomerDTO input) {
		if (CollectionUtils.isNotEmpty(input.getProducts())) {
			for (Iterator<CustomerProductDTO> it = input.getProducts().iterator(); it.hasNext();) {
				CustomerProductDTO dto = it.next();
				String productName = getProductName(dto.getProductId());
				if (StringUtils.isBlank(productName)) {
					throw new BussinessRuleException("1025", "Error validacion, el producto con id " + dto.getProductId() + " no existe", HttpStatus.PRECONDITION_REQUIRED);
				}
				else {
					dto.setCustomer(input);
				}
			}
		}
		
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
	public Optional<CustomerDTO> getByCode(String code) {
		Customer c = customerRepository.findByCode(code);
        
        if (!Objects.isNull(c)) {
        	CustomerDTO customer = modelMapper.map(c, CustomerDTO.class);
        	
        	// Set the products completed information
            customer.setProducts(c.getProducts().stream()
            		.map(p -> {
            			CustomerProductDTO cp = modelMapper.map(p, CustomerProductDTO.class);
            			cp.setProductName(getProductName(cp.getProductId()));
                    	return cp;
            		})
            		.toList());
            
            // find all transactions that belong this account number
            List<?> transactions = getTransactions(customer.getIban());
            customer.setTransactions(transactions);
            
            return Optional.of(customer);
        }
        
        return Optional.empty();
	}
	
	private String getProductName(Long id) {
        WebClient webClient = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl(productServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", productServiceUrl))
                .build();
        
        JsonNode block = webClient.method(HttpMethod.GET).uri("/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();
        
        return (!Objects.isNull(block)) ? block.get("name").asText() : StringUtils.EMPTY;
    }
    
    private List<?> getTransactions(String iban) {
        WebClient webClient = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl(transactionServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();       
        
        Optional<List<?>> oTransactions = Optional.ofNullable(webClient.method(HttpMethod.GET)
        		.uri(uriBuilder -> uriBuilder
        				.path("/customer/transactions")
        				.queryParam("ibanAccount", iban)
        				.build())
        		.retrieve()
        		.bodyToFlux(Object.class)
        		.collectList()
        		.block());       

        return oTransactions.orElse(Collections.emptyList());
    }
}
