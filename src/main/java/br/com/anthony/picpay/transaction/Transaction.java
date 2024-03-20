package br.com.anthony.picpay.transaction;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Table(name = "TRANSACTIONS")
public record Transaction(
        @Id Long id,
        Long payer,
        Long payee,
        BigDecimal value,
        @CreatedDate LocalDateTime createdAt
) {
    public Transaction {
        value = value.setScale(2, RoundingMode.FLOOR);
    }
}
