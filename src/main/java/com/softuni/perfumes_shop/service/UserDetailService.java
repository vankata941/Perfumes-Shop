package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.entity.UserDetail;

import java.util.Optional;

public interface UserDetailService {
    void saveUserDetail(UserDetail userDetail);

    Optional<UserDetail> findUserDetailIfExists(String firstName, String lastName, String phoneNumber, Long id);
}
