package com.revhire.userservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    private String username;
    private String password;

    @Override
    public String toString() {
        return "AuthRequest [username=" + username + ", password=" + password + "]";
    }
}