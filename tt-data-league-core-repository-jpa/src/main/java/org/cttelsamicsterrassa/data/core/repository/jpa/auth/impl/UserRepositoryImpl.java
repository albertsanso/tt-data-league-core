package org.cttelsamicsterrassa.data.core.repository.jpa.auth.impl;

import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.auth.User;
import org.cttelsamicsterrassa.data.core.domain.repository.auth.UserRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.UserJPAToUserMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.UserToUserJPAMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserRepositoryHelper userRepositoryHelper;
    private final UserJPAToUserMapper userJPAToUserMapper;
    private final UserToUserJPAMapper userToUserJPAMapper;

    @Autowired
    public UserRepositoryImpl(UserRepositoryHelper userRepositoryHelper, UserJPAToUserMapper userJPAToUserMapper, UserToUserJPAMapper userToUserJPAMapper) {
        this.userRepositoryHelper = userRepositoryHelper;
        this.userJPAToUserMapper = userJPAToUserMapper;
        this.userToUserJPAMapper = userToUserJPAMapper;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepositoryHelper.findById(id).map(userJPAToUserMapper);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepositoryHelper.findByUsername(username).map(userJPAToUserMapper);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositoryHelper.findByEmail(email).map(userJPAToUserMapper);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        userRepositoryHelper.findAll().forEach(userJPA -> users.add(userJPAToUserMapper.apply(userJPA)));
        return users;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepositoryHelper.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepositoryHelper.existsByEmail(email);
    }

    @Override
    public void save(User user) {
        userRepositoryHelper.save(userToUserJPAMapper.apply(user));
    }

    @Override
    public void delete(User user) {
        userRepositoryHelper.delete(userToUserJPAMapper.apply(user));
    }

    @Override
    public void deleteById(UUID id) {
        userRepositoryHelper.deleteById(id);
    }
}

