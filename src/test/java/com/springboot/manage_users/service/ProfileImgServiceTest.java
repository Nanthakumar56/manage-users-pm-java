package com.springboot.manage_users.service;

import com.springboot.manage_users.entity.ProfileImg;
import com.springboot.manage_users.repository.ProfileImgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileImgServiceTest {

    @Mock
    private ProfileImgRepository profileImgRepository; // Mock for ProfileImgRepository

    @InjectMocks
    private ProfileImgService profileImgService; // Inject mocks into ProfileImgService

    private MultipartFile mockFile;
    private ProfileImg mockProfileImg;

    @BeforeEach
    public void setUp() {
        // Create a mock MultipartFile
        mockFile = new MockMultipartFile(
                "testFile",
                "testFile.jpg",
                "image/jpeg",
                "Test file content".getBytes()
        );

        // Create a mock ProfileImg
        mockProfileImg = new ProfileImg();
        mockProfileImg.setId("1");
        mockProfileImg.setUser_id("user123");
        mockProfileImg.setFile_name("testFile.jpg");
        mockProfileImg.setFile_type("image/jpeg");
        mockProfileImg.setFile_size("12345");
        mockProfileImg.setGrp_data("Test file content".getBytes());
        mockProfileImg.setCreated_at(LocalDateTime.now());
        mockProfileImg.setUpdated_at(LocalDateTime.now());
    }

    @Test
    public void testUploadProfileImg() throws IOException {
        // Arrange
        when(profileImgRepository.save(any(ProfileImg.class))).thenReturn(mockProfileImg);

        // Act
        String result = profileImgService.uploadProfileImg(mockFile, "user123");

        // Assert
        assertNotNull(result);
        assertEquals("1", result); // Verify the returned ID
        verify(profileImgRepository, times(1)).save(any(ProfileImg.class));
    }

    @Test
    public void testGetProfileImg() {
        // Arrange
        when(profileImgRepository.findByUserId("user123")).thenReturn(Optional.of(mockProfileImg));

        // Act
        Optional<ProfileImg> result = profileImgService.getProfileImg("user123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("user123", result.get().getUser_id());
        verify(profileImgRepository, times(1)).findByUserId("user123");
    }

    @Test
    public void testUpdateProfileImg_ExistingProfile() throws IOException {
        // Arrange
        when(profileImgRepository.findByUserId("user123")).thenReturn(Optional.of(mockProfileImg));
        when(profileImgRepository.save(any(ProfileImg.class))).thenReturn(mockProfileImg);

        // Act
        String result = profileImgService.updateProfileImg(mockFile, "user123");

        // Assert
        assertNotNull(result);
        assertEquals("1", result); // Verify the returned ID
        verify(profileImgRepository, times(1)).findByUserId("user123");
        verify(profileImgRepository, times(1)).save(any(ProfileImg.class));
    }

    @Test
    public void testUpdateProfileImg_NewProfile() throws IOException {
        // Arrange
        when(profileImgRepository.findByUserId("user123")).thenReturn(Optional.empty());
        when(profileImgRepository.save(any(ProfileImg.class))).thenReturn(mockProfileImg);

        // Act
        String result = profileImgService.updateProfileImg(mockFile, "user123");

        // Assert
        assertNotNull(result);
        assertEquals("1", result); // Verify the returned ID
        verify(profileImgRepository, times(1)).findByUserId("user123");
        verify(profileImgRepository, times(1)).save(any(ProfileImg.class));
    }

    @Test
    public void testDeleteProfileImg() {
        // Arrange
        when(profileImgRepository.findByUserId("user123")).thenReturn(Optional.of(mockProfileImg));

        // Act
        profileImgService.deleteProfileImg("user123");

        // Assert
        verify(profileImgRepository, times(1)).findByUserId("user123");
        verify(profileImgRepository, times(1)).deleteById("1");
    }

    @Test
    public void testDeleteProfileImg_NotFound() {
        // Arrange
        when(profileImgRepository.findByUserId("user123")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            profileImgService.deleteProfileImg("user123");
        });

        assertEquals("Profile image not found with user id user123", exception.getMessage());
        verify(profileImgRepository, times(1)).findByUserId("user123");
        verify(profileImgRepository, never()).deleteById(any());
    }
}