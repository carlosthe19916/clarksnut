package org.clarksnut.models.db.jpa.entity;

import org.clarksnut.common.jpa.CreatableEntity;
import org.clarksnut.common.jpa.CreatedAtListener;
import org.clarksnut.common.jpa.UpdatableEntity;
import org.clarksnut.common.jpa.UpdatedAtListener;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cl_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "identity_id")
}, indexes = {
        @Index(columnList = "username", unique = true),
        @Index(columnList = "identity_id", unique = true)
})
@EntityListeners({CreatedAtListener.class, UpdatedAtListener.class})
@NamedQueries({
        @NamedQuery(name = "getAllUsers", query = "select u from UserEntity u order by u.username"),
        @NamedQuery(name = "getUserByUsername", query = "select u from UserEntity u where u.username = :username"),
        @NamedQuery(name = "getUserByIdentityID", query = "select u from UserEntity u where u.identityID = :identityID"),
        @NamedQuery(name = "getUserWithOfflineToken", query = "select u from UserEntity u left join fetch u.linkedBrokers b where u.offlineToken is not null order by u.username")
})
public class UserEntity implements CreatableEntity, UpdatableEntity, Serializable {

    @Id
    @Access(AccessType.PROPERTY)// Relationships often fetch id, but not entity.  This avoids an extra SQL
    @Column(name = "id", length = 36)
    private String id;

    @NotNull
    @Column(name = "identity_id")
    private String identityID;

    @NotNull
    @Column(name = "provider_type")
    private String providerType;

    @NotNull
    @Column(name = "username")
    private String username;

    @Size(max = 255)
    @Column(name = "full_name")
    private String fullName;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageURL;

    @Size(max = 255)
    @Column(name = "bio")
    private String bio;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Size(max = 255)
    @Column(name = "company")
    private String company;

    @Size(max = 255)
    @Column(name = "url")
    private String url;

    @Column(name = "email_theme")
    private String emailTheme;

    @Column(name = "language")
    private String language;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @NotNull
    @Type(type = "org.hibernate.type.TrueFalseType")
    @Column(name = "registration_complete")
    private boolean registrationCompleted;

    @Size(max = 2048)
    @Column(name = "offline_token", length = 2048)
    private String offlineToken;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<SpaceEntity> ownedSpaces = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<CollaboratorEntity> collaboratedSpaces = new HashSet<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserContextInformationEntity contextInformation;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<UserLinkedBrokerEntity> linkedBrokers = new HashSet<>();

    @ElementCollection
    @Column(name = "value")
    @CollectionTable(name = "favorite_spaces", joinColumns = {@JoinColumn(name = "user_id")})
    private Set<String> favoriteSpaces = new HashSet<>();

    @Version
    @Column(name = "version")
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentityID() {
        return identityID;
    }

    public void setIdentityID(String identityID) {
        this.identityID = identityID;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isRegistrationCompleted() {
        return registrationCompleted;
    }

    public void setRegistrationCompleted(boolean registrationCompleted) {
        this.registrationCompleted = registrationCompleted;
    }

    public String getOfflineToken() {
        return offlineToken;
    }

    public void setOfflineToken(String offlineToken) {
        this.offlineToken = offlineToken;
    }

    public Set<SpaceEntity> getOwnedSpaces() {
        return ownedSpaces;
    }

    public void setOwnedSpaces(Set<SpaceEntity> ownedSpaces) {
        this.ownedSpaces = ownedSpaces;
    }

    public void setContextInformation(UserContextInformationEntity contextInformation) {
        this.contextInformation = contextInformation;
    }

    public UserContextInformationEntity getContextInformation() {
        return contextInformation;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Set<UserLinkedBrokerEntity> getLinkedBrokers() {
        return linkedBrokers;
    }

    public void setLinkedBrokers(Set<UserLinkedBrokerEntity> linkedBrokers) {
        this.linkedBrokers = linkedBrokers;
    }

    public Set<CollaboratorEntity> getCollaboratedSpaces() {
        return collaboratedSpaces;
    }

    public void setCollaboratedSpaces(Set<CollaboratorEntity> collaboratedSpaces) {
        this.collaboratedSpaces = collaboratedSpaces;
    }

    public Set<String> getFavoriteSpaces() {
        return favoriteSpaces;
    }

    public void setFavoriteSpaces(Set<String> favoriteSpaces) {
        this.favoriteSpaces = favoriteSpaces;
    }

    public String getEmailTheme() {
        return emailTheme;
    }

    public void setEmailTheme(String emailTheme) {
        this.emailTheme = emailTheme;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String locale) {
        this.language = locale;
    }
}
