package com.revhire.userservice.exceptions;

import lombok.Data;

@Data
public class InvalidEmailException extends Exception{
    private String message;
    public InvalidEmailException(String message){
        this.message=message;
    }
}