package net.bounceme.chronos.paymentchain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.bounceme.chronos.paymentchain.product.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
