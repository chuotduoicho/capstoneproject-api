package com.jovinn.capstoneproject;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.Category;
import com.jovinn.capstoneproject.model.SubCategory;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.service.ServiceCategoryService;
import com.jovinn.capstoneproject.service.SubCategoryService;
import com.jovinn.capstoneproject.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CapstoneprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapstoneprojectApplication.class, args);
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    CommandLineRunner run(SubCategoryService subCategoryService, ServiceCategoryService categoryService) {
        return args -> {
//            userService.saveRole(new Role(null, "ROLE_SELLER"));
//            userService.saveRole(new Role(null, "ROLE_BUYER"));
//            userService.saveRole(new Role(null, "ROLE_ADMIN"));
//            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

//            userService.saveUser(new User(null, "Vo","Duc Tai", "tai","tai@gmail.com",null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER));
//            userService.saveUser(new User(null, "Nguyen","The Vinh", "vinh","vinh@gmail.com", null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER));
//
//            userService.saveUser(new User(null, "Tran","Xuan Son","son", "son@gmail.com", null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER));
//
//            userService.saveUser(new User(null, "Doan","Minh Duc","duc", "duc@gmail.com",   null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER));
//
//            userService.saveUser(new User(null, "Le","Thanh Tung", "tung","tung@gmail.com", null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER));

//            userService.addRoleToUser("tai", "ROLE_SELLER");
//            userService.addRoleToUser("tai", "ROLE_SUPER_ADMIN");
//            userService.addRoleToUser("vinh", "ROLE_BUYER");
//            userService.addRoleToUser("son", "ROLE_BUYER");
//            userService.addRoleToUser("duc", "ROLE_ADMIN");
//            userService.addRoleToUser("tung", "ROLE_SELLER");
            Category categoryA = new Category(null,"cateA",new ArrayList<>());
            Category categoryB = new Category(null,"cateB",new ArrayList<>());
            Category categoryC = new Category(null,"cateC",new ArrayList<>());
            SubCategory subA = new SubCategory(null,categoryA,"subA",new ArrayList<>());
            SubCategory subB = new SubCategory(null,categoryB,"subB",new ArrayList<>());
            SubCategory subC = new SubCategory(null,categoryC,"subC",new ArrayList<>());
            SubCategory subD = new SubCategory(null,categoryA,"subD",new ArrayList<>());

            categoryService.saveServiceCategory(categoryA);
            categoryService.saveServiceCategory(categoryB);
            categoryService.saveServiceCategory(categoryC);
            subCategoryService.saveSubCategory(subA);
            subCategoryService.saveSubCategory(subB);
            subCategoryService.saveSubCategory(subC);
            subCategoryService.saveSubCategory(subD);
                    };

    }
}
