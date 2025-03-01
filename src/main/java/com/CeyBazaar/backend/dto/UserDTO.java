package com.CeyBazaar.backend.dto;

import com.CeyBazaar.backend.entity.User;
import com.CeyBazaar.backend.entity.UserType;
import lombok.Data;

@Data
public class UserDTO {
    private int userId;
    private String userName;
    private String userEmail;
    private String password;
    private UserType userType;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
