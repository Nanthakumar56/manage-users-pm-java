package com.springboot.manage_users.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.springboot.manage_users.service.ProfileImgService;
import com.springboot.manage_users.service.UserService;
import com.springboot.manage_users.dto.UserDTO;
import com.springboot.manage_users.entity.Users;
import com.springboot.manage_users.repository.UsersRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UsersController.class)
public class UsersControllerTest {
    
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService; 
    
    @MockBean
    private UsersRepository userRepo;
    
    @MockBean
    private ProfileImgService profileImgService;
    
    @InjectMocks
    private UsersController usersController;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
    }
    
    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/users/getAllUsers"))
               .andExpect(status().isNoContent())
               .andExpect(content().string("No users found"));
    }
    
    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById("123")).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/users/getUser").param("userId", "123"))
               .andExpect(status().isNotFound());
    }
    
    @Test
    void testCreateUser_Success() throws Exception {
        Users user = new Users();
        user.setUserid("123");
        when(userService.createUser(any(Users.class), eq(false))).thenReturn("Success");
        
        mockMvc.perform(post("/users/newUser")
               .param("firstName", "John")
               .param("lastName", "Doe")
               .param("email", "john.doe@example.com")
               .param("phone", "1234567890")
               .param("username", "johndoe")
               .param("password", "password")
               .param("employeeId", "EMP123")
               .param("role", "Admin")
               .param("organizationUnit", "IT")
               .param("department", "Development")
               .param("designation", "Engineer")
               .param("sendmail", "false")
               .contentType(MediaType.MULTIPART_FORM_DATA))
               .andExpect(status().isOk());
    }
    
    @Test
    void testUpdateUser_Failed() throws Exception {
        when(userService.updateUser(eq("123"), any(Users.class))).thenReturn(false);
        
        mockMvc.perform(put("/users/update")
               .param("userid", "123")
               .param("first_name", "John"))
               .andExpect(status().isBadRequest());
    }
    
    @Test
    void testChangePassword_Failed() throws Exception {
        String requestBody = "{\"name\":\"\", \"email\":\"invalid-email\", \"url\":\"\"}";

        mockMvc.perform(post("/users/changePassword")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("Missing required fields")); 
    }

}
