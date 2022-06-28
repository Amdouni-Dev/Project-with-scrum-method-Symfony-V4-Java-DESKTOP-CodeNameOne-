package com.espritx.client.entities;

import com.codename1.properties.*;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.URLImage;
import com.espritx.client.utils.StringUtils;

import java.util.Date;

public class Request implements PropertyBusinessObject {
    public final IntProperty<Request> id = new IntProperty<>("id");
    public final Property<Date, Request> RespondedAt = new Property<>("RespondedAt", Date.class);
    public final Property<Date, Request> CreatedAt = new Property<>("CreatedAt", Date.class);
    public final Property<Date, Request> UpdatedAt = new Property<>("UpdatedAt", Date.class);
    public final Property<String, Request> Title = new Property<>("Title");
    public final Property<String, Request> Description = new Property<>("Description");
    public final Property<Service, Request> Type = new Property<>("Type",Service.class);
    public final Property<String, Request> Email = new Property<>("Email");
    public final Property<String, Request> Picture = new Property<>("Picture");
    public final Property<String, Request> Attachement = new Property<>("Attachement");
    public final Property<String, Request> Status = new Property<>("Status");
    public final Property<String, Request> Response = new Property<>("Response");
    public final Property<User, Request> Requester = new Property<>("Requester", User.class);

    public final PropertyIndex index = new PropertyIndex(this, "Request",
            id, RespondedAt, CreatedAt, UpdatedAt, Title, Description, Type, Email, Picture, Attachement, Status, Response, Requester
    );

    @Override
    public PropertyIndex getPropertyIndex() {
        return index;
    }

    public Request() {
        Title.setLabel("Title");
        Description.setLabel("Description");
        Email.setLabel("Email");
        Picture.setLabel("Picture");
        Attachement.setLabel("Attachement");
        CreatedAt.setLabel("Created At");
    }
    public EncodedImage getEncodedPic() {
        return this.getEncodedPic(6);
    }

    public EncodedImage getEncodedPic(int _size) {
        int size = Display.getInstance().convertToPixels(_size, true);
        EncodedImage placeholder = EncodedImage.createFromImage(FontImage.createFixed("" + FontImage.MATERIAL_IMAGE, FontImage.getMaterialDesignFont(), 0xff, size, size), true);
        /*if (Picture.get() != null) {
            String[] fragments = StringUtils.split(this.Picture.get(), "/");
            String storageName = fragments[fragments.length - 1];
            URLImage photo = URLImage.createToStorage(placeholder, storageName, Picture.get());
            return photo;
        } else return placeholder;*/
        return placeholder;
    }

    public EncodedImage getEncodedFile(int _size) {
        int size = Display.getInstance().convertToPixels(_size, true);
        EncodedImage placeholder = EncodedImage.createFromImage(FontImage.createFixed("" + FontImage.MATERIAL_PERSON, FontImage.getMaterialDesignFont(), 0xff, size, size), true);
        if (Picture.get() != null) {
            String[] fragments = StringUtils.split(this.Picture.get(), "/");
            String storageName = fragments[fragments.length - 1];
            URLImage photo = URLImage.createToStorage(placeholder, storageName, Picture.get());
            return photo;
        } else return placeholder;
    }
}