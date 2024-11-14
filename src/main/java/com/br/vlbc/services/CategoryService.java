package com.br.vlbc.services;

import com.br.vlbc.enums.Type;
import com.br.vlbc.exceptions.CategoryExistException;
import com.br.vlbc.exceptions.CategoryNotFoundException;
import com.br.vlbc.exceptions.InvalidTypeException;
import com.br.vlbc.model.Category;
import com.br.vlbc.records.categories.CategoryDTO;
import com.br.vlbc.records.categories.CategoryDetailsDTO;
import com.br.vlbc.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public Category create(CategoryDTO data) {
        var category = new Category(data);
        var categoryFound = repository.findByName(category.getName());
        if(categoryFound.isEmpty()) {
            return repository.save(category);
        }
        throw new CategoryExistException("Categoria já registrada!");
    }

    public Category findById(Long id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new CategoryNotFoundException("Categoria não encontrada!")
                );
    }

    public List<CategoryDetailsDTO> allCategoria() {
        var listCategories = repository.findAll();
        return listCategories.stream()
                .map(CategoryDetailsDTO::new)
                .toList();
    }

    public Category update(CategoryDTO data, Long id) {
        var category = findById(id);

        if (data.name() != null && !data.name().isEmpty()) {
            category.setName(data.name());
        }

        if (data.type() != null && !data.type().isEmpty()) {
            try {
                Type tipo = Type.valueOf(data.type());
                category.setType(tipo);
            } catch (IllegalArgumentException e) {
                throw new InvalidTypeException("Tipo inválido: " + data.type());
            }
        }

        return repository.save(category);
    }

    public void delete(Long id) {
        var category = findById(id);
        repository.delete(category);
    }
}
