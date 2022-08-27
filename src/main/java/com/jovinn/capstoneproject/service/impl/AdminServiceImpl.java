package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.adminsite.adminresponse.AdminCountDataResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.AdminProfileResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.AdminViewUserResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.CountTotalRevenueResponse;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.model.Admin;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.*;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.repository.payment.TransactionRepository;
import com.jovinn.capstoneproject.service.ActivityTypeService;
import com.jovinn.capstoneproject.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoxRepository boxRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ActivityTypeService activityTypeService;
    @Autowired
    private PostRequestRepository postRequestRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminCountDataResponse getAllCountDataResponse() {
        return new AdminCountDataResponse(userRepository.count(), boxRepository.count(),postRequestRepository.count(),contractRepository.count(), contractRepository.countTotalRevenue());
    }

    @Override
    public List<AdminViewUserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        List<AdminViewUserResponse> usersResponse = new ArrayList<>();
        for (User user : users) {
            usersResponse.add(new AdminViewUserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(),
                    user.getUsername(),user.getIsEnabled()));
        }
        return usersResponse;
    }

    @Override
    public CountTotalRevenueResponse countTotalRevenueToday() {
        return new CountTotalRevenueResponse(contractRepository.countTotalRevenueToday());
    }

    @Override
    public List<BigDecimal> countTotalRevenueByMonth() {
        List<BigDecimal> list = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            list.add(contractRepository.countTotalRevenueByMonth(i));
        }
        return list;
    }

    @Override
    public ApiResponse saveAdmin(User admin) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(admin.getUsername()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại");
        }
        User newAdmin = new User();
        newAdmin.setFirstName(admin.getFirstName());
        newAdmin.setLastName(admin.getLastName());
        newAdmin.setUsername(admin.getUsername());
        newAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        newAdmin.setPhoneNumber(admin.getPhoneNumber());
        userRepository.save(admin);
        return new ApiResponse(Boolean.TRUE, "Tạo mới thành công");
    }

    @Override
    public ApiResponse updateAdmin(UUID id, User admin) {
        User existAdmin = userRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy thông tin admin"));
        if (Boolean.TRUE.equals(adminRepository.existsAdminByAdminAccount(admin.getUsername()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại");
        }

        existAdmin.setFirstName(admin.getFirstName());
        existAdmin.setLastName(admin.getLastName());
        existAdmin.setUsername(admin.getUsername());
        existAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        existAdmin.setPhoneNumber(admin.getPhoneNumber());
        userRepository.save(existAdmin);

        return new ApiResponse(Boolean.TRUE, "Cập nhật thành công");
    }

    @Override
    public ApiResponse deleteAdmin(UUID id) {
        userRepository.deleteById(id);
        return new ApiResponse(Boolean.TRUE, "Xóa thành công");
    }

    @Override
    public List<AdminProfileResponse> getListAdmin() {
        List<User> admins = userRepository.findAll();
        List<AdminProfileResponse> responseList = new ArrayList<>();
        for (User admin : admins) {
            if(Objects.equals(activityTypeService.getActivityTypeByUserId(admin.getId()), UserActivityType.ADMIN)){
                responseList.add(new AdminProfileResponse(admin.getId(), admin.getFirstName(), admin.getLastName(), admin.getUsername(),
                        admin.getPhoneNumber()));
            }
        }
        return responseList;
    }

    @Override
    public AdminProfileResponse getAdminById(UUID id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy tài khoản admin"));

        return new AdminProfileResponse(admin.getId(), admin.getFirstName(), admin.getFirstName(), admin.getAdminAccount(),
                admin.getPhoneNumber());
    }
}
