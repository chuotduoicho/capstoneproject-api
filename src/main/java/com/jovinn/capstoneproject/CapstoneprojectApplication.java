package com.jovinn.capstoneproject;

import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.service.BoxService;
import com.jovinn.capstoneproject.service.ServiceCategoryService;
import com.jovinn.capstoneproject.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    CommandLineRunner run(UserService userService, BoxService boxService, ServiceCategoryService serviceCategoryService) {
        return args -> {
//            userService.saveRole(new Role(null, "ROLE_SELLER"));
//            userService.saveRole(new Role(null, "ROLE_BUYER"));
//            userService.saveRole(new Role(null, "ROLE_ADMIN"));
//            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

            userService.saveUser(new User(null, "Vo","Duc Tai", "tai","tai@gmail.com",null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER));
            userService.saveUser(new User(null, "Nguyen","The Vinh", "vinh","vinh@gmail.com", null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER));

            userService.saveUser(new User(null, "Tran","Xuan Son","son", "son@gmail.com", null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER));

            userService.saveUser(new User(null, "Doan","Minh Duc","duc", "duc@gmail.com",   null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER));

            userService.saveUser(new User(null, "Le","Thanh Tung", "tung","tung@gmail.com", null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER));
            //boxService.saveBox(new Box(null, UUID.randomUUID(),null,null,null,"MusicSoundCloud",1,2, BoxServiceStatus.ACTIVE));
            serviceCategoryService.saveServiceCategory(new Category(null,"cat1",null));
            serviceCategoryService.saveServiceCategory(new Category(null,"cat2",null));
            serviceCategoryService.saveServiceCategory(new Category(null,"cat3",null));
//            userService.addRoleToUser("tai", "ROLE_SELLER");
//            userService.addRoleToUser("tai", "ROLE_SUPER_ADMIN");
//            userService.addRoleToUser("vinh", "ROLE_BUYER");
//            userService.addRoleToUser("son", "ROLE_BUYER");
//            userService.addRoleToUser("duc", "ROLE_ADMIN");
//            userService.addRoleToUser("tung", "ROLE_SELLER");
        };

    }
}
