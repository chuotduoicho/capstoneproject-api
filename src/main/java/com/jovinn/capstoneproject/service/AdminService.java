package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Admin;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    Admin saveAdmin(Admin admin);
    List<Admin> findAdmins();
    Admin findAdminByUsername(String username);
    Admin findAdminById(UUID id);
    String deleteAdmin(UUID id);
    Admin updateAdmin(Admin admin);
}
