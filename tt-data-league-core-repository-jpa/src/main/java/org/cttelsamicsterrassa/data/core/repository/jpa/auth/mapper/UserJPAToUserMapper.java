package org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.auth.Role;
import org.cttelsamicsterrassa.data.core.domain.model.auth.User;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.UserJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserJPAToUserMapper implements Function<UserJPA, User> {

    private final RoleJPAToRoleMapper roleMapper;

    @Autowired
    public UserJPAToUserMapper(RoleJPAToRoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public User apply(UserJPA userJPA) {
        Set<Role> roles = userJPA.getRoles().stream()
                .map(roleMapper)
                .collect(Collectors.toSet());
        return User.createExisting(
                userJPA.getId(),
                userJPA.getUsername(),
                userJPA.getEmail(),
                userJPA.getPasswordHash(),
                userJPA.getCreatedAt(),
                userJPA.isActive(),
                roles
        );
    }
}
