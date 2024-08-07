package com.softuni.perfumes_shop.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.softuni.perfumes_shop.init.Initializer;
import com.softuni.perfumes_shop.model.entity.Cart;
import com.softuni.perfumes_shop.model.entity.User;
import com.softuni.perfumes_shop.repository.CartRepository;
import com.softuni.perfumes_shop.repository.UserRepository;
import com.softuni.perfumes_shop.service.JwtService;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private Initializer initializer;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private CartRepository cartRepository;


    @Test
    public void testViewRegister() throws Exception {
        mockMvc.perform(get("/user/register")).andExpect(status().isOk());
    }

    @Test
    public void testDoRegister() throws Exception {
        mockMvc.perform(post("/user/register")
        .param("username", "testUser")
        .param("email", "email@email.com")
        .param("firstName", "testFirstName")
        .param("lastName", "testLastName")
        .param("password", "testPassword1.")
        .param("confirmPassword", "testPassword1.")
        .with(csrf())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user/login"));

        Optional<User> optUser = userRepository.findByUsername("testUser");

        Assertions.assertTrue(optUser.isPresent());

        User user = optUser.get();

        Assertions.assertEquals("email@email.com", user.getEmail());
        Assertions.assertEquals("testFirstName", user.getFirstName());
        Assertions.assertEquals("testLastName", user.getLastName());
        Assertions.assertTrue(passwordEncoder.matches("testPassword1.", user.getPassword()));
    }

    @Test
    public void testViewLogin() throws Exception {
        mockMvc.perform(get("/user/login")).andExpect(status().isOk());
    }
 
    @Test
    public void testViewLoginError() throws Exception {
        mockMvc.perform(get("/user/login-error")).andExpect(status().isOk());
    }

    @Test
    public void testProfile() throws Exception {
        mockMvc.perform(get("/user/profile")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void testViewChangePassword() throws Exception {
        mockMvc.perform(get("/user/change-password")).andExpect(status().is3xxRedirection());
    }
}