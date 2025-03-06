package com.fixnow.api.web.exception;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String userName) {
        super("User already exists with name: " + userName);
    }
}
