package com.br.vlbc.services;

import com.br.vlbc.model.Transactions;
import com.br.vlbc.repositories.TransactionRepository;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionsService {

    @Autowired
    private TransactionRepository repository;

    public Transactions create(TransactionDTO data) {

    }

    public Transaction findById(Long id) {

    }

    public Transaction findAll() {

    }

    public Transaction update(TransactionDTO data) {

    }

    public Transaction delete(Long id) {

    }
}
