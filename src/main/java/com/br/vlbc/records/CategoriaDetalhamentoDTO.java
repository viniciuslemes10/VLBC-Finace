package com.br.vlbc.records;

import com.br.vlbc.enums.Type;
import com.br.vlbc.model.Categoria;

public record CategoriaDetalhamentoDTO(
        Long id,
        String name,
        Type type
) {
    public CategoriaDetalhamentoDTO(Categoria categoria) {
        this(categoria.getId(), categoria.getName(), categoria.getType());
    }
}
