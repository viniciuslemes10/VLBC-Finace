package com.br.vlbc.records.transactions;

import java.time.LocalDate;

public record TransactionsFilterDatesDTO(
        LocalDate startDate,
        LocalDate endDate
) {

}
