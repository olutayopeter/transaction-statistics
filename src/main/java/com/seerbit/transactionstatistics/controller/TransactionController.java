package com.seerbit.transactionstatistics.controller;


import com.seerbit.transactionstatistics.dto.TransactionDto;
import com.seerbit.transactionstatistics.dto.TransactionStatisticsDto;
import com.seerbit.transactionstatistics.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.Instant;


@RestController
@RequestMapping("")
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    private static final long THIRTY_SECONDS = 30 * 1000;
    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @PostMapping("/transactions")
    public ResponseEntity<Void> addTransaction(@RequestBody TransactionDto transactionDto) {
        logger.info("add transaction......");
        try {
            // Validate transaction data
            if (transactionDto == null || transactionDto.getAmount() == null || transactionDto.getTimestamp() == null) {
                // 400 – if any of the fields are missing
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction data");
            }

            Instant now = Instant.now();
            if (now.toEpochMilli() - transactionDto.getTimestamp().toEpochMilli() > THIRTY_SECONDS) {
                // 204 – if the transaction is older than 30 seconds
                return ResponseEntity.noContent().build();
            }

            // Validate transaction date is not in the future (422)
            if (transactionDto.getTimestamp().isAfter(now)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Transaction date is in the future");
            }

            // Add transaction
            transactionService.addTransaction(transactionDto);

            // 201 – in case of success
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResponseStatusException e) {
            throw e;  // Re-throw ResponseStatusException
        } catch (Exception e) {
            logger.error("add transaction error: " + e.fillInStackTrace());
            // 422 – if any other unexpected errors occur
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid transaction data");
        }
    }



    @GetMapping("/statistics")
    public ResponseEntity<TransactionStatisticsDto> getStatistics() {
        // Get statistics from the service
        TransactionStatisticsDto statistics = transactionService.getStatistics();
        // Return the statistics with a 200 status code
        return ResponseEntity.ok(statistics);
    }


    @DeleteMapping("/transactions")
    public ResponseEntity<Void> deleteTransactions() {
        // Delete transactions
        transactionService.deleteTransactions();
        // Return an empty response body with a 204 status code
        return ResponseEntity.noContent().build();
    }


}
