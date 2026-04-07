package org.cttelsamicsterrassa.data.core.domain.service.auth;

public interface PasswordHasher {
    String hash(String plainPassword);
    boolean verify(String plainPassword, String passwordHash);
}

