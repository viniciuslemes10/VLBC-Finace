package com.br.vlbc.records.users;

import java.math.BigDecimal;

public record UserDTO(
        String userName,
        String fullName,
        String email,
        String password,
        BigDecimal balance
) {
}
