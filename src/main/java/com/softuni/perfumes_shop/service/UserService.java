package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.UserChangePasswordDTO;
import com.softuni.perfumes_shop.model.dto.UserProfileDTO;
import com.softuni.perfumes_shop.model.dto.UserRegisterDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    void register(UserRegisterDTO registerData);

    UserProfileDTO getProfileData();

    void changePassword(UserChangePasswordDTO changePasswordData, HttpServletRequest request, HttpServletResponse response);
}
