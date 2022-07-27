package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.response.*;
import com.jovinn.capstoneproject.model.Admin;
import com.jovinn.capstoneproject.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private BoxService boxService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/get-total-service")
    public CountServiceResponse countTotalService(){
        return boxService.countTotalService();
    }

    @GetMapping("/get-total-revenue")
    public CountTotalRevenueResponse getTotalRevenue(){
        return contractService.getTotalRevenue();
    }

    @GetMapping("/count-total-user")
    public CountUserResponse countTotalUser(){
        return userService.countUserById();
    }

    @GetMapping("/get-all-count-data")
    public AdminCountDataResponse getAllCountData(){
        return adminService.getAllCountDataResponse();
    }

    @GetMapping("/get-all-user")
    public List<AdminViewUserResponse> getAllUser(){
        return adminService.getAllUser();
    }

    @GetMapping("/get-all-transaction")
    public List<AdminViewTransactionResponse> getAllTransaction(){
        return transactionService.getAllTransaction();
    }

    @GetMapping("/count-total-revenue-today")
    public CountTotalRevenueResponse getTotalRevenueToday(){
        return adminService.countTotalRevenueToday();
    }

    @GetMapping("/count-total-revenue-by-month")
    public List<BigDecimal> getTotalRevenueByMonth(){
        return adminService.countTotalRevenueByMonth();
    }

    @PutMapping("/ban-or-unban-user/{id}")
    public ApiResponse banOrUnbanUser(@PathVariable UUID id){
        return userService.banOrUnbanUser(id);
    }

    @PostMapping("/save-admin")
    public ResponseEntity<ApiResponse> saveAdmin(@RequestBody Admin admin){
        ApiResponse response = adminService.saveAdmin(admin);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("/update-admin/{id}")
    public ResponseEntity<ApiResponse> updateAdmin(@RequestBody Admin admin,@PathVariable UUID id){
        ApiResponse response = adminService.updateAdmin(id,admin);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @DeleteMapping("/delete-admin/{id}")
    public ResponseEntity<ApiResponse> deleteAdmin(@PathVariable UUID id){
        ApiResponse response = adminService.deleteAdmin(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/get-list-admin")
    public List<AdminProfileResponse> getListAdmin(){
        return adminService.getListAdmin();
    }
    @GetMapping("/get-admin-by-id/{id}")
    public AdminProfileResponse getAdminById(@PathVariable UUID id){
        return adminService.getAdminById(id);
    }
}
