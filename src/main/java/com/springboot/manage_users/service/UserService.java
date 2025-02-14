package com.springboot.manage_users.service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.manage_users.dto.BulkUserCreationResponse;
import com.springboot.manage_users.dto.BulkUserCreationResponse.FailedUser;
import com.springboot.manage_users.dto.ProjectUserDto;
import com.springboot.manage_users.dto.UserDTO;
import com.springboot.manage_users.entity.ProfileImg;
import com.springboot.manage_users.entity.Users;
import com.springboot.manage_users.repository.UsersRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private ProfileImgService profileImgService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @PostConstruct
    public void init() {
        if (mailSender == null) {
            System.out.println("JavaMailSender is null");
        } else {
            System.out.println("JavaMailSender is initialized");
        }
    }

    public String createUser(Users users, boolean sendmail) {
        try {
            // Generate user ID and set user details
            String userId = generateUserId(users);
            users.setUserid(userId);

            LocalDateTime now = LocalDateTime.now();
            users.setCreated_at(now);
            users.setStatus("Active");

            usersRepository.save(users);

            if(sendmail) {
                sendMailAsync(users.getEmail(), users.getUsername(), users.getPassword(), users.getFirst_name());
            }

            return "Success";
        } catch (Exception e) {
            return formatErrorMessage(e.getMessage());
        }
    }
           
    public BulkUserCreationResponse createUsers(List<Users> userRequests) {
        List<String> createdUserIds = new ArrayList<>();
        List<FailedUser> failedUsers = new ArrayList<>();

        for (Users user : userRequests) {
            try {
                String userId = generateUserId(user);
                user.setUserid(userId);
                user.setCreated_at(LocalDateTime.now());
                user.setStatus("Active");

                usersRepository.save(user);

                sendMailAsync(user.getEmail(), user.getUsername(), user.getPassword(), user.getFirst_name());

                createdUserIds.add(userId);
            } catch (Exception e) {
            	System.err.println(formatErrorMessage(e.getMessage()));
                FailedUser failedUser = new BulkUserCreationResponse.FailedUser(
                    user.getFirst_name() + " " + user.getLast_name(),
                    user.getEmail(),
                    formatErrorMessage(e.getMessage())
                );
                failedUsers.add(failedUser);
            }
        }

        int successfulCount = createdUserIds.size();
        int failedCount = failedUsers.size();

        return new BulkUserCreationResponse(successfulCount, failedCount, failedUsers);
    }

    private String generateUserId(Users users) {
        String firstName = users.getFirst_name();
        String lastName = users.getLast_name();

        StringBuilder userIdPrefix = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            userIdPrefix.append(firstName.charAt(0));
        }
        if (lastName != null && !lastName.isEmpty()) {
            userIdPrefix.append(lastName.charAt(0));
        }

        String lastUserId = usersRepository.findLastUserId();
        int nextId = 1;
        if (lastUserId != null && lastUserId.length() > 3) {
            String numericPart = lastUserId.substring(3);
            nextId = Integer.parseInt(numericPart) + 1;
        }

        return String.format("%s%05d", userIdPrefix, nextId);
    }

    @Async("taskExecutor")
    public void sendMailAsync(String email, String username, String password, String name) {
        CompletableFuture.runAsync(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo(email);
                message.setSubject("Your Account Has Been Successfully Created");
                message.setText("Dear " + name + ",\n\n" +
                    "Your account has been successfully created. Below are your login details:\n\n" +
                    "Username: " + username + "\n" +
                    "Password: " + password + "\n\n" +
                    "Please make sure to keep this information safe and secure. If you have any concerns, feel free to contact us.\n\n" +
                    "Best regards,\nVMA LABS");

                mailSender.send(message);
            } catch (Exception e) {
                System.out.println("Error sending email to: " + email);
                e.printStackTrace();  
            }
        });
    }

    public List<UserDTO> getAllUsers() {
        List<Users> userList = usersRepository.findAll();
        
        // Convert each Users entity to UserDTO
        List<UserDTO> userDTOList = userList.stream()
            .map(user -> new UserDTO(
                user.getUserid(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getRole(),
                user.getEmail(),
                user.getCreated_at(),
                user.getStatus()))
            .collect(Collectors.toList());
        
        return userDTOList;
    }
    
    public List<UserDTO> getUsersByIds(List<String> userIds) {
        List<Users> userList = usersRepository.findAllById(userIds);

        List<UserDTO> userDTOList = userList.stream()
            .map(user -> new UserDTO(
                user.getUserid(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getRole(),
                user.getEmail(),
                user.getCreated_at(),
                user.getStatus()))
            .collect(Collectors.toList());

        return userDTOList;
    }

    
    public Optional<Users> getUserById(String userId) {
        return usersRepository.findById(userId);
    }

    public void updateUserProfile(String userId, String profileImgId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setProfile(profileImgId);
            user.setUpdated_at(LocalDateTime.now());
            usersRepository.save(user);
        }
    }

    public boolean updateUser(Users users) {
        try {
            Optional<Users> existingUserOpt = usersRepository.findById(users.getUserid());
            if (existingUserOpt.isPresent()) {
                Users existingUser = existingUserOpt.get();

                Field[] fields = users.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(users);
                    if (value != null) {
                        field.set(existingUser, value);
                    }
                }

                existingUser.setUpdated_at(LocalDateTime.now());
                usersRepository.save(existingUser);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String formatErrorMessage(String errorMessage) {
        String detail = "Unknown error";
        int keyStartIndex = errorMessage.indexOf("Key (");
        if (keyStartIndex != -1) {
            int keyEndIndex = errorMessage.indexOf(')', keyStartIndex);
            if (keyEndIndex != -1) {
                String key = errorMessage.substring(keyStartIndex + 4, keyEndIndex).trim();
                int valueStartIndex = errorMessage.indexOf('(', keyEndIndex) + 1;
                int valueEndIndex = errorMessage.indexOf(')', valueStartIndex);
                if (valueEndIndex != -1) {
                    String value = errorMessage.substring(valueStartIndex, valueEndIndex).trim();
                    detail = key + " " + value + " already exists";
                }
            }
        }
        return detail.replace("(", "").replace(")", "").replace("=", "").trim();
    }
    
    public String updateCredential(Users user) {
        Optional<Users> userData = getUserById(user.getUserid());
        
        if (userData.isPresent()) {
           try {
        	   Users existingUser = userData.get();
               existingUser.setUsername(user.getUsername()); 
               existingUser.setPassword(user.getPassword()); 
               
               usersRepository.save(existingUser);
               
               return "Credentials updated successfully.";
           }
           catch(Exception e)
           {
        	   return e.getMessage();
           }
        } else {
            return "User not found.";
        }
    }
    
    public String updateStatus(Users user) {
        Optional<Users> userData = getUserById(user.getUserid());
        
        if (userData.isPresent()) {
           try {
        	   Users existingUser = userData.get();
               existingUser.setStatus(user.getStatus()); 
               
               usersRepository.save(existingUser);
               
               return "Status updated successfully.";
           }
           catch(Exception e)
           {
        	   return e.getMessage();
           }
        } else {
            return "User not found.";
        }
    }
    
    @Async("taskExecutor")
    public void sendPasswordResetEmail(String email, String name, String resetLink) {
        CompletableFuture.runAsync(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo(email);
                message.setSubject("Reset Your Password");
                message.setText("Hi " + name + ",\n\n" +
                    "We received a request to reset your password. You can reset it by clicking the link below:\n\n" +
                    resetLink + "\n\n" +
                    "If you didn’t request this, you can safely ignore this email. Your password will remain unchanged.\n\n" +
                    "For security purposes, this link will expire in 24 hours.\n\n" +
                    "If you have any questions or need assistance, feel free to contact our support team.\n\n" +
                    "Best regards,\n" +
                    "VMA LABS Team");

                mailSender.send(message);
            } catch (Exception e) {
                System.out.println("Error sending email to: " + email);
                e.printStackTrace();
            }
        });
    }
    
    public String getName(String userId) {
        Optional<Users> user = usersRepository.findById(userId);

        return user.map(u -> {
                    String firstName = u.getFirst_name();
                    String lastName = u.getLast_name();
                    return firstName + (lastName != null && !lastName.isEmpty() ? " " + lastName : "");
                })
                .orElse("User not found");
    }
    
    public List<ProjectUserDto> getAllProjectUsers(String searchTerm, List<String> userIds) {
        List<Users> userList = usersRepository.findAll();

        return userList.stream()
            .filter(user -> {
                if (userIds != null && userIds.contains(user.getUserid())) {
                    return false;
                }


                if (searchTerm == null || searchTerm.trim().isEmpty()) {
                    return true;
                }
                String fullName = user.getFirst_name() + " " + user.getLast_name();
                return fullName.toLowerCase().contains(searchTerm.toLowerCase());
            })
            .map(user -> {
                Optional<ProfileImg> profileImgOpt = profileImgService.getProfileImg(user.getUserid());
                byte[] file = profileImgOpt.map(ProfileImg::getGrp_data).orElse(null);

                return new ProjectUserDto(
                    user.getUserid(),
                    user.getFirst_name(),
                    user.getLast_name(),
                    user.getRole(),
                    user.getStatus(),
                    file
                );
            })
            .collect(Collectors.toList());
    }
    
    public List<ProjectUserDto> getTaskUsersWithinIds(String searchTerm, List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList(); // Return empty if no user IDs are provided
        }

        // Fetch only users whose IDs are in the provided list
        List<Users> userList = usersRepository.findByUseridIn(userIds);

        return userList.stream()
            .filter(user -> {
                if (searchTerm == null || searchTerm.trim().isEmpty()) {
                    return true; // No search term, return all users within the given userIds
                }
                String fullName = user.getFirst_name() + " " + user.getLast_name();
                return fullName.toLowerCase().contains(searchTerm.toLowerCase());
            })
            .map(user -> {
                Optional<ProfileImg> profileImgOpt = profileImgService.getProfileImg(user.getUserid());
                byte[] file = profileImgOpt.map(ProfileImg::getGrp_data).orElse(null);

                return new ProjectUserDto(
                    user.getUserid(),
                    user.getFirst_name(),
                    user.getLast_name(),
                    user.getRole(),
                    user.getStatus(),
                    file
                );
            })
            .collect(Collectors.toList());
    }

    
    public List<ProjectUserDto> getAllProjectUsersFilled(List<String> userIds) {
    	System.out.println("UserIds: " + userIds);

        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();  
        }

        List<Users> userList = usersRepository.findAllById(userIds);
        System.out.println("User List: " + userList);

        return userList.stream()
            .map(user -> {
                Optional<ProfileImg> profileImgOpt = profileImgService.getProfileImg(user.getUserid());
                byte[] file = profileImgOpt.map(ProfileImg::getGrp_data).orElse(null);

                return new ProjectUserDto(
                    user.getUserid(),
                    user.getFirst_name(),
                    user.getLast_name(),
                    user.getRole(),
                    user.getStatus(),
                    file
                );
            })
            .collect(Collectors.toList());
    }

}
