/*
 * Copyright (c) 2016, Codename One
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.codename1.uikit.pheonixui;

import com.codename1.components.FloatingActionButton;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Rectangle;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;

import java.util.List;

/**
 * GUI builder created Form
 *
 * @author shai
 */
public class InboxForm extends BaseForm {

    public InboxForm() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }

    @Override
    protected boolean isCurrentInbox() {
        return true;
    }


    public InboxForm(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);

        getToolbar().setTitleComponent(
                FlowLayout.encloseCenterMiddle(
                        new Label("Inbox", "Title"),
                        new Label("19", "InboxNumber")
                )
        );

        installSidemenu(resourceObjectInstance);


        gui_Label_5.setShowEvenIfBlank(true);
        gui_Label_6.setShowEvenIfBlank(true);
        gui_Label_7.setShowEvenIfBlank(true);
        gui_Label_8.setShowEvenIfBlank(true);
        gui_Label_9.setShowEvenIfBlank(true);

        gui_Text_Area_1.setRows(2);
        gui_Text_Area_1.setColumns(100);
        gui_Text_Area_1.setEditable(false);
        gui_Text_Area_1_1.setRows(2);
        gui_Text_Area_1_1.setColumns(100);
        gui_Text_Area_1_1.setEditable(false);
        gui_Text_Area_1_2.setRows(2);
        gui_Text_Area_1_2.setColumns(100);
        gui_Text_Area_1_2.setEditable(false);
        gui_Text_Area_1_3.setRows(2);
        gui_Text_Area_1_3.setColumns(100);
        gui_Text_Area_1_3.setEditable(false);
        gui_Text_Area_1_4.setRows(2);
        gui_Text_Area_1_4.setColumns(100);
        gui_Text_Area_1_4.setEditable(false);

        FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        RoundBorder rb = (RoundBorder) fab.getUnselectedStyle().getBorder();
        rb.uiid(true);
        fab.bindFabToContainer(getContentPane());
        fab.addActionListener(e -> {
            fab.setUIID("FloatingActionButtonClose");
            Image oldImage = fab.getIcon();
            FontImage image = FontImage.createMaterial(FontImage.MATERIAL_CLOSE, "FloatingActionButton", 3.8f);
            fab.setIcon(image);
            Dialog popup = new Dialog();
            popup.setDialogUIID("Container");
            popup.setLayout(new LayeredLayout());

            Button c1 = new Button(resourceObjectInstance.getImage("contact-a.png"));
            Button c2 = new Button(resourceObjectInstance.getImage("contact-b.png"));
            Button c3 = new Button(resourceObjectInstance.getImage("contact-c.png"));
            Button trans = new Button(" ");
            trans.setUIID("Container");
            c1.setUIID("Container");
            c2.setUIID("Container");
            c3.setUIID("Container");
            Style c1s = c1.getAllStyles();
            Style c2s = c2.getAllStyles();
            Style c3s = c3.getAllStyles();

            c1s.setMarginUnit(Style.UNIT_TYPE_DIPS);
            c2s.setMarginUnit(Style.UNIT_TYPE_DIPS);
            c3s.setMarginUnit(Style.UNIT_TYPE_DIPS);

            c1s.setMarginBottom(16);
            c1s.setMarginLeft(12);
            c1s.setMarginRight(3);

            c2s.setMarginLeft(4);
            c2s.setMarginTop(5);
            c2s.setMarginBottom(10);
            c3s.setMarginRight(14);

            c3s.setMarginTop(12);
            c3s.setMarginRight(16);

            popup.add(trans).
                    add(FlowLayout.encloseIn(c1)).
                    add(FlowLayout.encloseIn(c2)).
                    add(FlowLayout.encloseIn(c3));

            ActionListener a = ee -> popup.dispose();

            trans.addActionListener(a);
            c1.addActionListener(a);
            c2.addActionListener(a);
            c3.addActionListener(a);

            popup.setTransitionInAnimator(CommonTransitions.createEmpty());
            popup.setTransitionOutAnimator(CommonTransitions.createEmpty());
            popup.setDisposeWhenPointerOutOfBounds(true);
            int t = InboxForm.this.getTintColor();
            InboxForm.this.setTintColor(0);
            popup.showPopupDialog(new Rectangle(InboxForm.this.getWidth() - 10, InboxForm.this.getHeight() - 10, 10, 10));
            InboxForm.this.setTintColor(t);
            fab.setUIID("FloatingActionButton");
            fab.setIcon(oldImage);
        });
    }

    ////-- DON'T EDIT BELOW THIS LINE!!!
    protected com.codename1.ui.Container gui_Container_1 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BorderLayout());
    protected com.codename1.ui.Container gui_Container_2 = new com.codename1.ui.Container(new com.codename1.ui.layouts.FlowLayout());
    protected com.codename1.ui.Label gui_Label_1 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_4 = new com.codename1.ui.Container(new com.codename1.ui.layouts.FlowLayout());
    protected com.codename1.ui.Label gui_Label_4 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_3 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BoxLayout(com.codename1.ui.layouts.BoxLayout.Y_AXIS));
    protected com.codename1.ui.Label gui_Label_3 = new com.codename1.ui.Label();
    protected com.codename1.ui.Label gui_Label_2 = new com.codename1.ui.Label();
    protected com.codename1.ui.TextArea gui_Text_Area_1 = new com.codename1.ui.TextArea();
    protected com.codename1.ui.Label gui_Label_6 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_1_1 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BorderLayout());
    protected com.codename1.ui.Container gui_Container_2_1 = new com.codename1.ui.Container(new com.codename1.ui.layouts.FlowLayout());
    protected com.codename1.ui.Label gui_Label_1_1 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_4_1 = new com.codename1.ui.Container(new com.codename1.ui.layouts.FlowLayout());
    protected com.codename1.ui.Label gui_Label_4_1 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_3_1 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BoxLayout(com.codename1.ui.layouts.BoxLayout.Y_AXIS));
    protected com.codename1.ui.Label gui_Label_3_1 = new com.codename1.ui.Label();
    protected com.codename1.ui.Label gui_Label_2_1 = new com.codename1.ui.Label();
    protected com.codename1.ui.TextArea gui_Text_Area_1_1 = new com.codename1.ui.TextArea();
    protected com.codename1.ui.Label gui_Label_7 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_1_2 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BorderLayout());
    protected com.codename1.ui.Container gui_Container_2_2 = new com.codename1.ui.Container(new com.codename1.ui.layouts.FlowLayout());
    protected com.codename1.ui.Label gui_Label_1_2 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_4_2 = new com.codename1.ui.Container(new com.codename1.ui.layouts.FlowLayout());
    protected com.codename1.ui.Label gui_Label_4_2 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_3_2 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BoxLayout(com.codename1.ui.layouts.BoxLayout.Y_AXIS));
    protected com.codename1.ui.Label gui_Label_3_2 = new com.codename1.ui.Label();
    protected com.codename1.ui.Label gui_Label_2_2 = new com.codename1.ui.Label();
    protected com.codename1.ui.TextArea gui_Text_Area_1_2 = new com.codename1.ui.TextArea();
    protected com.codename1.ui.Label gui_Label_8 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_1_3 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BorderLayout());
    protected com.codename1.ui.Container gui_Container_2_3 = new com.codename1.ui.Container(new com.codename1.ui.layouts.FlowLayout());
    protected com.codename1.ui.Label gui_Label_1_3 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_4_3 = new com.codename1.ui.Container(new com.codename1.ui.layouts.FlowLayout());
    protected com.codename1.ui.Label gui_Label_4_3 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_3_3 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BoxLayout(com.codename1.ui.layouts.BoxLayout.Y_AXIS));
    protected com.codename1.ui.Label gui_Label_3_3 = new com.codename1.ui.Label();
    protected com.codename1.ui.Label gui_Label_2_3 = new com.codename1.ui.Label();
    protected com.codename1.ui.TextArea gui_Text_Area_1_3 = new com.codename1.ui.TextArea();
    protected com.codename1.ui.Label gui_Label_9 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_1_4 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BorderLayout());
    protected com.codename1.ui.Container gui_Container_2_4 = new com.codename1.ui.Container(new com.codename1.ui.layouts.FlowLayout());
    protected com.codename1.ui.Label gui_Label_1_4 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_4_4 = new com.codename1.ui.Container(new com.codename1.ui.layouts.FlowLayout());
    protected com.codename1.ui.Label gui_Label_4_4 = new com.codename1.ui.Label();
    protected com.codename1.ui.Container gui_Container_3_4 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BoxLayout(com.codename1.ui.layouts.BoxLayout.Y_AXIS));
    protected com.codename1.ui.Label gui_Label_3_4 = new com.codename1.ui.Label();
    protected com.codename1.ui.Label gui_Label_2_4 = new com.codename1.ui.Label();
    protected com.codename1.ui.TextArea gui_Text_Area_1_4 = new com.codename1.ui.TextArea();
    protected com.codename1.ui.Label gui_Label_5 = new com.codename1.ui.Label();


    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.BoxLayout(com.codename1.ui.layouts.BoxLayout.Y_AXIS));
        setInlineStylesTheme(resourceObjectInstance);
        setInlineStylesTheme(resourceObjectInstance);
        setTitle("InboxForm");
        setName("InboxForm");
        gui_Container_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_1.setName("Container_1");
        gui_Label_6.setText("");
        gui_Label_6.setUIID("Separator");
        gui_Label_6.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_6.setName("Label_6");
        gui_Container_1_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_1_1.setName("Container_1_1");
        gui_Label_7.setText("");
        gui_Label_7.setUIID("Separator");
        gui_Label_7.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_7.setName("Label_7");
        gui_Container_1_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_1_2.setName("Container_1_2");
        gui_Label_8.setText("");
        gui_Label_8.setUIID("Separator");
        gui_Label_8.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_8.setName("Label_8");
        gui_Container_1_3.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_1_3.setName("Container_1_3");
        gui_Label_9.setText("");
        gui_Label_9.setUIID("Separator");
        gui_Label_9.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_9.setName("Label_9");
        gui_Container_1_4.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_1_4.setName("Container_1_4");
        gui_Label_5.setText("");
        gui_Label_5.setUIID("Separator");
        gui_Label_5.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_5.setName("Label_5");
        addComponent(gui_Container_1);
        gui_Container_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_2.setName("Container_2");
        gui_Container_4.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_4.setName("Container_4");
        ((com.codename1.ui.layouts.FlowLayout) gui_Container_4.getLayout()).setAlign(com.codename1.ui.Component.CENTER);
        gui_Container_3.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_3.setName("Container_3");
        gui_Container_1.addComponent(com.codename1.ui.layouts.BorderLayout.EAST, gui_Container_2);
        gui_Label_1.setText("11:31 AM");
        gui_Label_1.setUIID("SmallFontLabel");
        gui_Label_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_1.setName("Label_1");
        gui_Container_2.addComponent(gui_Label_1);
        gui_Container_1.addComponent(com.codename1.ui.layouts.BorderLayout.WEST, gui_Container_4);
        gui_Label_4.setUIID("Padding2");
        gui_Label_4.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_4.setName("Label_4");
        gui_Label_4.setIcon(resourceObjectInstance.getImage("label_round.png"));
        gui_Container_4.addComponent(gui_Label_4);
        gui_Container_1.addComponent(com.codename1.ui.layouts.BorderLayout.CENTER, gui_Container_3);
        gui_Label_3.setText("Sheldon Murphy");
        gui_Label_3.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_3.setName("Label_3");
        gui_Label_2.setText("Design Updates");
        gui_Label_2.setUIID("RedLabel");
        gui_Label_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_2.setName("Label_2");
        gui_Text_Area_1.setText("Hi Adrian, there is a new announcement for you from Oxford  Learning  Lab. Hello we completly...");
        gui_Text_Area_1.setUIID("SmallFontLabel");
        gui_Text_Area_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Text_Area_1.setName("Text_Area_1");
        gui_Text_Area_1.setColumns(100);
        gui_Text_Area_1.setRows(2);
        gui_Container_3.addComponent(gui_Label_3);
        gui_Container_3.addComponent(gui_Label_2);
        gui_Container_3.addComponent(gui_Text_Area_1);
        addComponent(gui_Label_6);
        addComponent(gui_Container_1_1);
        gui_Container_2_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_2_1.setName("Container_2_1");
        gui_Container_4_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_4_1.setName("Container_4_1");
        ((com.codename1.ui.layouts.FlowLayout) gui_Container_4_1.getLayout()).setAlign(com.codename1.ui.Component.CENTER);
        gui_Container_3_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_3_1.setName("Container_3_1");
        gui_Container_1_1.addComponent(com.codename1.ui.layouts.BorderLayout.EAST, gui_Container_2_1);
        gui_Label_1_1.setText("8:23 PM");
        gui_Label_1_1.setUIID("SmallFontLabel");
        gui_Label_1_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_1_1.setName("Label_1_1");
        gui_Container_2_1.addComponent(gui_Label_1_1);
        gui_Container_1_1.addComponent(com.codename1.ui.layouts.BorderLayout.WEST, gui_Container_4_1);
        gui_Label_4_1.setUIID("Padding2");
        gui_Label_4_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_4_1.setName("Label_4_1");
        gui_Label_4_1.setIcon(resourceObjectInstance.getImage("label_round-selected.png"));
        gui_Container_4_1.addComponent(gui_Label_4_1);
        gui_Container_1_1.addComponent(com.codename1.ui.layouts.BorderLayout.CENTER, gui_Container_3_1);
        gui_Label_3_1.setText("Massdrop");
        gui_Label_3_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_3_1.setName("Label_3_1");
        gui_Label_2_1.setText("We Are Just Getting Started");
        gui_Label_2_1.setUIID("RedLabel");
        gui_Label_2_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_2_1.setName("Label_2_1");
        gui_Text_Area_1_1.setText("Tenkara Rod Co Teton Package Made possible by the Ultralight community...");
        gui_Text_Area_1_1.setUIID("SmallFontLabel");
        gui_Text_Area_1_1.setInlineStylesTheme(resourceObjectInstance);
        gui_Text_Area_1_1.setName("Text_Area_1_1");
        gui_Text_Area_1_1.setColumns(100);
        gui_Text_Area_1_1.setRows(2);
        gui_Container_3_1.addComponent(gui_Label_3_1);
        gui_Container_3_1.addComponent(gui_Label_2_1);
        gui_Container_3_1.addComponent(gui_Text_Area_1_1);
        addComponent(gui_Label_7);
        addComponent(gui_Container_1_2);
        gui_Container_2_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_2_2.setName("Container_2_2");
        gui_Container_4_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_4_2.setName("Container_4_2");
        ((com.codename1.ui.layouts.FlowLayout) gui_Container_4_2.getLayout()).setAlign(com.codename1.ui.Component.CENTER);
        gui_Container_3_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_3_2.setName("Container_3_2");
        gui_Container_1_2.addComponent(com.codename1.ui.layouts.BorderLayout.EAST, gui_Container_2_2);
        gui_Label_1_2.setText("Yesterday");
        gui_Label_1_2.setUIID("SmallFontLabel");
        gui_Label_1_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_1_2.setName("Label_1_2");
        gui_Container_2_2.addComponent(gui_Label_1_2);
        gui_Container_1_2.addComponent(com.codename1.ui.layouts.BorderLayout.WEST, gui_Container_4_2);
        gui_Label_4_2.setUIID("Padding2");
        gui_Label_4_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_4_2.setName("Label_4_2");
        gui_Label_4_2.setIcon(resourceObjectInstance.getImage("label_round.png"));
        gui_Container_4_2.addComponent(gui_Label_4_2);
        gui_Container_1_2.addComponent(com.codename1.ui.layouts.BorderLayout.CENTER, gui_Container_3_2);
        gui_Label_3_2.setText("Product Hunt");
        gui_Label_3_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_3_2.setName("Label_3_2");
        gui_Label_2_2.setText("Our favorite GIF apps");
        gui_Label_2_2.setUIID("RedLabel");
        gui_Label_2_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_2_2.setName("Label_2_2");
        gui_Text_Area_1_2.setText("We know that you spend a lot of time admiring the hard work of GIF-makers the world over. ");
        gui_Text_Area_1_2.setUIID("SmallFontLabel");
        gui_Text_Area_1_2.setInlineStylesTheme(resourceObjectInstance);
        gui_Text_Area_1_2.setName("Text_Area_1_2");
        gui_Text_Area_1_2.setColumns(100);
        gui_Text_Area_1_2.setRows(2);
        gui_Container_3_2.addComponent(gui_Label_3_2);
        gui_Container_3_2.addComponent(gui_Label_2_2);
        gui_Container_3_2.addComponent(gui_Text_Area_1_2);
        addComponent(gui_Label_8);
        addComponent(gui_Container_1_3);
        gui_Container_2_3.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_2_3.setName("Container_2_3");
        gui_Container_4_3.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_4_3.setName("Container_4_3");
        ((com.codename1.ui.layouts.FlowLayout) gui_Container_4_3.getLayout()).setAlign(com.codename1.ui.Component.CENTER);
        gui_Container_3_3.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_3_3.setName("Container_3_3");
        gui_Container_1_3.addComponent(com.codename1.ui.layouts.BorderLayout.EAST, gui_Container_2_3);
        gui_Label_1_3.setText("Mar 12");
        gui_Label_1_3.setUIID("SmallFontLabel");
        gui_Label_1_3.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_1_3.setName("Label_1_3");
        gui_Container_2_3.addComponent(gui_Label_1_3);
        gui_Container_1_3.addComponent(com.codename1.ui.layouts.BorderLayout.WEST, gui_Container_4_3);
        gui_Label_4_3.setUIID("Padding2");
        gui_Label_4_3.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_4_3.setName("Label_4_3");
        gui_Label_4_3.setIcon(resourceObjectInstance.getImage("label_round.png"));
        gui_Container_4_3.addComponent(gui_Label_4_3);
        gui_Container_1_3.addComponent(com.codename1.ui.layouts.BorderLayout.CENTER, gui_Container_3_3);
        gui_Label_3_3.setText("MightyDeals");
        gui_Label_3_3.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_3_3.setName("Label_3_3");
        gui_Label_2_3.setText("Vintage Design: 600+ Retro Vector Illustrations and Objects");
        gui_Label_2_3.setUIID("RedLabel");
        gui_Label_2_3.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_2_3.setName("Label_2_3");
        gui_Text_Area_1_3.setText("With just a little imagery, an ordinary project can transform into something extraordinary! ");
        gui_Text_Area_1_3.setUIID("SmallFontLabel");
        gui_Text_Area_1_3.setInlineStylesTheme(resourceObjectInstance);
        gui_Text_Area_1_3.setName("Text_Area_1_3");
        gui_Text_Area_1_3.setColumns(100);
        gui_Text_Area_1_3.setRows(2);
        gui_Container_3_3.addComponent(gui_Label_3_3);
        gui_Container_3_3.addComponent(gui_Label_2_3);
        gui_Container_3_3.addComponent(gui_Text_Area_1_3);
        addComponent(gui_Label_9);
        addComponent(gui_Container_1_4);
        gui_Container_2_4.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_2_4.setName("Container_2_4");
        gui_Container_4_4.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_4_4.setName("Container_4_4");
        ((com.codename1.ui.layouts.FlowLayout) gui_Container_4_4.getLayout()).setAlign(com.codename1.ui.Component.CENTER);
        gui_Container_3_4.setInlineStylesTheme(resourceObjectInstance);
        gui_Container_3_4.setName("Container_3_4");
        gui_Container_1_4.addComponent(com.codename1.ui.layouts.BorderLayout.EAST, gui_Container_2_4);
        gui_Label_1_4.setText("Mar 08");
        gui_Label_1_4.setUIID("SmallFontLabel");
        gui_Label_1_4.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_1_4.setName("Label_1_4");
        gui_Container_2_4.addComponent(gui_Label_1_4);
        gui_Container_1_4.addComponent(com.codename1.ui.layouts.BorderLayout.WEST, gui_Container_4_4);
        gui_Label_4_4.setUIID("Padding2");
        gui_Label_4_4.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_4_4.setName("Label_4_4");
        gui_Label_4_4.setIcon(resourceObjectInstance.getImage("label_round.png"));
        gui_Container_4_4.addComponent(gui_Label_4_4);
        gui_Container_1_4.addComponent(com.codename1.ui.layouts.BorderLayout.CENTER, gui_Container_3_4);
        gui_Label_3_4.setText("Twitter");
        gui_Label_3_4.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_3_4.setName("Label_3_4");
        gui_Label_2_4.setText("Popular tweets this week");
        gui_Label_2_4.setUIID("RedLabel");
        gui_Label_2_4.setInlineStylesTheme(resourceObjectInstance);
        gui_Label_2_4.setName("Label_2_4");
        gui_Text_Area_1_4.setText("Hi Adrian, there is a new announcement for you from Oxford Learning  Lab. Hello we completly...");
        gui_Text_Area_1_4.setUIID("SmallFontLabel");
        gui_Text_Area_1_4.setInlineStylesTheme(resourceObjectInstance);
        gui_Text_Area_1_4.setName("Text_Area_1_4");
        gui_Text_Area_1_4.setColumns(100);
        gui_Text_Area_1_4.setRows(2);
        gui_Container_3_4.addComponent(gui_Label_3_4);
        gui_Container_3_4.addComponent(gui_Label_2_4);
        gui_Container_3_4.addComponent(gui_Text_Area_1_4);
        addComponent(gui_Label_5);
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
