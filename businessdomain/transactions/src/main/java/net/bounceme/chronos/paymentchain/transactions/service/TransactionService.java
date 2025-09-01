package net.bounceme.chronos.paymentchain.transactions.service;

import java.util.List;
import java.util.Optional;

import net.bounceme.chronos.paymentchain.transactions.dto.TransactionDTO;

/**
 * 
 */
public interface TransactionService {

	/**
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * @param input
	 * @return
	 */
	Optional<TransactionDTO> save(TransactionDTO input);

	/**
	 * @return
	 */
	List<TransactionDTO> list();

	/**
	 * @param id
	 * @return
	 */
	Optional<TransactionDTO> getById(Long id);

	/**
	 * @param ibanAccount
	 * @return
	 */
	List<TransactionDTO> getByIban(String ibanAccount);

	/**
	 * @param id
	 * @param input
	 * @return
	 */
	Optional<TransactionDTO> update(Long id, TransactionDTO input);
}
