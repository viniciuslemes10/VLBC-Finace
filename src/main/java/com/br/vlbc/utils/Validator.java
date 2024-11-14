package com.br.vlbc.utils;

import com.br.vlbc.exceptions.BalanceInvalidException;
import com.br.vlbc.exceptions.DatesIllegalArgumentException;
import com.br.vlbc.exceptions.DatesNotNullException;
import com.br.vlbc.exceptions.EnumInvalidException;
import com.br.vlbc.records.transactions.TransactionsFilterDatesDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class Validator {

    public void checkValueIsNonNegative(BigDecimal value) {
        if(value.compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceInvalidException("Erro ao processar transação: Valor não pode ser negativo.");
        }
    }

    public void assertMinLessThanMax(BigDecimal minValue, BigDecimal maxValue) {
        checkValueIsNonNegative(minValue);
        checkValueIsNonNegative(maxValue);
        if(maxValue.compareTo(minValue) < 0) {
            throw new BalanceInvalidException("O valor máximo não pode ser menor que o valor mínimo.");
        }
    }

    public <T extends Enum<T>> T convertToEnum(Class<T> enumClass, String givenValue) {
        try {
            return Enum.valueOf(enumClass, givenValue);
        } catch (RuntimeException ex) {
            throw new EnumInvalidException("Valor inválido para o enum.");
        }
    }

    public void validateRequiredDates(TransactionsFilterDatesDTO data) {
        if(data.startDate() == null || data.endDate() == null) {
            throw new DatesNotNullException("Os campos são obrigatórios.");
        }
    }

    public LocalDateTime convertStartDateToStartOfDay(LocalDate startDate) {
        return startDate.atStartOfDay();
    }

    public LocalDateTime convertEndDateToEndOfDay(LocalDate endDate) {
        return endDate.atTime(LocalTime.MAX);
    }

    public void validateStartDateBeforeEndDate(TransactionsFilterDatesDTO data) {
        if (data.startDate().isAfter(data.endDate())) {
            throw new DatesIllegalArgumentException("A data de início não pode ser posterior à data de término.");
        }
    }
}
