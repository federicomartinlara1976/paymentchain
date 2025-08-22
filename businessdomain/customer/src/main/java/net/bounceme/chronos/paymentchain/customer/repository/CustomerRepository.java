package net.bounceme.chronos.paymentchain.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.bounceme.chronos.paymentchain.customer.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	@Query("select c from Customer c where c.code = ?1")
	Customer findByCode(String code);

	@Query("select c from Customer c where c.iban = ?1")
	Customer findByAccount(String iban);
}
