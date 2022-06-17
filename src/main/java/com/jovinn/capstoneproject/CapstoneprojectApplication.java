package com.jovinn.capstoneproject;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EntityScan(basePackageClasses = { CapstoneprojectApplication.class, Jsr310Converters.class })
public class CapstoneprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapstoneprojectApplication.class, args);
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
//            userService.saveRole(new Role(null, "ROLE_SELLER"));
//            userService.saveRole(new Role(null, "ROLE_BUYER"));
//            userService.saveRole(new Role(null, "ROLE_ADMIN"));
//            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

            //userService.saveUser(new User(null, "Vo","Duc Tai", "tai","tai@gmail.com",null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER, null));
            //userService.saveUser(new User(null, "Nguyen","The Vinh", "vinh","vinh@gmail.com", null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER,null));

            //userService.saveUser(new User(null, "Tran","Xuan Son","son", "son@gmail.com", null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER,null));

            userService.saveUser(new User(null, "Doan","Minh Duc","duc", "ducdmhe141516@fpt.edu.vn",   null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER,null));
            userService.saveUser(new User(null, "Le","Thanh Tung","tung", "tunglthe141097@fpt.edu.vn",   null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER,null));

            //userService.saveUser(new User(null, "Le","Thanh Tung", "tung","tung@gmail.com", null,null,null,null,null,null,null,null, "123",null,null,null, UserActivityType.BUYER,null));

//            userService.addRoleToUser("tai", "ROLE_SELLER");
//            userService.addRoleToUser("tai", "ROLE_SUPER_ADMIN");
//            userService.addRoleToUser("vinh", "ROLE_BUYER");
//            userService.addRoleToUser("son", "ROLE_BUYER");
//            userService.addRoleToUser("duc", "ROLE_ADMIN");
//            userService.addRoleToUser("tung", "ROLE_SELLER");
        };

    }
}
