package com.br.vlbc.controllers;

import com.br.vlbc.records.users.UserDTO;
import com.br.vlbc.records.users.UserDetailsDTO;
import com.br.vlbc.records.users.UserPermissionsDTO;
import com.br.vlbc.records.users.UserPermissionsDetailsDTO;
import com.br.vlbc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users/v1")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPermissionsDetailsDTO> create(@RequestBody UserPermissionsDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(data));
    }

    @GetMapping(value = "/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsDTO> findById(@PathVariable("id") Long id) {
        var user = service.findById(id);
        return ResponseEntity.ok(new UserDetailsDTO(user));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDetailsDTO>> findAllUsers() {
        var users = service.findAllUsers();
        return ResponseEntity.ok(UserDetailsDTO.fromUsers(users));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsDTO> update(@RequestBody UserDTO data, @PathVariable("id") Long id) {
        var user = service.update(data, id);
        return ResponseEntity.ok(new UserDetailsDTO(user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDetailsDTO> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
