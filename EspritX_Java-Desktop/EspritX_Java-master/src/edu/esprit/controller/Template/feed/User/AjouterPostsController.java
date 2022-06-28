package edu.esprit.controller.Template.feed.User;

import edu.esprit.DAO.UserDAO;

import edu.esprit.DAO.feed.PostCrud;
import edu.esprit.entities.User;
import edu.esprit.entities.feed.Post;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.event.*;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.sql.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class AjouterPostsController implements Initializable {
    public AnchorPane AnchorPane_Publication_User;
    public TextField tfTitle;
    public Button btnAjouter;
    public WebView webmap;

    public Button btnCharger;
    public ImageView imageview_Publication;
    public TextArea tfContent;

    public WebEngine webengine;
    public Label file_path;
    public ProgressBar idPB;
    public Button idRetour;
    public Button Button_POSTToPDF;


    String latitude;
    String longitude ;
    User currentUser = new User();
    public void handleButtonaction(ActionEvent event) {
        PostCrud pc=new PostCrud();
        Post p=new Post();
         String pathI="";

        if(event.getSource()== btnAjouter){

            UserDAO UserDAO = new UserDAO();
            currentUser = UserDAO.findById(125);


            if (tfTitle.getText().isEmpty() || tfContent.getText().isEmpty()) {


                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs!");
                alert.showAndWait();
            }


            else if (tfTitle.getText().matches("^[0-9]+$") || tfContent.getText().matches("^[0-9]+$")  ) {
                Notifications notifAjout = Notifications.create().
                        title("Post!")
                        .text("veuillez entrez des caractereres ")

                        .graphic(null)
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.TOP_RIGHT).onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("notifff");
                            }
                        });
                notifAjout.darkStyle();
                notifAjout.show();


            }

            else {
idPB.setProgress(10);
                Image img1 = new Image("/check.png");
                ImageView iv = new ImageView(img1);
                iv.setFitHeight(100);
                iv.setFitWidth(100);

                latitude = webmap.getEngine().executeScript("lat").toString();
                longitude = webmap.getEngine().executeScript("lon").toString();


                System.out.println("Lat :****************** " + latitude);
                System.out.println("LOn ************************" + longitude);


                p.setTitle(tfTitle.getText());
                p.setContent(tfContent.getText());
                p.setUser(currentUser);
                System.out.println(currentUser.getFirstName());
                p.setImage(file_path.getText());
                System.out.println("oooooooo" + pathI);

                p.setLongitude(longitude);
                p.setLatitude(latitude);

                pc.ajouterPost(p);

                Notifications notifAjout = Notifications.create().
                        title("Post bien ajouté")
                        .text("Mais doit etre approuvé par notre admin")

                        .graphic(iv)
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.TOP_RIGHT).onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("notifff");
                            }
                        });
                notifAjout.darkStyle();
                notifAjout.show();



            }
        }


    }


    public void StartProgrssBar() {
        double ii = 0;

        do {

            ii += 0.001;
            idPB.setProgress(ii);
        } while (1 > (ii));

    }
    String[] titles={"hiii", "hellow" ,"how are you"};
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        webengine = webmap.getEngine();
        idPB.setProgress(0);
        url = this.getClass().getResource("/Template/Feed/map2/map/index.html");
        webengine.load(url.toString());

        // Auto complete

    //  TextFields.bindAutoCompletion(tfTitle,titles);




    }



    @FXML
    public void InsertImage() {

        FileChooser open = new FileChooser();

        Stage stage = (Stage) AnchorPane_Publication_User.getScene().getWindow();

        File file = open.showOpenDialog(stage);

        if (file != null) {

            String path = file.getAbsolutePath();

            path = path.replace("\\", "\\\\");

            file_path.setText(path);

            Image image = new Image(file.toURI().toString(), 110, 110, false, true);

            imageview_Publication.setImage(image);

        } else {

            System.out.println("NO DATA EXIST!");

        }
    }

    public void back(ActionEvent event) throws IOException {
        Parent page2 = FXMLLoader.load(getClass().getResource("/Template/Feed/Admin/Menu.fxml"));

        Scene scene2 = new Scene(page2);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene2);
        app_stage.show();
    }



}
