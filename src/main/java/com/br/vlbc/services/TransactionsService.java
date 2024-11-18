package com.br.vlbc.services;

import com.br.vlbc.enums.Method;
import com.br.vlbc.enums.Type;
import com.br.vlbc.exceptions.TransactionNotFoundException;
import com.br.vlbc.model.Category;
import com.br.vlbc.model.Transactions;
import com.br.vlbc.model.User;
import com.br.vlbc.records.transactions.*;
import com.br.vlbc.repositories.TransactionRepository;
import com.br.vlbc.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TransactionsService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Validator validator;

    @Transactional
    public Transactions createTransaction(TransactionsDTO data) {
        var transaction = processDepositOrWithdrawal(data);
        return repository.save(transaction);
    }

    public Transactions processDepositOrWithdrawal(TransactionsDTO data) {
        var user = userService.findById(data.userDTO().id());
        var category = categoryService.findById(data.categoryDTO().id());

        validator.checkValueIsNonNegative(data.value());

        var transaction = createTransaction(data, category, user);

        categoryService.checkTransactionType(category.getType(), transaction.getType());

        if(transaction.getType().equals(Type.EXPENSES)) {
            userService.withdrawTransactionBalance(user, transaction);
            return transaction;
        } else {
            userService.depositTransactionBalance(user, transaction);
            return transaction;
        }
    }

    private Transactions createTransaction(TransactionsDTO data, Category category, User user) {
        return new Transactions(data, category, user);
    }

    public Transactions findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found."));
    }

    public List<Transactions> findAll(Long idUser) {
        return findUserTransactions(idUser);
    }

    public void delete(Long id) {
        var transaction = findById(id);
        var user = userService.findById(transaction.getUser().getId());

        userService.updateUserBalanceBasedOnTransactionType(user, transaction);
        repository.delete(transaction);
    }

    public List<Transactions> findByType(TransactionsFilterDTO data, Long id) {
        Type typeEnum = validator.convertToEnum(Type.class, data.given());

        return findByFilter(data, id, t -> t.getType().equals(typeEnum));
    }

    public List<Transactions> findByName(TransactionsFilterDTO data, Long id) {
        return findByFilter(data, id, t -> t.getName().equals(data.given()));
    }

    public List<Transactions> findByCategory(TransactionsFilterDTO data, Long id) {
        return findByFilter(data, id, t -> t.getCategory().getName().equals(data.given()));
    }

    public List<Transactions> findByMethod(TransactionsFilterDTO data, Long id) {
        Method method = validator.convertToEnum(Method.class, data.given());

        return findByFilter(data, id, t -> t.getMethod().equals(method));
    }

    public List<Transactions> findByDates(TransactionsFilterDatesDTO data, Long id) {
        validator.validateRequiredDates(data);
        validator.validateStartDateBeforeEndDate(data);

        var startDateTime = validator.convertStartDateToStartOfDay(data.startDate());
        var endDateTime = validator.convertEndDateToEndOfDay(data.endDate());

        return findByFilter(data, id, t -> !t.getDateOfCreation().isBefore(startDateTime) && !t.getUpdateDate().isAfter(endDateTime));
    }

    public List<Transactions> findByValues(TransactionsFilterValuesDTO data, Long id) {
        validator.assertMinLessThanMax(data.minValue(), data.maxValue());

        return findByFilter(data, id, t -> t.getValue().compareTo(data.minValue()) >= 0 && t.getValue().compareTo(data.maxValue()) <= 0);
    }

    public <T extends Record> List<Transactions> findByFilter(
            T data, Long id, Function<Transactions, Boolean> filter) {
        var listOfUserTransaction = findUserTransactions(id);
        return listOfUserTransaction.stream()
                .filter(filter::apply)
                .collect(Collectors.toList());
    }

    private List<Transactions> findUserTransactions(Long id) {
        return repository.findAllOfIdUser(id);
    }

    public List<CategoryStatisticsDTO> getCategoryPercentages(Long id) {
        var transactions = findUserTransactions(id);

        BigDecimal totalValue = transactions.stream()
                .map(Transactions::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if(totalValue.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("Nenhuma transação encontrada ou total geral é zero.");
        }

        Map<String, BigDecimal> totalsByCategory = transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory().getName(),
                        Collectors.mapping(
                                Transactions::getValue,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        return totalsByCategory.entrySet().stream()
                .map(entry -> {
                    String categoryName = entry.getKey();
                    BigDecimal totalSpent = entry.getValue();
                    BigDecimal percentage = totalSpent
                            .divide(totalValue, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));
                    return new CategoryStatisticsDTO(categoryName, totalSpent, percentage);
                })
                .collect(Collectors.toList());
    }

    public List<Transactions> getTransactionsForUserInDateRange(Long id, LocalDateTime startOfMonth, LocalDateTime endOfMonth) {
        return repository.findByUserIdAndDateBetween(id, startOfMonth, endOfMonth);
    }
}