package com.revhire.userservice.exceptions;

public class InvalidCredentialsException extends Exception{
    String message;
    public InvalidCredentialsException(String message){
        this.message=message;
    }
}