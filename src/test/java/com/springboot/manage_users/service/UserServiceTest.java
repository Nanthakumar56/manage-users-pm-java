package com.springboot.manage_users.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.springboot.manage_users.dto.BulkUserCreationResponse;
import com.springboot.manage_users.dto.ProjectUserDto;
import com.springboot.manage_users.dto.UserDTO;
import com.springboot.manage_users.entity.ProfileImg;
import com.springboot.manage_users.entity.Users;
import com.springboot.manage_users.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private ProfileImgService profileImgService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService;

    private Users user;
    private UserDTO userDTO;
    private ProjectUserDto projectUserDto;

    @BeforeEach
    public void setUp() {
        user = new Users();
        user.setUserid("AB12345");
        user.setFirst_name("John");
        user.setLast_name("Doe");
        user.setEmail("john.doe@example.com");
        user.setRole("USER");
        user.setStatus("Active");
        user.setCreated_at(LocalDateTime.now());

        userDTO = new UserDTO("AB12345", "John", "Doe", "USER", "john.doe@example.com", LocalDateTime.now(), "Active");

        projectUserDto = new ProjectUserDto("AB12345", "John", "Doe", "USER", "Active", null);
    }

    @Test
    public void testCreateUser() {
        when(usersRepository.save(any(Users.class))).thenReturn(user);

        String result = userService.createUser(user, true);

        assertEquals("Success", result);
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    public void testCreateUsers() {
        List<Users> userList = Arrays.asList(user);
        when(usersRepository.save(any(Users.class))).thenReturn(user);

        BulkUserCreationResponse response = userService.createUsers(userList);

        assertEquals(1, response.getSuccessfulCount());
        assertEquals(0, response.getFailedCount());
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    public void testGetAllUsers() {
        when(usersRepository.findAll()).thenReturn(Arrays.asList(user));

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(userDTO.getUserid(), result.get(0).getUserid());
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    public void testGetUsersByIds() {
        when(usersRepository.findAllById(anyList())).thenReturn(Arrays.asList(user));

        List<UserDTO> result = userService.getUsersByIds(Arrays.asList("AB12345"));

        assertEquals(1, result.size());
        assertEquals(userDTO.getUserid(), result.get(0).getUserid());
        verify(usersRepository, times(1)).findAllById(anyList());
    }

    @Test
    public void testGetUserById() {
        when(usersRepository.findById("AB12345")).thenReturn(Optional.of(user));

        Optional<Users> result = userService.getUserById("AB12345");

        assertTrue(result.isPresent());
        assertEquals(user.getUserid(), result.get().getUserid());
        verify(usersRepository, times(1)).findById("AB12345");
    }

    @Test
    public void testUpdateUserProfile() {
        when(usersRepository.findById("AB12345")).thenReturn(Optional.of(user));

        userService.updateUserProfile("AB12345", "profile123");

        verify(usersRepository, times(1)).findById("AB12345");
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    public void testUpdateUser() {
        when(usersRepository.findById("AB12345")).thenReturn(Optional.of(user));

        boolean result = userService.updateUser("AB12345", user);

        assertTrue(result);
        verify(usersRepository, times(1)).findById("AB12345");
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    public void testUpdateCredential() {
        when(usersRepository.findById("AB12345")).thenReturn(Optional.of(user));

        String result = userService.updateCredential(user);

        assertEquals("Credentials updated successfully.", result);
        verify(usersRepository, times(1)).findById("AB12345");
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    public void testUpdateStatus() {
        when(usersRepository.findById("AB12345")).thenReturn(Optional.of(user));

        String result = userService.updateStatus(user);

        assertEquals("Status updated successfully.", result);
        verify(usersRepository, times(1)).findById("AB12345");
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    public void testGetName() {
        when(usersRepository.findById("AB12345")).thenReturn(Optional.of(user));

        String result = userService.getName("AB12345");

        assertEquals("John Doe", result);
        verify(usersRepository, times(1)).findById("AB12345");
    }

    @Test
    public void testGetAllProjectUsers() {
        when(usersRepository.findByStatus("Active")).thenReturn(Arrays.asList(user));
        when(profileImgService.getProfileImg("AB12345")).thenReturn(Optional.empty());

        List<ProjectUserDto> result = userService.getAllProjectUsers(null, Collections.emptyList());

        assertEquals(1, result.size());
        assertEquals(projectUserDto.getUserid(), result.get(0).getUserid());
        verify(usersRepository, times(1)).findByStatus("Active");
        verify(profileImgService, times(1)).getProfileImg("AB12345");
    }

    @Test
    public void testGetTaskUsersWithinIds() {
        when(usersRepository.findByUseridIn(anyList())).thenReturn(Arrays.asList(user));
        when(profileImgService.getProfileImg("AB12345")).thenReturn(Optional.empty());

        List<ProjectUserDto> result = userService.getTaskUsersWithinIds(null, Arrays.asList("AB12345"));

        assertEquals(1, result.size());
        assertEquals(projectUserDto.getUserid(), result.get(0).getUserid());
        verify(usersRepository, times(1)).findByUseridIn(anyList());
        verify(profileImgService, times(1)).getProfileImg("AB12345");
    }

    @Test
    public void testGetAllProjectUsersFilled() {
        when(usersRepository.findAllById(anyList())).thenReturn(Arrays.asList(user));
        when(profileImgService.getProfileImg("AB12345")).thenReturn(Optional.empty());

        List<ProjectUserDto> result = userService.getAllProjectUsersFilled(Arrays.asList("AB12345"));

        assertEquals(1, result.size());
        assertEquals(projectUserDto.getUserid(), result.get(0).getUserid());
        verify(usersRepository, times(1)).findAllById(anyList());
        verify(profileImgService, times(1)).getProfileImg("AB12345");
    }

    @Test
    public void testUpdateRoleUsers() {
        // Arrange
        String roleName = "USER";
        String expectedUrl = "http://localhost:5757/roles/updateRoleUsers?rolename=" + roleName;

        // Act
        userService.updateRoleUsers(roleName);

        // Assert
        verify(restTemplate, times(1)).put(eq(expectedUrl), isNull());
    }

    @Test
    public void testUpdateRoleUsersRemove() {
        // Arrange
        String roleName = "USER";
        String expectedUrl = "http://localhost:5757/roles/updateRoleUsersRemove?rolename=" + roleName;

        // Act
        userService.updateRoleUsersRemove(roleName);

        // Assert
        verify(restTemplate, times(1)).put(eq(expectedUrl), isNull());
    }
}