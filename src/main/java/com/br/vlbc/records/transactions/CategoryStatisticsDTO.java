package com.br.vlbc.records.transactions;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CategoryStatisticsDTO(
        String name,
        BigDecimal totalSpent,
        BigDecimal percentage) {

    public CategoryStatisticsDTO(String name, BigDecimal totalSpent, BigDecimal percentage) {
        this.name = name;
        this.totalSpent = totalSpent.setScale(2, RoundingMode.HALF_UP);
        this.percentage = percentage.setScale(2, RoundingMode.HALF_UP);
    }
}
