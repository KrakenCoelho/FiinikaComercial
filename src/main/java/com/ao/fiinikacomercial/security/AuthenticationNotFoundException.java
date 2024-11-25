package com.ao.fiinikacomercial.security;

public class AuthenticationNotFoundException extends RuntimeException {
    public AuthenticationNotFoundException(String message) {
        super(message);
    }
}