package net.bounceme.chronos.paymentchain.transactions.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenericConfig {

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
