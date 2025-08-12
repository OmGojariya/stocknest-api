package com.stocknest.stocknest_api.service;

import com.stocknest.stocknest_api.model.Admin;
import com.stocknest.stocknest_api.model.User;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    Admin saveAdmin(Admin admin);
    Optional<Admin> findByEmail(String email);
    List<Admin> getAllAdmin();
    List<User> getAllUsers();
    Admin getAdminById(String id);
    void deleteAdmin(String id);
}
