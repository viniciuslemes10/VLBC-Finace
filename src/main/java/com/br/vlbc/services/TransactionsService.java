package com.br.vlbc.services;

import com.br.vlbc.enums.Method;
import com.br.vlbc.enums.Type;
import com.br.vlbc.exceptions.BalanceInvalidException;
import com.br.vlbc.model.Transactions;
import com.br.vlbc.records.transactions.TransactionsDTO;
import com.br.vlbc.records.transactions.TransactionsFilterDTO;
import com.br.vlbc.records.transactions.TransactionsFilterDatesDTO;
import com.br.vlbc.records.transactions.TransactionsFilterValuesDTO;
import com.br.vlbc.repositories.TransactionRepository;
import com.br.vlbc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
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
    private UserRepository userRepository;

    @Transactional
    public Transactions deposit(TransactionsDTO data) {
        var user = userService.findById(data.userDTO().id());
        var category = categoryService.findById(data.categoryDTO().id());

        if (data.value().compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceInvalidException("Valor inválido!");
        }
        var typeData = Type.valueOf(String.valueOf(data.type()));

        if (typeData.equals(Type.EXPENSES)) {
            throw new RuntimeException("Tipo inválido, escolha INVESTMENT ou REVENUE.");
        }

        var transaction = new Transactions(data, category, user);

        if (!category.getType().equals(transaction.getType())) {
            throw new BalanceInvalidException("Erro ao processar transação: Tipo de categoria incompatível!");
        }

        user.setBalance(user.getBalance().add(transaction.getValue()));
        userRepository.save(user);

        return repository.save(transaction);

    }

    public Transactions toRemove(TransactionsDTO data) {
        var user = userService.findById(data.userDTO().id());
        var category = categoryService.findById(data.categoryDTO().id());

        if (data.value().compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceInvalidException("Valor inválido!");
        }
        var typeData = Type.valueOf(String.valueOf(data.type()));

        if (!typeData.equals(Type.EXPENSES)) {
            throw new RuntimeException("Tipo inválido.");
        }

        var transaction = new Transactions(data, category, user);

        if (!category.getType().equals(transaction.getType())) {
            throw new BalanceInvalidException("Erro ao processar transação: Tipo de categoria incompatível!");
        }

        if (user.getBalance().compareTo(transaction.getValue()) >= 0) {
            user.setBalance(user.getBalance().subtract(transaction.getValue()));
            userRepository.save(user);

            return repository.save(transaction);
        } else {
            throw new BalanceInvalidException("Saldo insuficiente!");
        }
    }

    public Transactions findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found."));
    }

    public List<Transactions> findAll(Long idUser) {
        return findUserTransactions(idUser);
    }

    public void delete(Long id) {
        var transaction = findById(id);
        var user = userService.findById(transaction.getUser().getId());

        if(transaction.getType() == Type.REVENUE || transaction.getType() == Type.INVESTMENT) {
            user.setBalance(user.getBalance().subtract(transaction.getValue()));
        } else {
            user.setBalance(user.getBalance().add(transaction.getValue()));
        }

        userRepository.save(user);
        repository.delete(transaction);
    }

    public List<Transactions> findByType(TransactionsFilterDTO data, Long id) {
        Type typeEnum;
        try {
            typeEnum = Type.valueOf(data.given());
        } catch (RuntimeException e) {
            throw new RuntimeException("Tipo inválido.");
        }

        return findByFilter(data, id,
                t -> t.getType().equals(typeEnum));
    }

    public List<Transactions> findByName(TransactionsFilterDTO data, Long id) {
        return findByFilter(data, id,
                t -> t.getName().equals(data.given()));
    }

    public List<Transactions> findByCategory(TransactionsFilterDTO data, Long id) {
        return findByFilter(data, id,
                t -> t.getCategory().getName().equals(data.given()));
    }

    public List<Transactions> findByMethod(TransactionsFilterDTO data, Long id) {
        Method method;
        try {
            method = Method.valueOf(data.given());
        } catch (RuntimeException e) {
            throw new RuntimeException("Método inválido.");
        }

        return findByFilter(data, id,
                t -> t.getMethod().equals(method));
    }

    public List<Transactions> findByDates(TransactionsFilterDatesDTO data, Long id) {
        if(data.startDate() == null || data.endDate() == null) {
            throw new RuntimeException("Os campos são obrigatórios.");
        }
        var startDateTime = data.startDate().atStartOfDay();
        var endDateTime = data.endDate().atTime(LocalTime.MAX);

        return findByFilter(data, id,
                     t -> !t.getDateOfCreation().isBefore(startDateTime) &&
                          !t.getUpdateDate().isAfter(endDateTime));
    }

    public List<Transactions> findByValues(TransactionsFilterValuesDTO data, Long id) {
        if (data.minValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor mínimo não pode ser negativo.");
        }
        if (data.maxValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor máximo não pode ser negativo.");
        }
        if (data.maxValue().compareTo(data.minValue()) < 0) {
            throw new IllegalArgumentException("O valor máximo não pode ser menor que o valor mínimo.");
        }

        return findByFilter(data, id,
                            t -> t.getValue().compareTo(data.minValue()) >= 0 &&
                                    t.getValue().compareTo(data.maxValue()) <= 0);
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
}