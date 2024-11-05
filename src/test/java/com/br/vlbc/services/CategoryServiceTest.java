package com.br.vlbc.services;

import com.br.vlbc.enums.Type;
import com.br.vlbc.exceptions.CategoryExistException;
import com.br.vlbc.exceptions.CategoryNotFoundException;
import com.br.vlbc.exceptions.InvalidTypeException;
import com.br.vlbc.model.Category;
import com.br.vlbc.records.CategoryDTO;
import com.br.vlbc.repositories.CategoryRepository;
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

class CategoryServiceTest {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve ter sucesso ao criar uma Categoria com informações válidas")
    void testCreatedSuccess() {
        CategoryDTO data = new CategoryDTO("Transporte", "EXPENSES");
        var category = new Category(data);

        when(repository.findByName(category.getName())).thenReturn(Optional.empty());
        when(repository.save(category)).thenReturn(category);

        var result = service.create(data);

        assertNotNull(result);
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getType(), result.getType());

        verify(repository).findByName(category.getName());
        verify(repository).save(category);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar criar uma Categoria que já está registrada")
    void testShouldFailToCreateCategoryIfAlreadyExists() {
        CategoryDTO data = new CategoryDTO("Transporte", "EXPENSES");
        var category = new Category(data);

        when(repository.findByName(category.getName())).thenReturn(Optional.of(category));

        CategoryExistException categoriaExistException = assertThrows(CategoryExistException.class, () -> {
                        service.create(data);
        });

        assertEquals("Categoria já registrada!", categoriaExistException.getMessage());
        verify(repository).findByName(category.getName());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar a Categoria quando buscada pelo ID")
    void testFindCategoryByIdSuccess() {
        Long id = 1L;

        CategoryDTO data = new CategoryDTO("Transporte", "EXPENSES");
        var category = new Category(data);

        when(repository.findById(id)).thenReturn(Optional.of(category));

        var result = service.findById(id);

        assertNotNull(result);
        assertNotNull(result.getName());
        assertNotNull(result.getType());

        assertEquals(category.getName(), result.getName());
        assertEquals(category.getType(), result.getType());

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Lança CategoriaNotFoundException ao tentar buscar uma Categoria que não existe")
    void testFindCategoryByIdNotFoundException() {
        Long id = 2L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            service.findById(id);
        });

        assertEquals("Categoria não encontrada!", exception.getMessage(),
                "A mensagem de erro deve indicar que a Categoria não foi encontrada.");

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Successo ao atualizar uma Categoria")
    void testSuccessfulCategoryUpdate() {
        CategoryDTO dataUpdate = new CategoryDTO("Alimentação", "REVENUE");

        var category = new Category(dataUpdate);
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(category);

        var result = service.update(dataUpdate, id);

        assertNotNull(result);
        assertEquals("Alimentação", result.getName());
        assertEquals(Type.REVENUE, result.getType());

        verify(repository).findById(id);
        verify(repository).save(category);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar uma Categoria não encontrada")
    void testUpdateThrowsCategoriaNotFoundException() {
        CategoryDTO dataUpdate = new CategoryDTO("Transporte", "INVESTMENT");
        Long id = 2L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            service.update(dataUpdate, id);
        });

        assertEquals("Categoria não encontrada!", exception.getMessage(),
                "A mensagem de erro deve indicar que a Categoria não foi encontrada.");

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve manter dados existentes ao atualizar CategoriaDTO com dados vazios")
    void testUpdatePreservesDataWithEmptyCategoriaDTO() {
        CategoryDTO data = new CategoryDTO("Transporte", "INVESTMENT");
        CategoryDTO dataUpdate = new CategoryDTO("", "");
        Long id = 1L;

        var category = new Category(data);

        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(category);

        var result = service.update(dataUpdate, id);

        assertNotNull(result);
        assertEquals("Transporte", result.getName());
        assertEquals(Type.INVESTMENT, result.getType());

        verify(repository).findById(id);
        verify(repository).save(category);
    }

    @Test
    @DisplayName("Atualização de Categoria deve falhar com tipo inválido")
    void testUpdateThrowsExceptionForInvalidTypeArgumentException() {
        CategoryDTO data = new CategoryDTO("Transporte", "INVESTMENT");
        CategoryDTO dataUpdate = new CategoryDTO("Alimentação", "INVALID");
        Long id = 1L;

        var category = new Category(data);

        when(repository.findById(id)).thenReturn(Optional.of(category));

        InvalidTypeException exception = assertThrows(InvalidTypeException.class, () -> {
            service.update(dataUpdate, id);
        });

        assertEquals("Tipo inválido: " + dataUpdate.type(), exception.getMessage(),
                "A mensagem de erro deve indicar que o dado referente a Type está inválido.");

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Sucesso ao deletar uma Categoria")
    void testDeleteCategorySuccessfully() {
        CategoryDTO data = new CategoryDTO("Transporte", "INVESTMENT");
        Long id = 1L;

        var category = new Category(data);

        when(repository.findById(id)).thenReturn(Optional.of(category));

        service.delete(id);

        verify(repository).findById(id);
        verify(repository).delete(category);
    }

    @Test
    @DisplayName("Falha esperada ao tentar remover uma Categoria que não existe")
    void testDeleteNonExistentCategoriaThrowsException() {
        Long id = 2L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            service.delete(id);
        });

        assertEquals("Categoria não encontrada!", exception.getMessage(),
                "A mensagem de erro deve indicar que a Categoria não foi encontrada.");

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar todas as Categorias com sucesso")
    void testFetchAllCategoriasReturnsSuccess() {
        CategoryDTO data = new CategoryDTO("Transporte", "INVESTMENT");
        CategoryDTO dataTwo = new CategoryDTO("Alimentação", "EXPENSES");

        var category = new Category(data);
        var categoryTwo = new Category(dataTwo);

        List<Category> categoriasList = List.of(category, categoryTwo);
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