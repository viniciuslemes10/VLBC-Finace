package com.br.vlbc.controllers;

import com.br.vlbc.model.Categoria;
import com.br.vlbc.records.CategoriaDTO;
import com.br.vlbc.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categorias/v1")
public class CategoriaController {
    @Autowired
    private CategoriaService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Categoria> create(@RequestBody CategoriaDTO data) {
        service.create(data);
    }
}
