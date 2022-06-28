package edu.esprit.entities;

import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.annotation.FormFactory;
import com.dooapp.fxform.annotation.NonVisual;
import com.dooapp.fxform.view.factory.impl.PasswordFieldFactory;
import com.dooapp.fxform.view.factory.impl.TextAreaFactory;
import edu.esprit.enums.IdentityDocumentType;
import edu.esprit.enums.UserStatus;
import edu.esprit.lib.fx.annotations.IndexView;
import edu.esprit.lib.fx.annotations.Inflatable;
import edu.esprit.lib.fx.fxform2.extensions.BackedEnumChoiceBoxFactory;
import edu.esprit.lib.fx.fxform2.extensions.SimpleRelationTableViewFactory;
import edu.esprit.lib.fx.validation.constraint.Phone;
import edu.esprit.lib.persistence.IdentifiableEntity;
import edu.esprit.lib.persistence.annotation.Enumeration;
import edu.esprit.lib.persistence.annotation.HasMany;
import edu.esprit.lib.persistence.annotation.Timestampable;
import edu.esprit.lib.persistence.fx.PersistentListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Timestampable
@Table(name = "user")
@Accessor(value = Accessor.AccessType.METHOD)
public class User extends IdentifiableEntity {
    @Column(name = "first_name")
    @IndexView
    private SimpleStringProperty firstName = new SimpleStringProperty();

    @Column(name = "last_name")
    @IndexView
    private SimpleStringProperty lastName = new SimpleStringProperty();

    @Column(name = "email")
    @IndexView
    private SimpleStringProperty email = new SimpleStringProperty();

    @Column(name = "phone_number")
    private SimpleStringProperty phoneNumber = new SimpleStringProperty();

    @Column(name = "last_login")
    @NonVisual
    private SimpleObjectProperty<Timestamp> lastLogin = new SimpleObjectProperty<>();

    @Column(name = "last_activity_at")
    @IndexView
    @NonVisual
    private SimpleObjectProperty<Timestamp> lastActivityAt = new SimpleObjectProperty<>();

    @Column(name = "created_at")
    @IndexView
    @NonVisual
    private SimpleObjectProperty<Timestamp> createdAt = new SimpleObjectProperty<>();

    @Column(name = "updated_at")
    @IndexView
    @NonVisual
    private SimpleObjectProperty<Timestamp> updatedAt = new SimpleObjectProperty<>();

    @Transient
    @FormFactory(PasswordFieldFactory.class)
    private SimpleStringProperty plainPassword = new SimpleStringProperty();

    @Column(name = "password")
    @NonVisual
    private SimpleStringProperty password = new SimpleStringProperty();

    @Column(name = "identity_document_number")
    private SimpleStringProperty identityDocumentNumber = new SimpleStringProperty();

    @Column(name = "is_verified")
    private SimpleBooleanProperty isVerified = new SimpleBooleanProperty();

    @Column(name = "about")
    @FormFactory(TextAreaFactory.class)
    private SimpleStringProperty about = new SimpleStringProperty();

    @Column(name = "user_status")
    @Enumeration(type = UserStatus.class)
    @FormFactory(BackedEnumChoiceBoxFactory.class)
    private SimpleObjectProperty<UserStatus> userStatus = new SimpleObjectProperty<>(UserStatus.PENDING);

    @Column(name = "identity_type")
    @Enumeration(type = IdentityDocumentType.class)
    private SimpleObjectProperty<IdentityDocumentType> identityDocumentType = new SimpleObjectProperty<>(IdentityDocumentType.UNKNOWN);

    @HasMany(TargetEntity = Group.class, JoinTable = "user_group", LocalColumn = "user_id", ForeignColumn = "group_id")
    @Inflatable(fieldName = "displayName")
    @FormFactory(SimpleRelationTableViewFactory.class)
    private PersistentListProperty<Group> groups = new PersistentListProperty<>();

    @Column(name = "google_id")
    private SimpleStringProperty googleId = new SimpleStringProperty();

    public ObservableList<Group> getGroups() {
        return groups.get();
    }

    public PersistentListProperty<Group> groupsProperty() {
        return groups;
    }

    public void setGroups(ObservableList<Group> groups) {
        this.groups.set(groups);
    }

    public void addGroup(Group group) {
        this.groups.add(group);
    }

    public void removeGroup(Group group) {
        this.groups.remove(group);
    }

    @NotNull
    public IdentityDocumentType getIdentityDocumentType() {
        return identityDocumentType.get();
    }

    public SimpleObjectProperty<IdentityDocumentType> identityDocumentTypeProperty() {
        return identityDocumentType;
    }

    public void setIdentityDocumentType(IdentityDocumentType identityDocumentType) {
        this.identityDocumentType.set(identityDocumentType);
    }

    @NotNull
    public UserStatus getUserStatus() {
        return userStatus.get();
    }

    public SimpleObjectProperty<UserStatus> userStatusProperty() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus.set(userStatus);
    }

    @NotNull
    @Size(min = 2, max = 30)
    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    @NotNull
    @Size(min = 2, max = 30)
    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    @Email
    @NotNull
    @Size(min = 5, max = 30)
    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    @Size(min = 2, max = 30)
    @Phone
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    @NonVisual
    public Timestamp getLastLogin() {
        return lastLogin.get();
    }

    public SimpleObjectProperty<Timestamp> lastLoginProperty() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin.set(lastLogin);
    }

    @NonVisual
    public Timestamp getLastActivityAt() {
        return lastActivityAt.get();
    }

    public SimpleObjectProperty<Timestamp> lastActivityAtProperty() {
        return lastActivityAt;
    }

    public void setLastActivityAt(Timestamp lastActivityAt) {
        this.lastActivityAt.set(lastActivityAt);
    }

    public Timestamp getCreatedAt() {
        return createdAt.get();
    }

    public SimpleObjectProperty<Timestamp> createdAtProperty() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt.set(createdAt);
    }

    public Timestamp getUpdatedAt() {
        return updatedAt.get();
    }

    public SimpleObjectProperty<Timestamp> updatedAtProperty() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt.set(updatedAt);
    }

    @Size(min = 8, max = 32)
    public String getPlainPassword() {
        return plainPassword.get();
    }

    public SimpleStringProperty plainPasswordProperty() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword.set(plainPassword);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    @Size(min = 8, max = 12)
    @NotNull
    public String getIdentityDocumentNumber() {
        return identityDocumentNumber.get();
    }

    public SimpleStringProperty identityDocumentNumberProperty() {
        return identityDocumentNumber;
    }

    public void setIdentityDocumentNumber(String identityDocumentNumber) {
        this.identityDocumentNumber.set(identityDocumentNumber);
    }

    public boolean isIsVerified() {
        return isVerified.get();
    }

    public SimpleBooleanProperty isVerifiedProperty() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified.set(isVerified);
    }

    @Size(max = 512)
    public String getAbout() {
        return about.get();
    }

    public SimpleStringProperty aboutProperty() {
        return about;
    }

    public void setAbout(String about) {
        this.about.set(about);
    }

    @Size(max = 16)
    public String getGoogleId() {
        return googleId.get();
    }

    public SimpleStringProperty googleIdProperty() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId.set(googleId);
    }

    public User() {

    }

    @PreUpdate
    @PrePersist
    private void updatePassword(){
        if(!StringUtils.isEmpty(plainPassword.get())){
            this.password.set("a very secure hash I will implement once I have time");
            this.setPlainPassword("");
        }
    }
}
