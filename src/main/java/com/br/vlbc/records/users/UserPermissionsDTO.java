package com.br.vlbc.records.users;

import com.br.vlbc.records.permissions.PermissionsDTO;

public record UserPermissionsDTO(
        UserDTO userDTO,
        PermissionsDTO permissionsDTO
) {
}
