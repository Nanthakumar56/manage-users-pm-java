package com.springboot.manage_users.service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.springboot.manage_users.entity.Users;
import com.springboot.manage_users.repository.UsersRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JavaMailSender mailSender;

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

    public String createUser(Users users) {
        try {
            // Generate user ID and set user details
            String userId = generateUserId(users);
            users.setUserid(userId);

            LocalDateTime now = LocalDateTime.now();
            users.setCreated_at(now);
            users.setStatus("Active");

            // Save the user
            usersRepository.save(users);

            // Send email asynchronously
            sendMailAsync(users.getEmail(), users.getUsername(), users.getPassword(), users.getName());

            // Return success response without waiting for email
            return "Success";
        } catch (Exception e) {
            return formatErrorMessage(e.getMessage());
        }
    }

    private String generateUserId(Users users) {
        String firstName = users.getFirst_name();
        String middleName = users.getMiddle_name();
        String lastName = users.getLast_name();

        StringBuilder userIdPrefix = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            userIdPrefix.append(firstName.charAt(0));
        }
        if (middleName != null && !middleName.isEmpty()) {
            userIdPrefix.append(middleName.charAt(0));
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
        });
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Optional<Users> getUserById(String userId) {
        return usersRepository.findByUserId(userId);
    }

    public void updateUserProfile(String userId, int profileImgId) {
        String profileImgIdString = String.valueOf(profileImgId);
        Optional<Users> userOptional = usersRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setProfile(profileImgIdString);
            user.setUpdated_at(LocalDateTime.now());
            usersRepository.save(user);
        }
    }

    public boolean updateUser(Users users) {
        try {
            Optional<Users> existingUserOpt = usersRepository.findByUserId(users.getUserid());
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

    private String formatErrorMessage(String errorMessage) {
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
}
