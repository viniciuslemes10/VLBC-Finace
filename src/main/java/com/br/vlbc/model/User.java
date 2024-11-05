package com.br.vlbc.model;

import com.br.vlbc.records.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Transactions> transactions;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permissions_id")
    )
    private List<Permissions> permissions;

    public User(UserDTO data) {
        this.userName = data.userName();
        this.fullName = data.fullName();
        this.email = data.email();
        this.password = data.password();
        this.balance = data.balance();
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enable = false;
    }
}
