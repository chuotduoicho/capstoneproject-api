package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.ServiceCategory;

import java.util.List;
import java.util.UUID;

public interface ServiceCategoryService {
    ServiceCategory saveServiceCategory(ServiceCategory serviceCategory);
    List<ServiceCategory> saveServiceCategories(List<ServiceCategory> serviceCategories);
    List<ServiceCategory> getServiceCategories();
    ServiceCategory getServiceCategoryById(UUID id);
    ServiceCategory getServiceCategoryByName(String name);
    String deleteServiceCategoryById(UUID id);
    ServiceCategory updateServiceCategory(ServiceCategory serviceCategory);
}
