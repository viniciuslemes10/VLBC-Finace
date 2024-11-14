package com.br.vlbc.records.users;

import com.br.vlbc.model.User;

import java.math.BigDecimal;
import java.util.List;

public record UserDetailsDTO(
        Long id,
        String userName,
        String fullName,
        String email,
        String password,
        BigDecimal balance,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        boolean enable
) {
    public UserDetailsDTO(User user) {
        this(user.getId(), user.getUserName(), user.getFullName(),
                user.getEmail(), user.getPassword(), user.getBalance(),
                user.isAccountNonExpired(), user.isAccountNonLocked(),
                user.isCredentialsNonExpired(), user.isEnable());
    }

    public static List<UserDetailsDTO> fromUsers(List<User> users) {
        return users.stream()
                .map(UserDetailsDTO::new)
                .toList();
    }
}
