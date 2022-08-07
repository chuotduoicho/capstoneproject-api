package com.jovinn.capstoneproject;

import com.jovinn.capstoneproject.enumerable.*;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.repository.*;
import com.jovinn.capstoneproject.repository.payment.WalletRepository;
import com.jovinn.capstoneproject.service.*;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static com.jovinn.capstoneproject.util.GenerateRandom.getRandomNumberString;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EntityScan(basePackageClasses = { CapstoneprojectApplication.class, Jsr310Converters.class })
public class CapstoneprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapstoneprojectApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run(UserService userService, UserRepository ur, PasswordEncoder passwordEncoder, BuyerRepository buy, WalletRepository wr, SubCategoryRepository sub, GalleryRepository gr, BoxRepository br, PackageRepository pr, SellerRepository sr, NotificationRepository r, ActivityTypeService activityTypeService, BoxService boxService, CategoryService categoryService, NotificationService n) {
////        Seller seller = sr.findById(UUID.fromString("96c09a8e-fb41-4665-a8db-95f1f28e4e32")).orElseThrow(() -> new RuntimeException("ngu"));
//        SubCategory subCategory = sub.findById(UUID.fromString("b48ca3ec-87ee-4e21-a0e9-491028f31ff4")).orElseThrow(() -> new RuntimeException("ngu"));
//        return args -> {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            String date = "2000-10-10";
//            for(int i = 1; i < 20; i++) {
//                User user = new User();
//                //user.setActivityType(activityTypeService.getByActivityType(UserActivityType.SELLER));
//                user.setFirstName("Lê Đức ");
//                user.setLastName("Mạnh SELLER " + RandomString.make(3));
//                user.setEmail("testtest_" + RandomString.make(3) + "@gmail.com");
//                user.setAvatar("data:image/jpeg;base64,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");
//                user.setBirthDate(formatter.parse(date));
//                user.setCity("Ha Noi");
//                user.setCountry("Viet Nam");
//                user.setIsEnabled(Boolean.TRUE);
//                user.setGender(Gender.MALE);
//                user.setPassword("123456");
//                user.setPhoneNumber("097" + i + "45" + i + "52" + i);
//                user.setAuthType(AuthTypeUser.LOCAL);
//                user.setUsername("testtest_" + i + RandomString.make(1));
//                user.setJoinedAt(new Date());
//                userService.saveUser(user);
//
//                Buyer buyer = new Buyer(UUID.randomUUID(), 0, getRandomNumberString(), user, null);
//                buy.save(buyer);
//
//                Wallet wallet = new Wallet(UUID.randomUUID(), new BigDecimal(0), new BigDecimal(50), null, user, null);
//                wr.save(wallet);
//
//                Seller seller = new Seller();
//                seller.setRankSeller(RankSeller.ADVANCED);
//                seller.setBrandName("Brand NAME Test " + RandomString.make(2));
//                seller.setDescriptionBio("Tôi làm về mảng " + subCategory.getCategory().getName() + " và có kinh nghiệm " + RandomString.make(3));
//                seller.setRatingPoint(2);
//                seller.setUser(user);
//                seller.setVerifySeller(Boolean.TRUE);
//                sr.save(seller);
//
//                Box box = new Box("Tôi sẽ thiết kế cho bạn # " + i + RandomString.make(3),
//                        "New description #" + i + RandomString.make(3),
//                        0,0,BoxServiceStatus.ACTIVE, seller, subCategory );
//                br.save(box);
//
//                Gallery gallery = new Gallery(UUID.randomUUID(), "url image #" + i + RandomString.make(3), null,null,null,null,box);
//                gr.save(gallery);
//                Package p = new Package(UUID.randomUUID(), "Gói Cơ Bản #" + i, "Với gói này tôi sẽ làm cho bạn như này " + box.getId() + " #" + i, 5 + i, new BigDecimal(10), 20 + i, box  );
//                pr.save(p);
//
//            }
////            ActivityType buyers = new ActivityType();
////            buyers.setActivityType(UserActivityType.BUYER);
////            ActivityType sell = new ActivityType();
////            sell.setActivityType(UserActivityType.SELLER);
////            activityTypeService.saveType(buyers);
////            activityTypeService.saveType(sell);
//        };
//
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
