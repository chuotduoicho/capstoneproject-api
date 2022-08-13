package com.jovinn.capstoneproject;


import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.ActivityType;
import com.jovinn.capstoneproject.repository.*;
import com.jovinn.capstoneproject.repository.payment.WalletRepository;
import com.jovinn.capstoneproject.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

import java.util.*;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EntityScan(basePackageClasses = { CapstoneprojectApplication.class, Jsr310Converters.class })
public class CapstoneprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapstoneprojectApplication.class, args);
    }


//    @Bean
//    CommandLineRunner run(UserService userService, UserRepository ur, PasswordEncoder passwordEncoder,
//                          BuyerRepository buy, WalletRepository wr, SubCategoryRepository sub,
//                          GalleryRepository gr, BoxRepository br, PackageRepository pr, SellerRepository sr,
//                          NotificationRepository r, ActivityTypeService activityTypeService, BoxService boxService,
//                          CategoryService categoryService, NotificationService n) {
////        Seller seller = sr.findById(UUID.fromString("96c09a8e-fb41-4665-a8db-95f1f28e4e32")).orElseThrow(() -> new RuntimeException("ngu"));
////        SubCategory subCategory = sub.findById(UUID.fromString("b48ca3ec-87ee-4e21-a0e9-491028f31ff4")).orElseThrow(() -> new RuntimeException("ngu"));
////        return args -> {
////            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
////            String date = "2000-10-10";
////            for(int i = 1; i < 20; i++) {
////                User user = new User();
////                //user.setActivityType(activityTypeService.getByActivityType(UserActivityType.SELLER));
////                user.setFirstName("Lê Đức ");
////                user.setLastName("Mạnh SELLER " + RandomString.make(3));
////                user.setEmail("testtest_" + RandomString.make(3) + "@gmail.com");
////                user.setBirthDate(formatter.parse(date));
////                user.setCity("Ha Noi");
////                user.setCountry("Viet Nam");
////                user.setIsEnabled(Boolean.TRUE);
////                user.setGender(Gender.MALE);
////                user.setPassword("123456");
////                user.setPhoneNumber("097" + i + "45" + i + "52" + i);
////                user.setAuthType(AuthTypeUser.LOCAL);
////                user.setUsername("testtest_" + i + RandomString.make(1));
////                user.setJoinedAt(new Date());
////                userService.saveUser(user);
////
////                Buyer buyer = new Buyer(UUID.randomUUID(), 0, getRandomNumberString(), user, null);
////                buy.save(buyer);
////
////                Wallet wallet = new Wallet(UUID.randomUUID(), new BigDecimal(0), new BigDecimal(50), null, user, null);
////                wr.save(wallet);
////
////                Seller seller = new Seller();
////                seller.setRankSeller(RankSeller.ADVANCED);
////                seller.setBrandName("Brand NAME Test " + RandomString.make(2));
////                seller.setDescriptionBio("Tôi làm về mảng " + subCategory.getCategory().getName() + " và có kinh nghiệm " + RandomString.make(3));
////                seller.setRatingPoint(2);
////                seller.setUser(user);
////                seller.setVerifySeller(Boolean.TRUE);
////                sr.save(seller);
////
////                Box box = new Box("Tôi sẽ thiết kế cho bạn # " + i + RandomString.make(3),
////                        "New description #" + i + RandomString.make(3),
////                        0,0,BoxServiceStatus.ACTIVE, seller, subCategory );
////                br.save(box);
////
////                Gallery gallery = new Gallery(UUID.randomUUID(), "url image #" + i + RandomString.make(3), null,null,null,null,box);
////                gr.save(gallery);
////                Package p = new Package(UUID.randomUUID(), "Gói Cơ Bản #" + i, "Với gói này tôi sẽ làm cho bạn như này " + box.getId() + " #" + i, 5 + i, new BigDecimal(10), 20 + i, box  );
////                pr.save(p);
////
////            }
//            return args -> {
//                ActivityType buyers = new ActivityType();
//                buyers.setActivityType(UserActivityType.BUYER);
//                activityTypeService.saveType(buyers);
//                ActivityType sell = new ActivityType();
//                sell.setActivityType(UserActivityType.SELLER);
//                activityTypeService.saveType(sell);
//                ActivityType admin = new ActivityType();
//                admin.setActivityType(UserActivityType.ADMIN);
//                activityTypeService.saveType(admin);
//            };
//    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
