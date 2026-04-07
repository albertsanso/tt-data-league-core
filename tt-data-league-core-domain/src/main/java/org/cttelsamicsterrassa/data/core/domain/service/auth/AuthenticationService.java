package org.cttelsamicsterrassa.data.core.domain.service.auth;

import org.cttelsamicsterrassa.data.core.domain.model.auth.User;
import org.cttelsamicsterrassa.data.core.domain.repository.auth.UserRepository;

import java.util.Optional;
import java.util.UUID;

public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final UserValidator userValidator;

    public AuthenticationService(UserRepository userRepository, PasswordHasher passwordHasher, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.userValidator = userValidator;
    }

    public User registerUser(String username, String email, String plainPassword) {
        userValidator.validateOrThrow(username, email, plainPassword);

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = User.createExisting(
                UUID.randomUUID(),
                username,
                email,
                passwordHasher.hash(plainPassword),
                java.time.LocalDateTime.now(),
                true
        );

        userRepository.save(user);
        return user;
    }

    public Optional<User> authenticateUser(String username, String plainPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();
        if (!user.isActive()) {
            return Optional.empty();
        }

        boolean isValidPassword = passwordHasher.verify(plainPassword, user.getPasswordHash());
        return isValidPassword ? Optional.of(user) : Optional.empty();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void disableUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(InvalidCredentialsException::new);
        user.disable();
        userRepository.save(user);
    }

    public void enableUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(InvalidCredentialsException::new);
        user.enable();
        userRepository.save(user);
    }

    public void changeUserPassword(UUID userId, String newPlainPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(InvalidCredentialsException::new);

        if (!userValidator.validatePassword(newPlainPassword).isEmpty()) {
            throw new ValidationException("Invalid password format");
        }

        userRepository.save(User.createExisting(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                passwordHasher.hash(newPlainPassword),
                user.getCreatedAt(),
                user.isActive()
        ));
    }
}


