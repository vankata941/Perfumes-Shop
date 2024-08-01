package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.entity.User;
import com.softuni.perfumes_shop.model.entity.UserDetail;
import com.softuni.perfumes_shop.repository.UserDetailRepository;
import com.softuni.perfumes_shop.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {

    private final UserDetailRepository userDetailRepository;

    @Override
    public void saveUserDetail(UserDetail userDetail) {
        userDetailRepository.save(userDetail);
    }

    @Override
    public Optional<UserDetail> findUserDetailIfExists(String firstName, String lastName, String phoneNumber, Long id) {
        return userDetailRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndPhoneNumberAndUserId(firstName, lastName, phoneNumber, id);
    }
}
