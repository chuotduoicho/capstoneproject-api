package com.jovinn.capstoneproject;

import com.jovinn.capstoneproject.model.Admin;
import com.jovinn.capstoneproject.service.AdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CapstoneprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapstoneprojectApplication.class, args);
    }
//    @Bean
//    PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//    @Bean
//    CommandLineRunner run(UserService userService) {
//        return args -> {
//            userService.saveRole(new Role(null, "ROLE_SELLER"));
//            userService.saveRole(new Role(null, "ROLE_BUYER"));
//            userService.saveRole(new Role(null, "ROLE_ADMIN"));
//            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));
//            userService.saveUser(new User(null, "vo", "tai"), new ArrayList<>());
//            userService.saveUser(new User(null, "Vo Duc Tai", "tai", "123", new ArrayList<>()));
//            userService.saveUser(new User(null, "Nguyen The Vinh", "vinh", "123", new ArrayList<>()));
//            userService.saveUser(new User(null, "Tran Xuan Son", "son", "123", new ArrayList<>()));
//            userService.saveUser(new User(null, "Doan Minh Duc", "duc", "123", new ArrayList<>()));
//            userService.saveUser(new User(null, "Le Thanh Tung", "tung", "123", new ArrayList<>()));
//            userService.addRoleToUser("tai", "ROLE_SELLER");
//            userService.addRoleToUser("tai", "ROLE_SUPER_ADMIN");
//            userService.addRoleToUser("vinh", "ROLE_BUYER");
//            userService.addRoleToUser("son", "ROLE_BUYER");
//            userService.addRoleToUser("duc", "ROLE_ADMIN");
//            userService.addRoleToUser("tung", "ROLE_SELLER");
//        };
//
//    }
    @Bean
    CommandLineRunner run(AdminService adminService){
        return args -> {
            adminService.saveAdmin(new Admin(null,"Doan","Duc","duc23400@gmail.com","duc","123","0123456789"));
            adminService.saveAdmin(new Admin(null,"Vo","Tai","taivo@gmail.com","tai","123","0123456789"));
            adminService.saveAdmin(new Admin(null,"Son","Tran","sontran@gmail.com","son","123","0123456789"));
            adminService.saveAdmin(new Admin(null,"Vinh","Nguyen","vinhnguyen@gmail.com","vinh","123","0123456789"));
            adminService.saveAdmin(new Admin(null,"Tung","Le","tungle@gmail.com","tung","123","0123456789"));
        };
    }
}
