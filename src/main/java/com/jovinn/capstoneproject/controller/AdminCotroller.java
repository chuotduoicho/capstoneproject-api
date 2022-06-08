package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.Admin;
import com.jovinn.capstoneproject.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminCotroller {
    @Autowired
    private final AdminService adminService;

    @GetMapping("/admins")
    public List<Admin>findAllAdmins(){
        return adminService.findAdmins();
    }

    @GetMapping("/admin/{id}")
    public Admin findAdminById(@PathVariable UUID id){
        return adminService.findAdminById(id);
    }

    @GetMapping("/admin/findByName/{name}")
    public Admin findAdminByName(@PathVariable String name){
        return adminService.findAdminByUsername(name);
    }

    @PostMapping("/admin/save")
    public Admin saveAdmin(@RequestBody Admin admin){
        return adminService.saveAdmin(admin);
    }



    @DeleteMapping ("/admin/delete/{id}")
    public String deleteAdmin(@PathVariable UUID id){
        return adminService.deleteAdmin(id);
    }


}
