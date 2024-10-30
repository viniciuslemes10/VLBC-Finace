package com.br.vlbc.controllers;

import com.br.vlbc.records.CategoriaDTO;
import com.br.vlbc.records.CategoriaDetalhamentoDTO;
import com.br.vlbc.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categorias/v1")
public class CategoriaController {
    @Autowired
    private CategoriaService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoriaDetalhamentoDTO> create(@RequestBody CategoriaDTO data) {
        var categoria = service.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CategoriaDetalhamentoDTO(categoria));
    }

    @GetMapping(value = "/{id}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoriaDetalhamentoDTO> findById(@PathVariable("id") Long id) {
        var categoria = service.findById(id);
        return ResponseEntity.ok(new CategoriaDetalhamentoDTO(categoria));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoriaDetalhamentoDTO>> findAll() {
        return ResponseEntity.ok(service.allCategoria());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoriaDetalhamentoDTO> update(
            @RequestBody CategoriaDTO data,
            @PathVariable("id") Long id) {
        var categoria= service.update(data, id);
        return ResponseEntity.ok(new CategoriaDetalhamentoDTO(categoria));
    }
}
