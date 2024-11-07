package com.br.vlbc.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserPermissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "permissions_id")
    private Permissions permissions;

    public UserPermissions(User user, Permissions permissions) {
        this.user = user;
        this.permissions = permissions;
    }
}
