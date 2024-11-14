package com.br.vlbc.records.users;

import com.br.vlbc.records.permissions.PermissionsDetailsDTO;

public record UserPermissionsDetailsDTO(
        UserDetailsDTO userDetailsDTO,
        PermissionsDetailsDTO permissionsDetailsDTO
) {
}
