package com.br.vlbc.records;

import com.br.vlbc.enums.Type;
import com.br.vlbc.model.Categoria;

public record CategoriaDetalhamentoDTO(
        String name,
        Type type
) {
    public CategoriaDetalhamentoDTO(Categoria categoria) {
        this(categoria.getName(), categoria.getType());
    }
}
