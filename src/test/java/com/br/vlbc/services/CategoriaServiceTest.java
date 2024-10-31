package com.br.vlbc.services;

import com.br.vlbc.enums.Type;
import com.br.vlbc.exceptions.CategoriaExistException;
import com.br.vlbc.exceptions.CategoriaNotFoundException;
import com.br.vlbc.exceptions.InvalidTypeArgumentException;
import com.br.vlbc.model.Categoria;
import com.br.vlbc.records.CategoriaDTO;
import com.br.vlbc.repositories.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
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
    @DisplayName("Deve ter sucesso ao criar uma Categoria com informações válidas")
    void testCreatedSuccess() {
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
    @DisplayName("Deve lançar uma exceção ao tentar criar uma Categoria que já está registrada")
    void testShouldFailToCreateCategoryIfAlreadyExists() {
        CategoriaDTO data = new CategoriaDTO("Transporte", "EXPENSES");
        var categoria = new Categoria(data);

        when(repository.findByName(categoria.getName())).thenReturn(Optional.of(categoria));

        CategoriaExistException categoriaExistException = assertThrows(CategoriaExistException.class, () -> {
                        service.create(data);
        });

        assertEquals("Categoria já registrada!", categoriaExistException.getMessage());
        verify(repository).findByName(categoria.getName());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar a Categoria quando buscada pelo ID")
    void testFindCategoryByIdSuccess() {
        Long id = 1L;

        CategoriaDTO data = new CategoriaDTO("Transporte", "EXPENSES");
        var categoria = new Categoria(data);

        when(repository.findById(id)).thenReturn(Optional.of(categoria));

        var result = service.findById(id);

        assertNotNull(result);
        assertNotNull(result.getName());
        assertNotNull(result.getType());

        assertEquals(categoria.getName(), result.getName());
        assertEquals(categoria.getType(), result.getType());

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Lança CategoriaNotFoundException ao tentar buscar uma Categoria que não existe")
    void testFindCategoryByIdNotFoundException() {
        Long id = 2L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        CategoriaNotFoundException exception = assertThrows(CategoriaNotFoundException.class, () -> {
            service.findById(id);
        });

        assertEquals("Categoria não encontrada!", exception.getMessage(),
                "A mensagem de erro deve indicar que a Categoria não foi encontrada.");

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Successo ao atualizar uma Categoria")
    void testSuccessfulCategoryUpdate() {
        CategoriaDTO dataUpdate = new CategoriaDTO("Alimentação", "REVENUE");

        var categoria = new Categoria(dataUpdate);
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.of(categoria));
        when(repository.save(categoria)).thenReturn(categoria);

        var result = service.update(dataUpdate, id);

        assertNotNull(result);
        assertEquals("Alimentação", result.getName());
        assertEquals(Type.REVENUE, result.getType());

        verify(repository).findById(id);
        verify(repository).save(categoria);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar uma Categoria não encontrada")
    void testUpdateThrowsCategoriaNotFoundException() {
        CategoriaDTO dataUpdate = new CategoriaDTO("Transporte", "INVESTMENT");
        Long id = 2L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        CategoriaNotFoundException exception = assertThrows(CategoriaNotFoundException.class, () -> {
            service.update(dataUpdate, id);
        });

        assertEquals("Categoria não encontrada!", exception.getMessage(),
                "A mensagem de erro deve indicar que a Categoria não foi encontrada.");

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve manter dados existentes ao atualizar CategoriaDTO com dados vazios")
    void testUpdatePreservesDataWithEmptyCategoriaDTO() {
        CategoriaDTO data = new CategoriaDTO("Transporte", "INVESTMENT");
        CategoriaDTO dataUpdate = new CategoriaDTO("", "");
        Long id = 1L;

        var categoria = new Categoria(data);

        when(repository.findById(id)).thenReturn(Optional.of(categoria));
        when(repository.save(categoria)).thenReturn(categoria);

        var result = service.update(dataUpdate, id);

        assertNotNull(result);
        assertEquals("Transporte", result.getName());
        assertEquals(Type.INVESTMENT, result.getType());

        verify(repository).findById(id);
        verify(repository).save(categoria);
    }

    @Test
    @DisplayName("Atualização de Categoria deve falhar com tipo inválido")
    void testUpdateThrowsExceptionForInvalidTypeArgumentException() {
        CategoriaDTO data = new CategoriaDTO("Transporte", "INVESTMENT");
        CategoriaDTO dataUpdate = new CategoriaDTO("Alimentação", "INVALID");
        Long id = 1L;

        var categoria = new Categoria(data);

        when(repository.findById(id)).thenReturn(Optional.of(categoria));

        InvalidTypeArgumentException exception = assertThrows(InvalidTypeArgumentException.class, () -> {
            service.update(dataUpdate, id);
        });

        assertEquals("Tipo inválido: " + dataUpdate.type(), exception.getMessage(),
                "A mensagem de erro deve indicar que o dado referente a Type está inválido.");

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Sucesso ao deletar uma Categoria")
    void testDeleteCategorySuccessfully() {
        CategoriaDTO data = new CategoriaDTO("Transporte", "INVESTMENT");
        Long id = 1L;

        var categoria = new Categoria(data);

        when(repository.findById(id)).thenReturn(Optional.of(categoria));

        service.delete(id);

        verify(repository).findById(id);
        verify(repository).delete(categoria);
    }

    @Test
    @DisplayName("Falha esperada ao tentar remover uma Categoria que não existe")
    void testDeleteNonExistentCategoriaThrowsException() {
        Long id = 2L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        CategoriaNotFoundException exception = assertThrows(CategoriaNotFoundException.class, () -> {
            service.delete(id);
        });

        assertEquals("Categoria não encontrada!", exception.getMessage(),
                "A mensagem de erro deve indicar que a Categoria não foi encontrada.");

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar todas as Categorias com sucesso")
    void testFetchAllCategoriasReturnsSuccess() {
        CategoriaDTO data = new CategoriaDTO("Transporte", "INVESTMENT");
        CategoriaDTO dataTwo = new CategoriaDTO("Alimentação", "EXPENSES");

        var categoria = new Categoria(data);
        var categoriaTwo = new Categoria(dataTwo);

        List<Categoria> categoriasList = List.of(categoria, categoriaTwo);
        when(repository.findAll()).thenReturn(categoriasList);

        var result = service.allCategoria();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Transporte", result.get(0).name());
        assertEquals(Type.INVESTMENT, result.get(0).type());

        assertEquals("Alimentação", result.get(1).name());
        assertEquals(Type.EXPENSES, result.get(1).type());

        verify(repository).findAll();
    }
}