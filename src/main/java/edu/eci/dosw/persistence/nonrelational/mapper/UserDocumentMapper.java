package edu.eci.dosw.persistence.nonrelational.mapper;

import edu.eci.dosw.model.User;
import edu.eci.dosw.persistence.nonrelational.document.UserDocument;

public class UserDocumentMapper {

    private UserDocumentMapper() {}

    public static User toDomain(UserDocument doc) {
        User user = new User();
        user.setId(doc.getId());
        user.setName(doc.getName());
        user.setEmail(doc.getEmail());
        user.setUsername(doc.getUsername());
        user.setPassword(doc.getPassword());
        user.setRole(doc.getRole());
        user.setMembershipType(doc.getMembershipType());
        user.setDateAddedAsUser(doc.getDateAddedAsUser());
        return user;
    }

    public static UserDocument toDocument(User user) {
        UserDocument doc = new UserDocument();
        doc.setId(user.getId());
        doc.setName(user.getName());
        doc.setEmail(user.getEmail());
        doc.setUsername(user.getUsername());
        doc.setPassword(user.getPassword());
        doc.setRole(user.getRole());
        doc.setMembershipType(user.getMembershipType());
        doc.setDateAddedAsUser(user.getDateAddedAsUser());
        return doc;
    }
}
