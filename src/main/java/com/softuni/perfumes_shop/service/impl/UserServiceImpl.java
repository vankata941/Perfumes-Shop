package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.dto.incoming.AddAdminDTO;
import com.softuni.perfumes_shop.model.dto.incoming.UserChangePasswordDTO;
import com.softuni.perfumes_shop.model.dto.outgoing.UserProfileDTO;
import com.softuni.perfumes_shop.model.dto.incoming.UserRegisterDTO;
import com.softuni.perfumes_shop.model.entity.Role;
import com.softuni.perfumes_shop.model.entity.User;
import com.softuni.perfumes_shop.model.enums.UserRole;
import com.softuni.perfumes_shop.repository.RoleRepository;
import com.softuni.perfumes_shop.repository.UserRepository;
import com.softuni.perfumes_shop.service.UserService;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import jakarta.persistence.NonUniqueResultException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

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
            throw new NonUniqueResultException(); //TODO: Add more precise error handling!
        }

        if (!registerData.getPassword().equals(registerData.getConfirmPassword())) {
            throw new IllegalArgumentException(); //TODO: Add more precise error handling!
        }

        User user = modelMapper.map(registerData, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> optRole = roleRepository.findByUserRole(UserRole.USER);
        optRole.ifPresent(user::addRole);

        this.userRepository.save(user);

    }

    @Override
    public UserProfileDTO getProfileData() {
        if (currentUserDetails.optCurrentUser().isEmpty()) {
            return new UserProfileDTO(); //TODO: Check this output!
        }
        return modelMapper.map(currentUserDetails.optCurrentUser().get(), UserProfileDTO.class);
    }

    @Override
    public void changePassword(UserChangePasswordDTO changePasswordData, HttpServletRequest request, HttpServletResponse response) {

        if (!changePasswordData.getNewPassword().equals(changePasswordData.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New password and confirm password should be the same!");
        }

        Optional<User> optUser = currentUserDetails.optCurrentUser();

        if (optUser.isEmpty()) {
            throw new IllegalArgumentException("User not found!");
        }

        if (!passwordEncoder.matches(changePasswordData.getCurrentPassword(), optUser.get().getPassword())) {
            throw new IllegalArgumentException("The provided current password is incorrect!");
        }

        if (passwordEncoder.matches(changePasswordData.getNewPassword(), optUser.get().getPassword())) {
            throw new IllegalArgumentException("The new password should be different from the old one!");
        }

        User user = optUser.get();

        user.setPassword(passwordEncoder.encode(changePasswordData.getNewPassword()));

        userRepository.save(user);

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, currentUserDetails.getAuthentication());
    }

    @Override
    public void grantAuthorizationAdmin(AddAdminDTO newAdminData) {
        Optional<User> optUser = userRepository.findByUsername(newAdminData.getUsername());

        if (optUser.isEmpty()) {
            throw new IllegalArgumentException("User not found!");
        }

        Optional<Role> optRole = roleRepository.findByUserRole(UserRole.ADMIN);

        if (optRole.isEmpty()) {
            throw new IllegalArgumentException("Role not found!");
        }

        optUser.get().addRole(optRole.get());

        userRepository.save(optUser.get());
    }
}
