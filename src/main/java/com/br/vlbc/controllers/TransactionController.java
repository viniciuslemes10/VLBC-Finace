package com.br.vlbc.controllers;

import com.br.vlbc.records.transactions.TransactionDetailsDTO;
import com.br.vlbc.records.transactions.TransactionsDTO;
import com.br.vlbc.records.transactions.TransactionsFilterDTO;
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
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/type/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
                consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDetailsDTO>> findByType(
            @RequestBody TransactionsFilterDTO data,
            @PathVariable Long id) {
        return ResponseEntity.ok(TransactionDetailsDTO.fromListEntityToListDTO(service.findByType(data, id)));
    }

    @GetMapping(value = "/name/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDetailsDTO>> findByName(
            @RequestBody TransactionsFilterDTO data,
            @PathVariable Long id) {
        return ResponseEntity.ok(TransactionDetailsDTO.fromListEntityToListDTO(service.findByName(data, id)));
    }

    @GetMapping(value = "/category/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDetailsDTO>> findByCategory(
            @RequestBody TransactionsFilterDTO data,
            @PathVariable Long id) {
        return ResponseEntity.ok(TransactionDetailsDTO.fromListEntityToListDTO(service.findByCategory(data, id)));
    }
    @GetMapping(value = "/method/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDetailsDTO>> findByMethod(
            @RequestBody TransactionsFilterDTO data,
            @PathVariable Long id) {
        return ResponseEntity.ok(TransactionDetailsDTO.fromListEntityToListDTO(service.findByMethod(data, id)));
    }
}
