package com.br.vlbc.records.categories;

import com.br.vlbc.enums.Type;
import com.br.vlbc.model.Category;

public record CategoryDetailsDTO(
        Long id,
        String name,
        Type type
) {
    public CategoryDetailsDTO(Category categoria) {
        this(categoria.getId(), categoria.getName(), categoria.getType());
    }
}
