package com.br.vlbc.records;

public record UserPermissionsDTO(
        UserDTO userDTO,
        PermissionsDTO permissionsDTO
) {
}
