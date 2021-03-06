package org.clarksnut.models;

import java.util.Date;
import java.util.List;

public interface SpaceModel {

    String NAME = "name";
    String ASSIGNED_ID = "assignedId";

    String getId();
    String getAssignedId();
    UserModel getOwner();

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    Date getCreatedAt();
    Date getUpdatedAt();

    List<UserModel> getCollaborators();
    List<UserModel> getCollaborators(int offset, int limit);

    void addCollaborators(UserModel user);
    boolean removeCollaborators(UserModel user);
}
