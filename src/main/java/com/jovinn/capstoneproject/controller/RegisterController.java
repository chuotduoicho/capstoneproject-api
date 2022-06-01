package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/register")
public class RegisterController {
    private final UserService userService;
}
