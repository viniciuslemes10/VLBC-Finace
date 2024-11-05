package com.br.vlbc.records;

import com.br.vlbc.model.Permissions;

public record PermissionsDetailsDTO(
        Long id,
        String description
) {
    public PermissionsDetailsDTO(Permissions permissions) {
        this(permissions.getId(), permissions.getDescription());
    }
}
