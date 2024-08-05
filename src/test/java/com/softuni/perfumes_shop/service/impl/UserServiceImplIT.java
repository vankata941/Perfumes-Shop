package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.init.Initializer;
import com.softuni.perfumes_shop.model.dto.inbound.AddAuthorizationDTO;
import com.softuni.perfumes_shop.model.dto.inbound.UserChangePasswordDTO;
import com.softuni.perfumes_shop.model.dto.inbound.UserRegisterDTO;
import com.softuni.perfumes_shop.model.dto.outbound.UserProfileDTO;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceImplIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private CurrentUserDetails currentUserDetails;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private Initializer initializer;

    @MockBean
    private HttpServletRequest request;

    @MockBean
    private HttpServletResponse response;

    private UserRegisterDTO userRegisterDTO;

    @BeforeEach
    public void setUp() {

        userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testUser");
        userRegisterDTO.setFirstName("testFirstName");
        userRegisterDTO.setLastName("testLastName");
        userRegisterDTO.setPassword("testPassword");
        userRegisterDTO.setConfirmPassword("testPassword");
        userRegisterDTO.setEmail("testEmail");

        Role role = new Role();
        role.setUserRole(UserRole.USER);
        roleRepository.save(role);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void testRegister() {
        userService.register(userRegisterDTO);

        Optional<User> optUser = userRepository.findByUsername(userRegisterDTO.getUsername());

        Assertions.assertTrue(optUser.isPresent());

        User user = optUser.get();

        Assertions.assertEquals(userRegisterDTO.getFirstName(), user.getFirstName());
        Assertions.assertEquals(userRegisterDTO.getLastName(), user.getLastName());
        Assertions.assertTrue(passwordEncoder.matches(userRegisterDTO.getPassword(), user.getPassword()));
        Assertions.assertEquals(userRegisterDTO.getEmail(), user.getEmail());
        Assertions.assertEquals(1, user.getRoles().size());
        Assertions.assertEquals(UserRole.USER, user.getRoles().getFirst().getUserRole());
        Assertions.assertNotNull(user.getCart());
    }

    @Test
    public void testRegisterThrowsUsernameExists() {
        userService.register(userRegisterDTO);
        Assertions.assertThrows(NonUniqueResultException.class, () -> userService.register(userRegisterDTO));
    }

    @Test
    public void testRegisterThrowsPasswordMismatch() {
        userRegisterDTO.setConfirmPassword("test1");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.register(userRegisterDTO));
    }

    @Test
    public void testGrantAuthorizationAdmin() {
        userService.register(userRegisterDTO);

        Role role = new Role();
        role.setUserRole(UserRole.ADMIN);
        roleRepository.save(role);

        AddAuthorizationDTO addAuthorization = new AddAuthorizationDTO();
        addAuthorization.setUsername("testUser");
        addAuthorization.setUserRoleName("Admin");

        userService.grantAuthorizationAdmin(addAuthorization);

        Optional<User> optUser = userRepository.findByUsername(userRegisterDTO.getUsername());

        Assertions.assertTrue(optUser.isPresent());

        User user = optUser.get();

        Assertions.assertEquals(2, user.getRoles().size());
        Assertions.assertEquals(List.of(UserRole.USER, UserRole.ADMIN), user.getRoles().stream().map(Role::getUserRole).toList());
    }

    @Test
    public void testGrantAuthorizationAdminThrowsUserNotFound() {

        AddAuthorizationDTO addAuthorization = new AddAuthorizationDTO();
        addAuthorization.setUsername("testUser");
        addAuthorization.setUserRoleName("Admin");

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.grantAuthorizationAdmin(addAuthorization));
    }

    @Test
    public void testGrantAuthorizationAdminThrowsRoleNotFound() {

        userService.register(userRegisterDTO);

        AddAuthorizationDTO addAuthorization = new AddAuthorizationDTO();
        addAuthorization.setUsername("testUser");
        addAuthorization.setUserRoleName("Admin");

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.grantAuthorizationAdmin(addAuthorization));
    }
    
    @Test
    public void testGrantAuthorizationAdminThrowsDuplicateRoles() {

        AddAuthorizationDTO addAuthorization = new AddAuthorizationDTO();
        addAuthorization.setUsername("testUser");
        addAuthorization.setUserRoleName("Admin");
    
        userService.register(userRegisterDTO);

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.grantAuthorizationAdmin(addAuthorization));
    
    }

    @Test
    public void testGetProfileData() {
        userService.register(userRegisterDTO);

        when(currentUserDetails.optCurrentUser()).thenReturn(userRepository.findByUsername(userRegisterDTO.getUsername()));

        UserProfileDTO userProfile = userService.getProfileData();

        Assertions.assertEquals(userRegisterDTO.getUsername(), userProfile.getUsername());
        Assertions.assertEquals(userRegisterDTO.getFirstName(), userProfile.getFirstName());
        Assertions.assertEquals(userRegisterDTO.getLastName(), userProfile.getLastName());
        Assertions.assertEquals(userRegisterDTO.getEmail(), userProfile.getEmail());
    }

    @Test
    public void testGetProfileDataCurrentUserNotFound() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.empty());

        Assertions.assertNull(userService.getProfileData().getUsername());
        Assertions.assertNull(userService.getProfileData().getEmail());
        Assertions.assertNull(userService.getProfileData().getFirstName());
        Assertions.assertNull(userService.getProfileData().getLastName());
    }

    @Test
    public void testChangePassword() {
        userService.register(userRegisterDTO);

        when(currentUserDetails.optCurrentUser()).thenReturn(userRepository.findByUsername(userRegisterDTO.getUsername()));

        UserChangePasswordDTO changePass = new UserChangePasswordDTO();
        changePass.setCurrentPassword("testPassword");
        changePass.setNewPassword("test2");
        changePass.setConfirmNewPassword("test2");

        userService.changePassword(changePass, request, response);
        
        Optional<User> optUser = userRepository.findByUsername("testUser");
        Assertions.assertTrue(optUser.isPresent());

        User user = optUser.get();

        Assertions.assertTrue(passwordEncoder.matches(changePass.getNewPassword(), user.getPassword()));
    }

    @Test
    public void testChangePasswordThrowsPasswordMismatch() {

        UserChangePasswordDTO changePass = new UserChangePasswordDTO();
        changePass.setCurrentPassword("testPassword");
        changePass.setNewPassword("test");
        changePass.setConfirmNewPassword("test2");

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.changePassword(changePass, request, response));
    }

    @Test
    public void testChangePasswordThrowsCurrentPasswordMismatch() {
        userService.register(userRegisterDTO);

        when(currentUserDetails.optCurrentUser()).thenReturn(userRepository.findByUsername(userRegisterDTO.getUsername()));

        UserChangePasswordDTO changePass = new UserChangePasswordDTO();
        changePass.setCurrentPassword("test");
        changePass.setNewPassword("test2");
        changePass.setConfirmNewPassword("test2");

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.changePassword(changePass, request, response));
    }

    @Test
    public void testChangePasswordThrowsCurrentPasswordMatchesTheNewOne() {
        userService.register(userRegisterDTO);

        when(currentUserDetails.optCurrentUser()).thenReturn(userRepository.findByUsername(userRegisterDTO.getUsername()));

        UserChangePasswordDTO changePass = new UserChangePasswordDTO();
        changePass.setCurrentPassword("test2");
        changePass.setNewPassword("test2");
        changePass.setConfirmNewPassword("test2");

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.changePassword(changePass, request, response));
    }

    @Test
    public void testChangePasswordThrowsCurrentUserDoseNotExist() {

        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.empty());

        UserChangePasswordDTO changePass = new UserChangePasswordDTO();
        changePass.setCurrentPassword("testPassword");
        changePass.setNewPassword("test2");
        changePass.setConfirmNewPassword("test2");

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.changePassword(changePass, request, response));
    }
}