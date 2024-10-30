package com.br.vlbc.services;

import com.br.vlbc.enums.Type;
import com.br.vlbc.exceptions.CategoriaExistException;
import com.br.vlbc.model.Categoria;
import com.br.vlbc.records.CategoriaDTO;
import com.br.vlbc.repositories.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService service;

    @Mock
    private CategoriaRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Successo ao criar uma Categoria")
    @Order(1)
    void testCreateSuccess() {
        CategoriaDTO data = new CategoriaDTO("Transporte", "EXPENSES");
        var categoria = new Categoria(data);

        when(repository.findByName(categoria.getName())).thenReturn(Optional.empty());
        when(repository.save(categoria)).thenReturn(categoria);

        var result = service.create(data);

        assertNotNull(result);
        assertEquals(categoria.getName(), result.getName());
        assertEquals(categoria.getType(), result.getType());

        verify(repository).findByName(categoria.getName());
        verify(repository).save(categoria);
    }

    @Test
    @DisplayName("Falha ao criar uma Categoria já existente")
    @Order(2)
    void testCreateWhenCategoriaExists() {
        CategoriaDTO data = new CategoriaDTO("Transporte", "EXPENSES");
        var categoria = new Categoria(data);

        when(repository.findByName(categoria.getName())).thenReturn(Optional.of(categoria));

        CategoriaExistException categoriaExistException = assertThrows(CategoriaExistException.class,
                () -> {
                        service.create(data);
        });

       assertEquals("Categoria já registrada!", categoriaExistException.getMessage());
        verify(repository).findByName(categoria.getName());
        verify(repository, never()).save(any());
    }

    private Categoria mockCategoria(CategoriaDTO data) {
        var categoria = new Categoria();
        categoria.setId(1L);
        categoria.setName(data.name());
        categoria.setType(Type.valueOf(data.type()));
        return categoria;
    }
}