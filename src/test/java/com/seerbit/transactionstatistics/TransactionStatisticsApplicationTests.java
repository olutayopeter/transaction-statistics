package com.seerbit.transactionstatistics;

import com.seerbit.transactionstatistics.dto.TransactionDto;
import com.seerbit.transactionstatistics.dto.TransactionStatisticsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionStatisticsApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void testGetStatistics() {
		ResponseEntity<TransactionStatisticsDto> response = restTemplate.getForEntity("/statistics", TransactionStatisticsDto.class);
		// Add your assertions here
		System.out.println("response: " + response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		// Add assertions to validate the response content
	}

//	@Test
//	void testAddTransaction() {
//		// Create a test transaction and send a POST request to add it
//		TransactionDto transactionDto = new TransactionDto();
//		transactionDto.setAmount(new BigDecimal("72.34"));
//		transactionDto.setTimestamp(Instant.now());
//
//		ResponseEntity<Void> response = restTemplate.postForEntity("/transactions", transactionDto, Void.class);
//		// Add your assertions here
//		System.out.println("response: " + response.getBody());
//		assertEquals(HttpStatus.CREATED, response.getStatusCode());
//	}

//	@Test
//	void testAddTransactionOlderThanThirtySeconds() {
//		// Create a test transaction with a timestamp older than 30 seconds
//		TransactionDto transactionDto = new TransactionDto();
//		transactionDto.setAmount(new BigDecimal("12.34"));
//		transactionDto.setTimestamp(Instant.now().minusSeconds(31));
//
//		// Send a POST request to add the transaction and assert a response status of 204
//		ResponseEntity<Void> response = restTemplate.postForEntity("/transactions", transactionDto, Void.class);
//		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//	}
//
//	@Test
//	void testAddTransactionInvalidData() {
//		// Create a test transaction with invalid data
//		TransactionDto transaction = new TransactionDto();
//		transaction.setAmount(new BigDecimal("invalid")); // Invalid amount
//		transaction.setTimestamp(Instant.now());
//
//		// Send a POST request to add the transaction and assert a response status of 400 (Bad Request)
//		ResponseEntity<Void> response = restTemplate.postForEntity("/transactions", transaction, Void.class);
//		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//	}
//
//	@Test
//	void testDeleteTransactions() {
//		ResponseEntity<Void> response = restTemplate.exchange("/transactions", HttpMethod.DELETE, null, Void.class);
//		// Add your assertions here
//		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//	}
//
//	@Test
//	void testDeleteTransactionsWithInvalidData() {
//		// Create an invalid request (e.g., with a request body)
//		ResponseEntity<Void> response = restTemplate.exchange("/transactions", HttpMethod.DELETE, new HttpEntity<>(null), Void.class);
//		// Add your assertions here, e.g., expecting a response status of 400 (Bad Request)
//		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//	}
}
