package com.br.vlbc.repositories;

import com.br.vlbc.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean findByName(String name);
}
