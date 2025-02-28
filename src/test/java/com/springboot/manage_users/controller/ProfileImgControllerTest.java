package com.springboot.manage_users.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.manage_users.entity.ProfileImg;
import com.springboot.manage_users.service.ProfileImgService;

class ProfileImgControllerTest {

    @Mock
    private ProfileImgService profileImgService;

    @InjectMocks
    private ProfileImgController profileImgController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProfileImg() {
        String imgId = "12345";
        ProfileImg profileImg = new ProfileImg();
        profileImg.setFile_name("profile.jpg");
        profileImg.setFile_type("image/jpeg");
        profileImg.setGrp_data(new byte[]{1, 2, 3});
        
        when(profileImgService.getProfileImg(imgId)).thenReturn(Optional.of(profileImg));
        
        ResponseEntity<byte[]> response = profileImgController.getProfileImg(imgId, false);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateProfileImg() {
        String imgId = "12345";
        MockMultipartFile file = new MockMultipartFile("file", "profile.jpg", "image/jpeg", new byte[]{1, 2, 3});
        String userId = "user123";
        
        when(profileImgService.updateProfileImg(file, userId)).thenReturn("Updated Successfully");
        
        ResponseEntity<String> response = profileImgController.updateFile(imgId, file, userId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File updated successfully: Updated Successfully", response.getBody());
    }

    @Test
    void testDeleteProfileImg() {
        String imgId = "12345";
        
        doNothing().when(profileImgService).deleteProfileImg(imgId);
        
        ResponseEntity<String> response = profileImgController.deleteProfileImg(imgId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File deleted successfully", response.getBody());
    }
}
