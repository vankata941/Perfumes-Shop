package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.dto.UserProfileDTO;
import com.softuni.perfumes_shop.model.dto.UserRegisterDTO;
import com.softuni.perfumes_shop.model.entity.Role;
import com.softuni.perfumes_shop.model.entity.User;
import com.softuni.perfumes_shop.model.enums.UserRole;
import com.softuni.perfumes_shop.repository.RoleRepository;
import com.softuni.perfumes_shop.repository.UserRepository;
import com.softuni.perfumes_shop.service.UserService;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final CurrentUserDetails currentUserDetails;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Override
    public void register(UserRegisterDTO registerData) {

        if (userRepository.existsByUsernameOrEmail(registerData.getUsername(), registerData.getEmail())) {
            throw new NonUniqueResultException();
        }

        if (!registerData.getPassword().equals(registerData.getConfirmPassword())) {
            throw new IllegalArgumentException();
        }

        User user = modelMapper.map(registerData, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> optRole = roleRepository.findByUserRole(UserRole.USER);
        optRole.ifPresent(user::addRole);

        this.userRepository.save(user);

    }

    @Override
    public UserProfileDTO getProfileData() {
        return modelMapper.map(currentUserDetails.getCurrentUser(), UserProfileDTO.class);
    }
}
