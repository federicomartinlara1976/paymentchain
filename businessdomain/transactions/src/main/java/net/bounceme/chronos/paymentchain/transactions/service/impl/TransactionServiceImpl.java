package net.bounceme.chronos.paymentchain.transactions.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.bounceme.chronos.paymentchain.transactions.dto.TransactionDTO;
import net.bounceme.chronos.paymentchain.transactions.entities.Transaction;
import net.bounceme.chronos.paymentchain.transactions.repository.TransactionRepository;
import net.bounceme.chronos.paymentchain.transactions.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	@Transactional
	public void deleteById(Long id) {
		transactionRepository.findById(id).ifPresent(t -> {
        	transactionRepository.delete(t);  
        });  
	}

	@Override
	@Transactional
	public Optional<TransactionDTO> save(TransactionDTO input) {
		Transaction transaction = modelMapper.map(input, Transaction.class);
		transaction = transactionRepository.save(transaction);
		return Optional.of(modelMapper.map(transaction, TransactionDTO.class));
	}

	@Override
	@Transactional(readOnly = true)
	public List<TransactionDTO> list() {
		return transactionRepository.findAll()
				.stream()
				.map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<TransactionDTO> getById(Long id) {
		return transactionRepository.findById(id).map(x -> modelMapper.map(x, TransactionDTO.class));
	}

	@Override
	@Transactional(readOnly = true)
	public List<TransactionDTO> getByIban(String ibanAccount) {
		return transactionRepository.findByIbanAccount(ibanAccount)
				.stream()
				.map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
				.toList();
	}

	@Override
	@Transactional
	public Optional<TransactionDTO> update(Long id, TransactionDTO input) {
		Optional<Transaction> oTransaction = transactionRepository.findById(id);
		
		if (oTransaction.isPresent()) {
			Transaction t = oTransaction.get();
			
			t.setAmount(input.getAmount());
            t.setChannel(input.getChannel());
            t.setDate(input.getDate());
            t.setDescription(input.getDescription());
            t.setFee(input.getFee());
            t.setIbanAccount(input.getIbanAccount());
            t.setReference(input.getReference());
            t.setStatus(input.getStatus());
            
            t = transactionRepository.save(t);
            return Optional.of(modelMapper.map(t, TransactionDTO.class));
		}
		
		return Optional.empty();
	}
}
