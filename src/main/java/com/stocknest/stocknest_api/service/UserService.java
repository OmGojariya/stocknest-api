package com.stocknest.stocknest_api.service;

import com.stocknest.stocknest_api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
    List<User> findAll();
    void deleteUser(String id);
}
