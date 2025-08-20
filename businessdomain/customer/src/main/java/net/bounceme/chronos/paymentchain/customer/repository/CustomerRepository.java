package net.bounceme.chronos.paymentchain.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.bounceme.chronos.paymentchain.customer.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
