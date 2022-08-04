package com.jovinn.capstoneproject;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.ActivityType;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.service.ActivityTypeService;
import com.jovinn.capstoneproject.service.BoxService;
import com.jovinn.capstoneproject.service.CategoryService;
import com.jovinn.capstoneproject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.Jsr310Converters;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EntityScan(basePackageClasses = { CapstoneprojectApplication.class, Jsr310Converters.class })
public class CapstoneprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapstoneprojectApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService, ActivityTypeService activityTypeService, BoxService boxService, CategoryService categoryService) {
        return args -> {
//            ActivityType buyers = new ActivityType();
//            buyers.setActivityType(UserActivityType.BUYER);
//            ActivityType sell = new ActivityType();
//            sell.setActivityType(UserActivityType.SELLER);
//            ActivityType admin = new ActivityType();
//            admin.setActivityType(UserActivityType.ADMIN);
//            activityTypeService.saveType(buyers);
//            activityTypeService.saveType(sell);
//            activityTypeService.saveType(admin);
//            User user = new User();
//            user.setFirstName("admin");
//            user.setLastName("admin");
//            user.setUsername("admin");
//            user.setPhoneNumber("0942520358");
//            user.setIsEnabled(true);
//            user.setPassword("admin");
//            user.setEmail("admin@gmail.com");
//            user.setActivityType(activityTypeService.getByActivityType(UserActivityType.ADMIN));
//            userService.saveUser(user);
        };

    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
