package com.espritx.client.entities;

import com.codename1.properties.*;

import java.util.Objects;

public class Message implements PropertyBusinessObject {

    public final IntProperty<Message> id = new IntProperty<>("id");
    public final Property<String, Message> content = new Property<>("content");
    public final Property<User, Message> author=new Property<>("author",User.class);
    public final IntProperty<Message> Conversationid = new IntProperty<>("Conversationid");
    public final PropertyIndex idx = new PropertyIndex(this, "Message",
            id,content,author, Conversationid);
    /*,author*/
    @Override
    public PropertyIndex getPropertyIndex() {
        return idx;
    }
    public Message() {
        id.setLabel("id");
        author.setLabel("author");
        content.setLabel("content");
        Conversationid.setLabel("Conversationid");
    }
 @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", Author="  + author.get().first_name+"Content="+content+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message conv = (Message) o;
        return id.getInt() == conv.id.getInt();
    }

    public IntProperty<Message> getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
