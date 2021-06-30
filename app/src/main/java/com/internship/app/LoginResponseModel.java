package com.internship.app;

import java.util.List;

public class LoginResponseModel {

    Boolean Status;
    String Message;
    List<LoginResultModel> Response;

    public LoginResponseModel(Boolean status , String message , List<LoginResultModel> response) {
        Status = status;
        Message = message;
        Response = response;
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

    public List<LoginResultModel> getResponse() {
        return Response;
    }

    public void setResponse(List<LoginResultModel> response) {
        Response = response;
    }
}
