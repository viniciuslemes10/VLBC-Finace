package com.br.vlbc.services;

import com.br.vlbc.model.Categoria;
import com.br.vlbc.records.CategoriaDTO;
import com.br.vlbc.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository repository;

    public Categoria create(CategoriaDTO data) {

    }

    public Categoria findById(Long id) {

    }

    public Set<Categoria> allCategoria() {

    }

    public Categoria update(CategoriaDTO data) {

    }

    public void delete(Long id) {

    }
}
