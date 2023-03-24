package com.patroclos.service;

import com.patroclos.common.dto.PaymentRequestDTO;
import com.patroclos.common.dto.PaymentResponseDTO;
import com.patroclos.common.enums.PaymentStatus;
import com.patroclos.entity.CustomerAccount;
import com.patroclos.repository.CustomerAccountRepository;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

	private static org.slf4j.Logger Logger = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	private CustomerAccountRepository customerAccountRepository;

	public PaymentResponseDTO debitAccount(PaymentRequestDTO requestDTO){
		CustomerAccount customer = customerAccountRepository.findByCustomerId(requestDTO.getCustomerId()).block();
		if (customer == null)
			throw new RuntimeException("Customer account not found");

		PaymentResponseDTO responseDTO = new PaymentResponseDTO();
		responseDTO.setAmount(requestDTO.getAmount());
		responseDTO.setCustomerId(requestDTO.getCustomerId());
		responseDTO.setOrderId(requestDTO.getOrderId());
		responseDTO.setStatus(PaymentStatus.REJECTED);

		if(customer.getBalance() >= requestDTO.getAmount()){
			responseDTO.setStatus(PaymentStatus.APPROVED);
			customer.setBalance(customer.getBalance() - requestDTO.getAmount());
			customerAccountRepository.save(customer);
			Logger.info("Customer account with customerId " + customer.getCustomerId() + ", debited with the amount of "+ requestDTO.getAmount() + " euros");
		}
		else
			Logger.info("Customer account with customerId " + customer.getCustomerId() + ", not enough funds + {" + customer.getBalance() +  "} + to be debited with the amount of "+ requestDTO.getAmount() + " euros");
		
		return responseDTO;
	}

	public void creditAccount(PaymentRequestDTO requestDTO){
		CustomerAccount customer = customerAccountRepository.findByCustomerId(requestDTO.getCustomerId()).block();
		if (customer == null)
			throw new RuntimeException("Customer account not found");
		
		customer.setBalance(customer.getBalance() + requestDTO.getAmount());
		customerAccountRepository.save(customer);
		
		Logger.info("Customer account with customerId "+ customer.getCustomerId() +" credited  with the amoutn of "+ requestDTO.getAmount() + " euros");
	}

}
