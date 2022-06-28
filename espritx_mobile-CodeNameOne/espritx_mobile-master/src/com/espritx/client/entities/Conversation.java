package com.espritx.client.entities;

import com.codename1.properties.*;

import java.util.Objects;

public class Conversation implements PropertyBusinessObject {

    public final IntProperty<Conversation> id = new IntProperty<>("id");
    public final ListProperty<User, Conversation> participants = new ListProperty<>("participants",User.class);
    public final PropertyIndex idx = new PropertyIndex(this, "Conversation",
            id, participants);
    @Override
    public PropertyIndex getPropertyIndex() {
        return idx;
    }
    public Conversation() {
        id.setLabel("id");
        participants.setLabel("participants");
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", participants=" + participants.get(0).first_name + participants.get(1).first_name+
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conversation)) return false;
        Conversation conv = (Conversation) o;
        return id.getInt() == conv.id.getInt();
    }

    public IntProperty<Conversation> getId() {
        return id;
    }

    public ListProperty<User, Conversation> getParticipants() {
        return participants;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
