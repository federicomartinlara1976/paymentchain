package net.bounceme.chronos.paymentchain.transactions.service;

import java.util.List;
import java.util.Optional;

import net.bounceme.chronos.paymentchain.transactions.dto.TransactionDTO;

public interface TransactionService {

	void deleteById(Long id);

	Optional<TransactionDTO> save(TransactionDTO input);

	List<TransactionDTO> list();

	Optional<TransactionDTO> getById(Long id);

	List<TransactionDTO> getByIban(String ibanAccount);

	Optional<TransactionDTO> update(Long id, TransactionDTO input);
}
