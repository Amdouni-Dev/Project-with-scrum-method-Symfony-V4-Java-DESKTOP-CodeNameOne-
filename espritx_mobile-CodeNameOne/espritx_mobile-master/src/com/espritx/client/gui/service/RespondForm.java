package com.espritx.client.gui.service;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.RadioButtonList;
import com.codename1.components.ScaleImageButton;
import com.codename1.ext.filechooser.FileChooser;
import com.codename1.io.Log;
import com.codename1.properties.InstantUI;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.Request;
import com.espritx.client.entities.Service;
import com.espritx.client.services.Service.RequestService;
import com.espritx.client.services.Service.ServiceService;

import java.util.List;

public class RespondForm extends BaseForm {
    private Request request;
    private com.espritx.client.services.Service.RequestService RequestService;

    public RespondForm() {
        this(Resources.getGlobalResources());
    }

    public RespondForm(Resources resourceObjectInstance) {
        this(Resources.getGlobalResources(), new Request());
    }

    public RespondForm(Resources resourceObjectInstance, Request instance) {
        setLayout(new BorderLayout());

        final String[] attachmentsArray = new String[1];
        // start upload
        Container pictureContainer = new Container(BoxLayout.x());
        instance.getEncodedPic(6);
        pictureContainer.add(instance.getEncodedPic(6));
        pictureContainer.setName("Picture Container");
        Label filePickerStatus = new Label("No file chosen.");
        pictureContainer.add(filePickerStatus);
        Image icon = FontImage.createMaterial(FontImage.MATERIAL_ADD, new Style());

        // select image
        ScaleImageButton addImageButton = new ScaleImageButton(icon);
        addImageButton.addActionListener((evt) -> {
            ActionListener callback = e -> {
                if (e != null && e.getSource() != null) {
                    attachmentsArray[0] = (String) e.getSource();
                    filePickerStatus.setText("Selected Picture");
                }
            };

            if (FileChooser.isAvailable()) {
                FileChooser.showOpenDialog(".png,image/png,.jpg,image/jpg,.jpeg", callback);
            } else {
                Display.getInstance().openGallery(callback, Display.GALLERY_IMAGE);
            }
        });
        pictureContainer.add(addImageButton);
        // end upload

        // construction
        this.RequestService = new RequestService();
        this.request = instance;
        instance.getPropertyIndex().registerExternalizable();
        setName("ChangeRequestForm");
        setInlineStylesTheme(resourceObjectInstance);
        InstantUI iui = new InstantUI();
        iui.excludeProperty(this.request.id);
        iui.excludeProperty(this.request.RespondedAt);
        iui.excludeProperty(this.request.CreatedAt);
        iui.excludeProperty(this.request.UpdatedAt);
        iui.excludeProperty(this.request.Picture);
        iui.excludeProperty(this.request.Attachement);
        iui.excludeProperty(this.request.Requester);
        iui.excludeProperty(this.request.Type);
        iui.setMultiChoiceLabels(this.request.Status, "Hold", "Deny", "Done");
        iui.setMultiChoiceValues(this.request.Status, "processing", "denied", "done");
        Container cnt = iui.createEditUI(request, true);
        cnt.setScrollableY(true);
        cnt.setInlineStylesTheme(resourceObjectInstance);

        List<Service> serviceList = ServiceService.getInstance().getAllServices();

        Label Type = new Label("Type (Reslect the same if there is no change)");

        RadioButtonList radioButtonList = new RadioButtonList(new DefaultListModel());
        radioButtonList.setLayout(BoxLayout.y());
        for (Service S : serviceList) {
            radioButtonList.getMultiListModel().addItem(S);
        }

        Button b = new Button(request.Type.toString());
        b.addActionListener(e -> {
            Dialog d = new Dialog();
            d.setLayout(BoxLayout.y());
            d.getContentPane().setScrollableY(true);
            d.add(radioButtonList);
            radioButtonList.addActionListener(ee -> {
                b.setText(radioButtonList.getModel().getItemAt(radioButtonList.getModel().getSelectedIndex()).toString());
                d.dispose();
            });
            d.showPopupDialog(b);
            d.removeComponent(radioButtonList);
        });

        cnt.addAll(Type, b, pictureContainer);

        addComponent(BorderLayout.CENTER, cnt);

        Button saveButton = makeButton("SaveButton", "Save");
        saveButton.addActionListener(evt -> {
            Dialog dlg = (new InfiniteProgress()).showInfiniteBlocking();
            try {
                Service TypeSer = (Service) radioButtonList.getModel().getItemAt(radioButtonList.getModel().getSelectedIndex());
                request.Type.set(TypeSer);
                this.RequestService.UpdateRequest(request, attachmentsArray[0], ""); // fix me
            } catch (Exception e) {
                Log.p(e.getMessage(), Log.ERROR);
                dlg.dispose();
                Dialog.show("error", e.getMessage(), "ok", null);
            }
            dlg.dispose();
            (new ShowRequestUserForm()).show();
        });
        addComponent(BorderLayout.SOUTH, saveButton);
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> showBack());
    }
}
