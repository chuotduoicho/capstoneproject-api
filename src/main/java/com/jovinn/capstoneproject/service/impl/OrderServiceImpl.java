package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Orders;
import com.jovinn.capstoneproject.repository.OrderRepository;
import com.jovinn.capstoneproject.repository.PackageRepository;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Orders createOrderByBuyer(Orders request, UserPrincipal currentUser) {

        return orderRepository.save(request);
    }
}
