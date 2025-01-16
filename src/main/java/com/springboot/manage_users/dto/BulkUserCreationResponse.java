package com.springboot.manage_users.dto;

import java.util.List;

public class BulkUserCreationResponse {
    private int successfulCount;
    private int failedCount;
    private List<FailedUser> failedUsers;

    public BulkUserCreationResponse(int successfulCount, int failedCount, List<FailedUser> failedUsers) {
        this.successfulCount = successfulCount;
        this.failedCount = failedCount;
        this.failedUsers = failedUsers;
    }

    public int getSuccessfulCount() {
        return successfulCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public List<FailedUser> getFailedUsers() {
        return failedUsers;
    }

    public static class FailedUser {
        private String name;
        private String email;
        private String errorMessage;

        public FailedUser(String name, String email, String errorMessage) {
            this.name = name;
            this.email = email;
            this.errorMessage = errorMessage;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
