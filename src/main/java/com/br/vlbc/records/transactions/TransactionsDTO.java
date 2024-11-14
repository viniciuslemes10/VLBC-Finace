package com.br.vlbc.records.transactions;

import com.br.vlbc.enums.Method;
import com.br.vlbc.enums.Type;
import com.br.vlbc.records.users.UserDetailsDTO;
import com.br.vlbc.records.categories.CategoryDetailsDTO;

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
