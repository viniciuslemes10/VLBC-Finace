package com.br.vlbc.repositories;

import com.br.vlbc.model.UserPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionsRepository extends JpaRepository<UserPermissions, Long> {
}
