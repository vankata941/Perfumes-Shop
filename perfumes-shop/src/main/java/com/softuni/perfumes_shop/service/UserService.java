package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.inbound.AddAuthorizationDTO;
import com.softuni.perfumes_shop.model.dto.inbound.UserChangePasswordDTO;
import com.softuni.perfumes_shop.model.dto.outbound.UserProfileDTO;
import com.softuni.perfumes_shop.model.dto.inbound.UserRegisterDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    void register(UserRegisterDTO registerData);

    UserProfileDTO getProfileData();

    void changePassword(UserChangePasswordDTO changePasswordData, HttpServletRequest request, HttpServletResponse response);

    void grantAuthorizationAdmin(AddAuthorizationDTO newAdminData);
}
