package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.ServiceCategory;
import com.jovinn.capstoneproject.repository.ServiceCategoryRepository;
import com.jovinn.capstoneproject.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceCategoryServiceImpl implements ServiceCategoryService {
    @Autowired
    private ServiceCategoryRepository categoryRepository;

    @Override
    public ServiceCategory saveServiceCategory(ServiceCategory serviceCategory) {
        return categoryRepository.save(serviceCategory);
    }

    @Override
    public List<ServiceCategory> saveServiceCategories(List<ServiceCategory> serviceCategories) {
        return categoryRepository.saveAll(serviceCategories);
    }

    @Override
    public List<ServiceCategory> getServiceCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public ServiceCategory getServiceCategoryById(UUID id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public ServiceCategory getServiceCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public String deleteServiceCategoryById(UUID id) {
        categoryRepository.deleteById(id);
        return "Category remove" + id;
    }

    @Override
    public ServiceCategory updateServiceCategory(ServiceCategory serviceCategory) {
        ServiceCategory existCategory = categoryRepository.findById(serviceCategory.getId()).orElse(null);
        existCategory.setName(serviceCategory.getName());
        return categoryRepository.save(existCategory);
    }
}
