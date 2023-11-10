package com.seerbit.transactionstatistics.service;


import com.seerbit.transactionstatistics.dto.TransactionDto;
import com.seerbit.transactionstatistics.dto.TransactionStatisticsDto;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class TransactionService {

    private final Map<Long, TransactionDto> transactions = new ConcurrentHashMap<>();
    private static final long THIRTY_SECONDS = 30 * 1000;

    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal max = BigDecimal.ZERO;
    private BigDecimal min = BigDecimal.ZERO;
    private long count = 0;
    private long latestTimestamp = 0;

    public void addTransaction(TransactionDto transactionDto) {
        if (transactionDto == null || transactionDto.getAmount() == null || transactionDto.getTimestamp() == null) {
            throw new IllegalArgumentException("Invalid transaction");
        }

        // Validate amount
        if (transactionDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid transaction amount");
        }

        // Validate timestamp
        if (transactionDto.getTimestamp().isAfter(Instant.now())) {
            throw new IllegalArgumentException("Invalid transaction timestamp");
        }

        long currentTimestamp = transactionDto.getTimestamp().toEpochMilli();
        if (currentTimestamp - latestTimestamp <= THIRTY_SECONDS) {
            sum = sum.add(transactionDto.getAmount());
            max = max.max(transactionDto.getAmount());
            if (count == 0) {
                min = transactionDto.getAmount();
            } else {
                min = min.min(transactionDto.getAmount());
            }
            count++;
        }

        transactions.put(currentTimestamp, transactionDto);
        latestTimestamp = currentTimestamp;

    }

    public Map<Long, TransactionDto> getTransactions() {
        return transactions;
    }

    public void deleteTransactions() {
        long deleteThreshold = Instant.now().minusMillis(THIRTY_SECONDS).toEpochMilli();
        transactions.entrySet().removeIf(entry -> entry.getKey() <= deleteThreshold);
    }

    public TransactionStatisticsDto getStatistics() {
        TransactionStatisticsDto statistics = new TransactionStatisticsDto();
        statistics.setSum(sum);
        statistics.setAvg(count == 0 ? BigDecimal.ZERO : sum.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP));
        statistics.setMax(max);
        statistics.setMin(min);
        statistics.setCount(count);
        return statistics;
    }
}
