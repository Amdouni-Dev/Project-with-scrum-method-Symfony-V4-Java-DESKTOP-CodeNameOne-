package edu.esprit.controller.Template.feed.Admin;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mouna
 */
public class MenuController implements Initializable {


    @FXML
    public Button Button_GoToGPublication;
    @FXML
    public Button Button_GoToAjoutPost;


    public Stage stage;
    public Scene scene;
    public Parent root;

    public Button Button_GoToALLPostsValid;
    public Button Button_GoToGGroupes;
    public Button ButtonGoToStatPostsooo;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }


    @FXML
    public void GoToGPublication(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/Template/Feed/Admin/GererPosts.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    public void GoToGPost(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/Template/Feed/User/AjouterPost.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
        }
    }



    @FXML
    public void GoToVoirPostsUser(MouseEvent mouseEvent) {
        try {
            System.out.println("yyyyyyyyyyyyyyyyyy");
            root = FXMLLoader.load(getClass().getResource("/Template/Feed/User/AllPost.fxml"));
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
        }
    }
    @FXML
    public void GoToGroupes(MouseEvent mouseEvent) {
        try {
            root = FXMLLoader.load(getClass().getResource("/Template/Feed/Admin/GererGroupes.fxml"));
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
        }
    }




    @FXML
    public void GoToStatPostsoo(MouseEvent mouseEvent) {
        try {
            root = FXMLLoader.load(getClass().getResource("/Template/Feed/Admin/postLikesStat.fxml"));
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
        }
    }
}
