module edu.esprit {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires dotenv.java;
    requires javafaker;
    requires persistence.api;
    requires reflect;
    requires java.management;
    requires commons.beanutils;
    requires commons.lang;
    requires commons.lang3;
    requires commons.logging;
    requires commons.collections;
    requires io.pebbletemplates;
    requires javafx.base;
    requires datafx;
    requires com.dooapp.fxform;
    requires java.validation;
    requires libphonenumber;
    requires classindex;
    requires itextpdf;
    requires java.desktop;
    exports edu.esprit;
    exports edu.esprit.gui;
    exports edu.esprit.controller;
    exports edu.esprit.DAO;
    exports edu.esprit.entities;
    exports edu.esprit.enums;
    exports edu.esprit.services;
    exports edu.esprit.lib.persistence.fx;
    exports edu.esprit.lib.persistence;
    exports edu.esprit.controller.Template;

    opens edu.esprit.lib.fx.fxform2.extensions to com.dooapp.fxform;
    opens edu.esprit.lib.fx.fxform2.view.property to com.dooapp.fxform;
    opens edu.esprit.lib.fx.validation.constraint to org.hibernate.validator;
    opens edu.esprit.lib.fx.validation to org.hibernate.validator;
    opens edu.esprit.controller to javafx.fxml;
    opens edu.esprit.lib.controller to javafx.fxml;
    opens edu.esprit.controller.Template to javafx.fxml;
    opens edu.esprit.entities to reflect, com.dooapp.fxform, org.hibernate.validator;
    opens edu.esprit.lib.persistence to reflect, com.dooapp.fxform;








    exports edu.esprit.controller.Template.feed.Admin ;

    exports edu.esprit.gui.Feed.Admin;

    exports edu.esprit.entities.feed ;

    exports edu.esprit.gui.Feed.Map;
    exports edu.esprit.gui.Feed.Map.Map2;

    exports edu.esprit.gui.Feed.User;
    exports edu.esprit.controller.Template.feed.User;


}