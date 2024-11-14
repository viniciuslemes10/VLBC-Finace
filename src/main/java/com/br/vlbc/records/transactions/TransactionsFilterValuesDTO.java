package com.br.vlbc.records.transactions;

import java.math.BigDecimal;

public record TransactionsFilterValuesDTO(
        BigDecimal maxValue,
        BigDecimal minValue
) {
}
