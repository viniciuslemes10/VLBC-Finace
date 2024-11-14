package com.br.vlbc.controllers;

import com.br.vlbc.records.categories.CategoryDTO;
import com.br.vlbc.records.categories.CategoryDetailsDTO;
import com.br.vlbc.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categorias/v1")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDetailsDTO> create(@RequestBody CategoryDTO data) {
        var category = service.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CategoryDetailsDTO(category));
    }

    @GetMapping(value = "/{id}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDetailsDTO> findById(@PathVariable("id") Long id) {
        var category = service.findById(id);
        return ResponseEntity.ok(new CategoryDetailsDTO(category));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDetailsDTO>> findAll() {
        return ResponseEntity.ok(service.allCategoria());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDetailsDTO> update(
            @RequestBody CategoryDTO data,
            @PathVariable("id") Long id) {
        var category= service.update(data, id);
        return ResponseEntity.ok(new CategoryDetailsDTO(category));
    }
}
