/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.customer.controller;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.net.HttpHeaders;
import com.paymentchain.customer.dto.CustomerDTO;
import com.paymentchain.customer.service.CustomerService;

import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
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
    
    private WebClient.Builder webClientBuilder;
    
    public CustomerController(Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}
    
    HttpClient client = HttpClient.create()
    		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
    		.option(ChannelOption.SO_KEEPALIVE, true)
    		.option(EpollChannelOption.TCP_KEEPIDLE, 300)
    		.option(EpollChannelOption.TCP_KEEPINTVL, 60)
    		.responseTimeout(Duration.ofSeconds(1))
    		.doOnConnected(connection -> {
    			connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
    			connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
    		});

	@GetMapping()
    public ResponseEntity<List<CustomerDTO>> list() {
        return ResponseEntity.ok(customerService.list());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.get(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> put(@PathVariable Long id, @RequestBody CustomerDTO input) {
        return ResponseEntity.ok(customerService.update(id, input));
    }
    
    @PostMapping
    public ResponseEntity<CustomerDTO> post(@RequestBody CustomerDTO input) {
    	input.getProducts().forEach(x -> x.setCustomer(input));
        return ResponseEntity.ok(customerService.save(input));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
    	customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    private String getProductName(Long id) {
    	WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
    			.baseUrl("http://192.168.1.135:8090/product")
    			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    			.defaultUriVariables(Collections.singletonMap("url", "http://192.168.1.135:8090/product"))
    			.build();
    	
    	JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
    			.retrieve().bodyToMono(JsonNode.class).block();
    	
    	String name = block.get("name").asText();
    	return name;
    }
    
}
