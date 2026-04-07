package org.cttelsamicsterrassa.data.core.domain.service.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptPasswordHasher implements PasswordHasher {

    private final BCryptPasswordEncoder passwordEncoder;

    public BcryptPasswordHasher() {
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    @Override
    public String hash(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    @Override
    public boolean verify(String plainPassword, String passwordHash) {
        return passwordEncoder.matches(plainPassword, passwordHash);
    }
}

