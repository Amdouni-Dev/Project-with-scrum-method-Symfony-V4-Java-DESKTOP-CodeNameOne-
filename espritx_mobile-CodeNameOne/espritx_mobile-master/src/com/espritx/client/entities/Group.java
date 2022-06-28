package com.espritx.client.entities;

import com.codename1.properties.*;

public class Group implements PropertyBusinessObject {
    public final IntProperty<Group> id = new IntProperty<>("id");
    public final Property<String, Group> display_name = new Property<>("display_name");
    public final Property<String, Group> security_title = new Property<>("security_title");
    public final Property<String, Group> groupType = new Property<>("groupType");
    public final ListProperty<User, Group> members = new ListProperty<>("members", User.class);
    public final PropertyIndex idx = new PropertyIndex(this, "Group", id, display_name, security_title, groupType, members);

    @Override
    public PropertyIndex getPropertyIndex() {
        return idx;
    }

    public Group() {
        this.display_name.setLabel("Display Name");
        this.security_title.setLabel("Security Title");
        this.groupType.setLabel("Group Type");
    }

    @Override
    public String toString() {
        return display_name.toString();
    }
}
