package org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.auth.User;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.RoleJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.UserJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserToUserJPAMapper implements Function<User, UserJPA> {

    private final RoleToRoleJPAMapper roleMapper;

    @Autowired
    public UserToUserJPAMapper(RoleToRoleJPAMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public UserJPA apply(User user) {
        UserJPA userJPA = new UserJPA();
        userJPA.setId(user.getId());
        userJPA.setUsername(user.getUsername());
        userJPA.setEmail(user.getEmail());
        userJPA.setPasswordHash(user.getPasswordHash());
        userJPA.setCreatedAt(user.getCreatedAt());
        userJPA.setActive(user.isActive());
        Set<RoleJPA> roles = user.getRoles().stream()
                .map(roleMapper)
                .collect(Collectors.toSet());
        userJPA.setRoles(roles);
        return userJPA;
    }
}
