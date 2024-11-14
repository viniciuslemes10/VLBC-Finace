package com.br.vlbc.services;

import com.br.vlbc.enums.Type;
import com.br.vlbc.exceptions.BalanceInvalidException;
import com.br.vlbc.model.Transactions;
import com.br.vlbc.records.transactions.TransactionsDTO;
import com.br.vlbc.records.transactions.TransactionsFilterDTO;
import com.br.vlbc.repositories.TransactionRepository;
import com.br.vlbc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
    public Transactions create(TransactionsDTO data) {
        var user = userService.findById(data.userDTO().id());
        var category = categoryService.findById(data.categoryDTO().id());

        if (data.value().compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceInvalidException("Valor inválido!");
        }

        var transaction = new Transactions(data, category, user);

        if (!category.getType().equals(transaction.getType())) {
            throw new BalanceInvalidException("Erro ao processar transação: Tipo de categoria incompatível!");
        }

        if(transaction.getType() == Type.REVENUE || transaction.getType() == Type.INVESTMENT) {
            user.setBalance(user.getBalance().add(transaction.getValue()));
            userRepository.save(user);

            return repository.save(transaction);
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
        return repository.findAllOfIdUser(idUser);
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

        var listOfUserTransaction = repository.findAllOfIdUser(id);

        return listOfUserTransaction.stream()
                .filter(t -> t.getType().equals(typeEnum))
                .toList();
    }

    public List<Transactions> findByName(TransactionsFilterDTO data, Long id) {
        var listOfUserTransaction = repository.findAllOfIdUser(id);

        return listOfUserTransaction.stream()
                .filter(t -> t.getName().equals(data.given()))
                .toList();
    }

    public List<Transactions> findByCategory(TransactionsFilterDTO data, Long id) {
        var listOfUserTransaction = repository.findAllOfIdUser(id);

        return listOfUserTransaction.stream()
                .filter(t -> t.getCategory().getName().equals(data.given()))
                .toList();
    }
}
