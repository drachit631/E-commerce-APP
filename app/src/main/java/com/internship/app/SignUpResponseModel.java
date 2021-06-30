package com.internship.app;

public class SignUpResponseModel {

    Boolean Status;
    String Message;

    public SignUpResponseModel(Boolean status , String message) {
        Status = status;
        Message = message;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
