package com.example.qlybandocu.models;

public class MessageModel {
    boolean success;
    String message;
    String name; // (Tuỳ chọn: có thể thêm nếu sau này API trả về tên ảnh)

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}