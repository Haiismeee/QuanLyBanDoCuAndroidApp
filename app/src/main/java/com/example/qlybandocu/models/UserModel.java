package com.example.qlybandocu.models;

public class UserModel {
    private boolean success;
    private String message;
    private User result;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public User getResult() { return result; }

    public static class User {
        private int id;
        private String name;
        private String email;
        private String phone;

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }

        public String getPhone() {
            return phone;
        }
    }
}
