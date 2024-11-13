package com.br.vlbc.records;

import com.br.vlbc.enums.Method;
import com.br.vlbc.enums.Type;
import com.br.vlbc.model.Transactions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TransactionDetailsDTO(
        Long id,
        String name,
        LocalDateTime dataOfCreation,
        LocalDateTime updateDate,
        Type type,
        Method method,
        BigDecimal value,
        BigDecimal previousBalance,
        UserDetailsDTO userDTO,
        CategoryDetailsDTO categoryDTO
) {
    public static TransactionDetailsDTO fromEntityToDTO(Transactions transaction) {
        return new TransactionDetailsDTO(
                transaction.getId(),
                transaction.getName(),
                transaction.getDateOfCreation(),
                transaction.getUpdateDate(),
                transaction.getType(),
                transaction.getMethod(),
                transaction.getValue(),
                transaction.getPreviousBalance(),
                new UserDetailsDTO(transaction.getUser()),
                new CategoryDetailsDTO(transaction.getCategory())
        );
    }

    public static List<TransactionDetailsDTO> fromListEntityToListDTO(List<Transactions> transactions) {
        return transactions.stream()
                .map(TransactionDetailsDTO::fromEntityToDTO)
                .toList();
    }
}
