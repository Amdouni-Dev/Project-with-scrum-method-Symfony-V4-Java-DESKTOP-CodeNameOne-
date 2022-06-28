package edu.esprit.controller;

import edu.esprit.DAO.Chat.ChatCrud;
import edu.esprit.DAO.UserDAO;
import edu.esprit.entities.Channel;
import edu.esprit.entities.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddConver implements Initializable {

    @FXML
    private Button AddingButton;

    @FXML
    private TextField AddingTextField;

    @FXML
    private VBox ChannelContainer;

    public void addConversation() {
        Channel Channeltoadd = new Channel(0);
        List<User> UsersToAdd = new ArrayList<>();
        ChatCrud cc = new ChatCrud();
        User CurrentUser = new User();
        User u1 = new User();
        UserDAO d1 = new UserDAO();
        List<User> AllUsers = new ArrayList<>();
        AllUsers = d1.findAll();
        CurrentUser = d1.findById(3312);
        for (User user : AllUsers) {
            if (user.getEmail().equals(AddingTextField.getText())) {
                u1 = user;
                break;
            }

        }
        UsersToAdd.add(u1);
        UsersToAdd.add(CurrentUser);
        Channeltoadd.setParticipants(FXCollections.observableArrayList(UsersToAdd));
        if (AddingTextField.getText().contains("@") == false) {

            AddingTextField.setText("saisir une adresse mail valide");

        } else {
            cc.CreateChannel(Channeltoadd);
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AddingButton.setOnMouseClicked(mouseEvent -> {
            addConversation();
        });
    }

}
