package com.springboot.manage_users.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.manage_users.service.ProfileImgService;
import com.springboot.manage_users.service.UserService;
import com.springboot.manage_users.entity.ProfileImg;
import com.springboot.manage_users.entity.Users;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private ProfileImgService profileImgService;

    @PostMapping("/newUser")
    public ResponseEntity<String> createUser(
        @RequestParam("firstName") String firstName,
        @RequestParam(value = "middleName", required = false) String middleName,
        @RequestParam("lastName") String lastName,
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone,
        @RequestParam("gender") String gender,
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam("employeeId") String employeeId,
        @RequestParam("role") String role,
        @RequestParam("address") String address,
        @RequestParam("state") String state,
        @RequestParam("country") String country,
        @RequestParam("sendmail") boolean sendmail,
        @RequestParam(value = "file" , required = false) MultipartFile file
    ) {
        Users user = new Users();
        user.setFirst_name(firstName);
        user.setMiddle_name(middleName);
        user.setLast_name(lastName);
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setGender(gender);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmployee_id(employeeId);
        user.setRole(role);
        user.setAddress(address);
        user.setState(state);
        user.setCountry(country);

        String status = userService.createUser(user);

        if (status.equals("Success")) {
        	 if(file != null) {
             	try {
                     ProfileImg uploadedImage = profileImgService.uploadProfileImg(file, user.getUserid());
                     userService.updateUserProfile(user.getUserid(), uploadedImage.getId());
                     return ResponseEntity.status(HttpStatus.OK).body("User and profile image created successfully");
                 } catch (Exception e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User created but failed to upload profile image: " + e.getMessage());
                 }
             }
        	 else {
                 return ResponseEntity.status(HttpStatus.OK).body("Use created with no profile picture successfully");
        	 }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
        }
    }

	    @GetMapping("/getAllUsers")
	    public ResponseEntity<List<Users>> getAllUsers() {  
	        List<Users> roleList = userService.getAllUsers();
	        if (!roleList.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.OK).body(roleList);
	        } else {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	        }
	    }

	    @GetMapping("/getUser")
	    public ResponseEntity<Users> getUserById(@RequestParam String userId) {
	        if (userId == null || userId.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                 .body(null); 
	        }

	        Optional<Users> userData = userService.getUserById(userId);

	        if (userData.isPresent()) {
	            return ResponseEntity.ok(userData.get());
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(null);  
	        }
	    }

	    @PutMapping("/update")
	    public ResponseEntity<String> updateUser(
	    		@RequestParam("userid") String userId,
	    		@RequestParam("first_name") String firstName,
	            @RequestParam(value = "middle_name", required = false) String middleName,
	            @RequestParam("last_name") String lastName,
	            @RequestParam("name") String name,
	            @RequestParam("email") String email,
	            @RequestParam("phone") String phone,
	            @RequestParam("gender") String gender,
	            @RequestParam("username") String username,
	            @RequestParam("password") String password,
	            @RequestParam("employee_id") String employeeId,
	            @RequestParam("role") String role,
	            @RequestParam("address") String address,
	            @RequestParam("state") String state,
	            @RequestParam("country") String country,
	            @RequestParam("status") String status,
	            @RequestParam(value = "profile" , required = false) String profile,
	            @RequestParam(value = "file" , required = false) MultipartFile file
	    ) {
	    	if (userId == null || userId.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                 .body("User ID is required");
	        }
	        Optional<Users> existingUserOpt = userService.getUserById(userId);

	        if (existingUserOpt.isPresent()) {
	            Users user = existingUserOpt.get();
	            user.setUserid(userId);
	            user.setFirst_name(firstName);
	            user.setMiddle_name(middleName);
	            user.setLast_name(lastName);
	            user.setName(name);
	            user.setEmail(email);
	            user.setPhone(phone);
	            user.setGender(gender);
	            user.setUsername(username);
	            user.setRole(role);
	            user.setEmployee_id(employeeId);
	            user.setAddress(address);
	            user.setState(state);
	            user.setCountry(country);
	            user.setStatus(status);
	            user.setProfile(profile);

	            boolean updateStatus = userService.updateUser(user);

	            if (updateStatus) {
	                if (file != null && !file.isEmpty()) {
	                    try {
	                        ProfileImg uploadedImage = profileImgService.updateProfileImg(file, userId);
	                        userService.updateUserProfile(userId, uploadedImage.getId());
	                    } catch (Exception e) {
	                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                             .body("User updated, but failed to upload profile image: " + e.getMessage());
	                    }
	                }
	                return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
	            } else {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update user");
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	        }
	    }
}
