package com.br.vlbc.records;

import com.br.vlbc.enums.Method;
import com.br.vlbc.enums.Type;

import java.math.BigDecimal;

public record TransactionsDTO(
        String name,
        Type type,
        Method method,
        BigDecimal value,
        UserDetailsDTO userDTO,
        CategoryDetailsDTO categoryDTO
) {
}
