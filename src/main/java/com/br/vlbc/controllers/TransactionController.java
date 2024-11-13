package com.br.vlbc.controllers;

import com.br.vlbc.records.TransactionDetailsDTO;
import com.br.vlbc.records.TransactionsDTO;
import com.br.vlbc.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions/v1")
public class TransactionController {
    @Autowired
    private TransactionsService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDetailsDTO> create(@RequestBody TransactionsDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionDetailsDTO.fromEntityToDTO(service.create(data)));
    }

    @GetMapping(value = "/list/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDetailsDTO>> findAll(@PathVariable Long id) {
        return ResponseEntity.ok(TransactionDetailsDTO.fromListEntityToListDTO(service.findAll(id)));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDetailsDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(TransactionDetailsDTO.fromEntityToDTO(service.findById(id)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<TransactionDetailsDTO> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
