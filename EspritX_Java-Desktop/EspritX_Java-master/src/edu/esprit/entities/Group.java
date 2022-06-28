package edu.esprit.entities;

import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.annotation.FormFactory;
import edu.esprit.enums.GroupType;
import edu.esprit.lib.fx.annotations.IndexView;
import edu.esprit.lib.fx.annotations.Inflatable;
import edu.esprit.lib.fx.fxform2.extensions.BackedEnumChoiceBoxFactory;
import edu.esprit.lib.fx.fxform2.extensions.SimpleRelationTableViewFactory;
import edu.esprit.lib.persistence.IdentifiableEntity;
import edu.esprit.lib.persistence.annotation.Enumeration;
import edu.esprit.lib.persistence.annotation.HasMany;
import edu.esprit.lib.persistence.fx.PersistentListProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Table(name = "group")
@Accessor(value = Accessor.AccessType.METHOD)
public class Group extends IdentifiableEntity {

    @IndexView
    @Column(name = "display_name")
    private SimpleStringProperty displayName = new SimpleStringProperty();

    @Column(name = "security_title")
    private SimpleStringProperty securityTitle = new SimpleStringProperty();

    @IndexView
    @Column(name = "group_type")
    @Enumeration(type = GroupType.class)
    @FormFactory(BackedEnumChoiceBoxFactory.class)
    private SimpleObjectProperty<GroupType> groupType = new SimpleObjectProperty<>();

    @HasMany(TargetEntity = User.class, JoinTable = "user_group", LocalColumn = "group_id", ForeignColumn = "user_id")
    @FormFactory(SimpleRelationTableViewFactory.class)
    @Inflatable(fieldName = "email")
    private ListProperty<User> members = new PersistentListProperty<>();

    public Group() {
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        members.remove(user);
    }

    public ObservableList<User> getMembers() {
        return members.get();
    }

    public ListProperty<User> membersProperty() {
        return members;
    }

    public void setMembers(ObservableList<User> members) {
        this.members.set(members);
    }

    @NotNull
    public GroupType getGroupType() {
        return groupType.get();
    }

    public SimpleObjectProperty<GroupType> groupTypeProperty() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType.set(groupType);
    }

    @NotNull
    @Size(min = 5, max = 64)
    public String getDisplayName() {
        return displayName.get();
    }

    public SimpleStringProperty displayNameProperty() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName.set(displayName);
    }

    @NotNull
    @Size(min = 5, max = 64)
    public String getSecurityTitle() {
        return securityTitle.get();
    }

    public SimpleStringProperty securityTitleProperty() {
        return securityTitle;
    }

    public void setSecurityTitle(String securityTitle) {
        this.securityTitle.set(securityTitle);
    }
}
