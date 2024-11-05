package com.br.vlbc.services;

import com.br.vlbc.model.User;
import com.br.vlbc.records.UserDTO;
import com.br.vlbc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User create(UserDTO data) {

    }

    public User findById(Long id) {

    }

    public User update(UserDTO data) {

    }

    public List<User> findAllUsers() {

    }

    public void delete(Long id) {

    }
}
