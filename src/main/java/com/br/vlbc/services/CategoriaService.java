package com.br.vlbc.services;

import com.br.vlbc.enums.Type;
import com.br.vlbc.exceptions.CategoriaNotFoundException;
import com.br.vlbc.model.Categoria;
import com.br.vlbc.records.CategoriaDTO;
import com.br.vlbc.records.CategoriaDetalhamentoDTO;
import com.br.vlbc.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository repository;

    public Categoria create(CategoriaDTO data) {
        var categoria = new Categoria(data);
        var categoriaFound = repository.findByName(categoria.getName());
        if(categoriaFound.isEmpty()) {
            return repository.save(categoria);
        }
        throw new CategoriaNotFoundException("Categoria não encontrada!");
    }

    public Categoria findById(Long id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new CategoriaNotFoundException("Categoria não encontrada!")
                );
    }

    public List<CategoriaDetalhamentoDTO> allCategoria() {
        var listCategorias = repository.findAll();
        return listCategorias.stream()
                .map(CategoriaDetalhamentoDTO::new)
                .toList();
    }

    public Categoria update(CategoriaDTO data, Long id) {
        var categoria = findById(id);
        if(data.name() != null && !data.name().isEmpty()) {
            categoria.setName(data.name());
        }

        if(data.type() != null && EnumSet.allOf(Type.class).contains(data.type())) {
            categoria.setType(data.type());
        }
        return repository.save(categoria);
    }

    public void delete(Long id) {
        var categoria = findById(id);
        repository.delete(categoria);
    }
}
