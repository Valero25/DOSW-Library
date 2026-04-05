package edu.eci.dosw.persistence.relational.mapper;

import edu.eci.dosw.model.User;
import edu.eci.dosw.persistence.relational.entity.UserEntity;

public class UserEntityMapper {

    private UserEntityMapper() {}

    public static User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setMembershipType(entity.getMembershipType());
        user.setDateAddedAsUser(entity.getDateAddedAsUser());
        return user;
    }

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setMembershipType(user.getMembershipType());
        entity.setDateAddedAsUser(user.getDateAddedAsUser());
        return entity;
    }
}
