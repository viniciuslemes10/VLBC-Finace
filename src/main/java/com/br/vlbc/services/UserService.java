package com.br.vlbc.services;

import com.br.vlbc.exceptions.BalanceInvalidException;
import com.br.vlbc.exceptions.PermissionNotFoundException;
import com.br.vlbc.exceptions.UserNotFoundException;
import com.br.vlbc.model.Permissions;
import com.br.vlbc.model.User;
import com.br.vlbc.model.UserPermissions;
import com.br.vlbc.records.*;
import com.br.vlbc.repositories.PermissionsRepository;
import com.br.vlbc.repositories.UserPermissionsRepository;
import com.br.vlbc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private UserPermissionsRepository userPermissionsRepository;

    public UserPermissionsDetailsDTO create(UserPermissionsDTO data) {
        var user = new User(data.userDTO());
        repository.save(user);

        var permissions = permissionsRepository.findByDescription(data.permissionsDTO().description())
                .orElseThrow(() ->
                    new PermissionNotFoundException("Permission not found!"));

        var userPermissions = new UserPermissions(user, permissions);
        userPermissionsRepository.save(userPermissions);

        return fromDTOs(user, permissions);
    }

    private UserPermissionsDetailsDTO fromDTOs(User user, Permissions permissions) {
        var userDTO = new UserDetailsDTO(user);
        var permissionsDTO = new PermissionsDetailsDTO(permissions);

        return new UserPermissionsDetailsDTO(userDTO, permissionsDTO);
    }

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Usuário não encontrado!"));
    }

    public User update(UserDTO data, Long id) {
        var user = findById(id);

        if(data.balance().compareTo(BigDecimal.ZERO) < 0) throw new BalanceInvalidException("Valor inválido!");
        user.setBalance(data.balance());

        updateUserFieldsIfNotEmpty(data.userName(), user::setUserName);
        updateUserFieldsIfNotEmpty(data.fullName(), user::setFullName);
        updateUserFieldsIfNotEmpty(data.email(), user::setEmail);
        updateUserFieldsIfNotEmpty(data.password(), user::setPassword);

        return repository.save(user);
    }

    public void updateUserFieldsIfNotEmpty(String data, Consumer<String> update) {
        if(data != null && !data.isEmpty()){
            update.accept(data);
        }
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public void delete(Long id) {
        var user = findById(id);
        repository.delete(user);
    }
}
