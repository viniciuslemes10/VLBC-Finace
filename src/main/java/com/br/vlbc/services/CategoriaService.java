package com.br.vlbc.services;

import com.br.vlbc.enums.Type;
import com.br.vlbc.exceptions.CategoriaExistException;
import com.br.vlbc.exceptions.CategoriaNotFoundException;
import com.br.vlbc.exceptions.InvalidTypeArgumentException;
import com.br.vlbc.model.Categoria;
import com.br.vlbc.records.CategoriaDTO;
import com.br.vlbc.records.CategoriaDetalhamentoDTO;
import com.br.vlbc.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        throw new CategoriaExistException("Categoria já registrada!");
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

        if (data.name() != null && !data.name().isEmpty()) {
            categoria.setName(data.name());
        }

        if (data.type() != null && !data.type().isEmpty()) {
            try {
                Type tipo = Type.valueOf(data.type());
                categoria.setType(tipo);
            } catch (IllegalArgumentException e) {
                throw new InvalidTypeArgumentException("Tipo inválido: " + data.type());
            }
        }

        return repository.save(categoria);
    }

    public void delete(Long id) {
        var categoria = findById(id);
        repository.delete(categoria);
    }
}
