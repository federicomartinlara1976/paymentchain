package com.paymentchain.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymentchain.customer.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
