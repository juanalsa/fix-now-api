package com.fixnow.api.web.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String id) {
        super("User not found with id: " + id);
    }
}
