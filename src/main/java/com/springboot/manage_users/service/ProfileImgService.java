package com.springboot.manage_users.service;

import com.springboot.manage_users.entity.ProfileImg;
import com.springboot.manage_users.repository.ProfileImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileImgService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @Autowired
    private ProfileImgRepository profileImgRepository;

    public ProfileImgService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory to store files.", ex);
        }
    }

    public String uploadProfileImg(MultipartFile file, String userId) {
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        long fileSize = file.getSize();

        try {
            ProfileImg profileImg = new ProfileImg();
            profileImg.setUser_id(userId); 
            profileImg.setFile_name(fileName);
            profileImg.setFile_type(fileType);
            profileImg.setFile_size(String.valueOf(fileSize));
            profileImg.setGrp_data(file.getBytes()); 
            profileImg.setCreated_at(LocalDateTime.now());
            profileImg.setUpdated_at(LocalDateTime.now());

            ProfileImg savedProfileImg = profileImgRepository.save(profileImg);
            return savedProfileImg.getId();
        } catch (IOException ex) {
            throw new RuntimeException("Could not store the file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }

    public Optional<ProfileImg> getProfileImg(String id) {
        return profileImgRepository.findByUserId(id);
    }

    public String updateProfileImg(MultipartFile file, String userId) {
        Optional<ProfileImg> existingProfileImgOpt = profileImgRepository.findByUserId(userId);
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        long fileSize = file.getSize();

        try {
            ProfileImg profileImg;

            if (existingProfileImgOpt.isPresent()) {
                // If profile image exists, update it
                profileImg = existingProfileImgOpt.get();
                profileImg.setUpdated_at(LocalDateTime.now());
            } else {
                profileImg = new ProfileImg();
                profileImg.setUser_id(userId);
                profileImg.setCreated_at(LocalDateTime.now());
                profileImg.setUpdated_at(LocalDateTime.now());
            }

            profileImg.setFile_name(fileName);
            profileImg.setFile_type(fileType);
            profileImg.setFile_size(String.valueOf(fileSize));
            profileImg.setGrp_data(file.getBytes()); 

           ProfileImg profile = profileImgRepository.save(profileImg);

           System.err.println(profile.getId());
            return profile.getId();

        } catch (IOException ex) {
            throw new RuntimeException("Could not update or create the file " + fileName + ". Please try again!", ex);
        }
    }

    public void deleteProfileImg(String userId) {
        Optional<ProfileImg> profileImgOpt = profileImgRepository.findByUserId(userId);
        if (profileImgOpt.isPresent()) {
            ProfileImg profileImg = profileImgOpt.get(); 
            String id = profileImg.getId();  
            profileImgRepository.deleteById(id); 
        } else {
            throw new RuntimeException("Profile image not found with user id " + userId);
        }
    }

}
