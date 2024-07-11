package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.UserProfileDTO;
import com.softuni.perfumes_shop.model.dto.UserRegisterDTO;

public interface UserService {
    void register(UserRegisterDTO registerData);

    UserProfileDTO getProfileData();
}
