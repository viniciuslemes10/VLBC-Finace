package com.br.vlbc.records;

public record UserPermissionsDetailsDTO(
        UserDetailsDTO userDetailsDTO,
        PermissionsDetailsDTO permissionsDetailsDTO
) {
}
