package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.incoming.AddAdminDTO;
import com.softuni.perfumes_shop.model.dto.incoming.UserChangePasswordDTO;
import com.softuni.perfumes_shop.model.dto.outgoing.UserProfileDTO;
import com.softuni.perfumes_shop.model.dto.incoming.UserRegisterDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    void register(UserRegisterDTO registerData);

    UserProfileDTO getProfileData();

    void changePassword(UserChangePasswordDTO changePasswordData, HttpServletRequest request, HttpServletResponse response);

    void grantAuthorizationAdmin(AddAdminDTO newAdminData);
}
