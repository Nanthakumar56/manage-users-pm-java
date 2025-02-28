package com.springboot.manage_users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.manage_users.entity.ProfileImg;
import com.springboot.manage_users.service.ProfileImgService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class ProfileImgController {

    @Autowired
    private ProfileImgService profileImgService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getProfileImg(
            @PathVariable String id,
            @RequestParam(required = false, defaultValue = "false") boolean download) {
        Optional<ProfileImg> profileImg = profileImgService.getProfileImg(id);
        if (profileImg.isPresent()) {
            ProfileImg img = profileImg.get();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, img.getFile_type());
            
            if (download) {
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + img.getFile_name() + "\"");
            } else {
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + img.getFile_name() + "\"");
            }

            return new ResponseEntity<>(img.getGrp_data(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFile(@PathVariable String id, 
                                             @RequestParam("file") MultipartFile file, 
                                             @RequestParam("user_id") String userId) {
        String profileImg = profileImgService.updateProfileImg(file, userId);
        return ResponseEntity.ok("File updated successfully: " + profileImg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProfileImg(@PathVariable String id) {
        profileImgService.deleteProfileImg(id);
        return ResponseEntity.ok("File deleted successfully");
    }
}
