package org.cttelsamicsterrassa.data.core.domain.service.auth;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}

