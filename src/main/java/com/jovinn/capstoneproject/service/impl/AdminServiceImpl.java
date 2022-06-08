package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Admin;
import com.jovinn.capstoneproject.repository.AdminRepository;
import com.jovinn.capstoneproject.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public List<Admin> findAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin findAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public Admin findAdminById(UUID id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public String deleteAdmin(UUID id) {
        adminRepository.deleteById(id);
        return "admin remove: "+ id;
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        Admin existedAdmin = adminRepository.findById(admin.getId()).orElse(null);
        existedAdmin.setFirst_name(admin.getFirst_name());
        existedAdmin.setLast_name(admin.getLast_name());
        existedAdmin.setEmail(admin.getEmail());
        existedAdmin.setPhone_number(admin.getPhone_number());
        existedAdmin.setUsername(admin.getUsername());
        existedAdmin.setPassword(admin.getPassword());
        return adminRepository.save(existedAdmin);
    }
}

