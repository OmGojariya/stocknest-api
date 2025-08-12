package com.stocknest.stocknest_api.service.impl;

import com.stocknest.stocknest_api.model.Admin;
import com.stocknest.stocknest_api.model.User;
import com.stocknest.stocknest_api.repository.AdminRepository;
import com.stocknest.stocknest_api.repository.UserRepository;
import com.stocknest.stocknest_api.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Autowired
    private final AdminRepository adminRepository;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Override
    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Admin getAdminById(String id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteAdmin(String id) {
        adminRepository.deleteById(id);
    }
}
