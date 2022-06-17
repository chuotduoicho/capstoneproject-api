package com.jovinn.capstoneproject;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.ActivityType;
import com.jovinn.capstoneproject.service.ActivityTypeService;
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
import java.util.TimeZone;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EntityScan(basePackageClasses = { CapstoneprojectApplication.class, Jsr310Converters.class })
public class CapstoneprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapstoneprojectApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService, ActivityTypeService activityTypeService) {
        return args -> {
//            ActivityType buyer = ActivityType.builder().activityType(UserActivityType.BUYER).build();
//            ActivityType seller = ActivityType.builder().activityType(UserActivityType.SELLER).build();
            ActivityType buyers = new ActivityType();
            buyers.setActivityType(UserActivityType.BUYER);
            ActivityType sell = new ActivityType();
            sell.setActivityType(UserActivityType.SELLER);
            activityTypeService.saveType(buyers);
            activityTypeService.saveType(sell);

            //activityTypeService.saveType(new ActivityType(null, UserActivityType.BUYER));
//            userService.saveUser(new User(null, "Vo","Duc Tai", "tai","tai@gmail.com",null,null,null,null,null,null,null,null,"123", null,null,null,null,null,null,null));
//            userService.saveUser(new User(null, "Nguyen","The Vinh", "vinh","vinh@gmail.com", null,null,null,null,null,null,null,null, "123",null,null,null,null,null,null,null));
//
//            userService.saveUser(new User(null, "Tran","Xuan Son","son", "son@gmail.com", null,null,null,null,null,null,null,null, "123",null,null, null,null,null,null,null));
//
//            userService.saveUser(new User(null, "Doan","Minh Duc","duc", "duc@gmail.com",   null,null,null,null,null,null,null,null, "123",null,null, null,null,null,null,null));
//
//            userService.saveUser(new User(null, "Le","Thanh Tung", "tung","tung@gmail.com", null,null,null,null,null,null,null,null, "123",null,null, null,null,null,null,null));
        };

    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter();
//    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
