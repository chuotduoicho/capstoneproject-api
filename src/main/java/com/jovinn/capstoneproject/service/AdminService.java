package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.adminsite.adminresponse.AdminCountDataResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.AdminProfileResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.AdminViewUserResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.CountTotalRevenueResponse;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.model.Admin;
import com.jovinn.capstoneproject.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AdminService {

    AdminCountDataResponse getAllCountDataResponse();

    List<AdminViewUserResponse> getAllUser();

    CountTotalRevenueResponse countTotalRevenueToday();

    List<BigDecimal> countTotalRevenueByMonth();

    ApiResponse saveAdmin(User admin);

    ApiResponse updateAdmin(UUID id,User admin);

    ApiResponse deleteAdmin(UUID id);

    List<AdminProfileResponse> getListAdmin();

    AdminProfileResponse getAdminById(UUID id);
}
