package edu.esprit.controller.Template.feed.Admin;



import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import edu.esprit.DAO.feed.GroupPostCrud;
import edu.esprit.DAO.feed.PostCrud;
import edu.esprit.entities.User;
import edu.esprit.entities.feed.GroupPost;
import edu.esprit.entities.feed.Post;
import edu.esprit.gui.Feed.Map.Map2.GoogleApp;
import edu.esprit.utils.DataSource2;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.controlsfx.control.Notifications;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;


public class GererGroupeController implements Initializable {

    public TextField tfid;
    public TextField tfTitle;

    public TextField tfDelete;
    public TextField tfContent;
    public TextField tfValid;
    public TextField tfId;
    public TextField tfUser;
    public TextField tfLatitude;
    public TextField tfLongitude;
    public TableView tvPosts;
    public TableColumn colID;
    public TableColumn colTitle;
    public TableColumn colContent;
    public TableColumn colDate;

    public TableColumn colUSer;



    public TableColumn colDeleted;
    public Button btnAjouter;
    public Button btnUpdate;
    public Button btnValid;
    public Button btnDelete;

    public Button btnCharger;
    public TextField tfRechercherPost;
    public TextField txtimage;
    public TextArea txtContent;

    private Label label;
    @FXML
    public WebView webmap;
    public WebEngine webengine;

    //stuff and stuff
    public AnchorPane AnchorPane_Publication;
    public ImageView imageview_Publication;


    public void handleButtonaction(ActionEvent event) throws Exception {
        GroupPostCrud pc=new GroupPostCrud();
     GroupPost p=new GroupPost();
        System.out.println("you clicked Me");









        if (event.getSource() == btnCharger) {
            System.out.println("*************");

            FileChooser open = new FileChooser();
            Stage stage = (Stage) AnchorPane_Publication.getScene().getWindow();
            File file = open.showOpenDialog(stage);
            if (file != null) {

                String path = file.getAbsolutePath();

                path = path.replace("\\", "\\\\");

                txtimage.setText(path);

                Image image = new Image(file.toURI().toString(), 110, 110, false, true);

                imageview_Publication.setImage(image);

            } else {

                System.out.println("NO DATA EXIST!");

            }

        }

        if (event.getSource() == btnValid) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Voulez vous approuver ce groupe ?");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.OK) {
                //  GoogleApp g = new GoogleApp();
                //g.start(new Stage());

                ObservableList<GroupPost> list = afficherLesPosts();
                for (int i=0; i<list.size();i++) {
                    if (Integer.parseInt(tfId.getText()) == list.get(i).getId()) {
                        pc.ChangerValiditéGroup(list.get(i));
                        Image img1=new Image("/check.png");
                        ImageView iv=new ImageView(img1);
                        iv.setFitHeight(100);
                        iv.setFitWidth(100);
                        Notifications notifSupp=Notifications.create().
                                title("Groupe bien approuvé")
                                .text("ce groupe est approuvé ")

                                .graphic(iv)
                                .hideAfter(Duration.seconds(5))
                                .position(Pos.TOP_RIGHT).onAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        System.out.println("notifff");
                                    }
                                });
                        notifSupp.darkStyle();
                        notifSupp.show();


                    }


                }
            }
        }


        else if (event.getSource() == btnUpdate) {
            GroupPost g=new GroupPost();
            //  p1.setId(Integer.parseInt(tfid.getText()));
            g.setNomGroupe(tfTitle.getText());
            g.setBut(txtContent.getText());
            Notifications notifSupp=Notifications.create().
                    title("vous ne pouvez pas changer le groupe")
                    .text("noooooooooooo ")

                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_RIGHT).onAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            System.out.println("notifff");
                        }
                    });
            notifSupp.darkStyle();
            notifSupp.show();
            //    p1.setUserId(1);
            //    p1.setLongitude(tfLongitude.getText());
            //     p1.setLatitude(tfLatitude.getText());

        }


        else if (event.getSource() == btnDelete) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Voulez vous supprimer ce groupe ?");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.OK) {
                //  GoogleApp g = new GoogleApp();
                //g.start(new Stage());

                ObservableList<GroupPost> list = afficherLesPosts();
                for (int i=0; i<list.size();i++) {
                    if (Integer.parseInt(tfId.getText()) == list.get(i).getId()) {
                        pc.SupprimerGroupPost(list.get(i));
                        Image img1=new Image("/check.png");
                        ImageView iv=new ImageView(img1);
                        iv.setFitHeight(100);
                        iv.setFitWidth(100);
                        Notifications notifSupp=Notifications.create().
                                title("Groupe bien supprimé")
                                .text("ce groupe est supprimé ")

                                .graphic(iv)
                                .hideAfter(Duration.seconds(5))
                                .position(Pos.TOP_RIGHT).onAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        System.out.println("notifff");
                                    }
                                });
                        notifSupp.darkStyle();
                        notifSupp.show();


                    }


                }
            }
        }



        else {

            return;

        }
    }

    Connection cnx2;

    public ObservableList<GroupPost> afficherLesPosts(){

        ObservableList<GroupPost> postsList= FXCollections.observableArrayList();
        String sql="select id,nom_groupe,but ,user_id, is_valid, is_deleted, created_at  from group_post";
        Statement st;
        ResultSet rs;
        cnx2= DataSource2.getInstance().getCnx();
        try{

            st=cnx2.createStatement();
            rs=st.executeQuery(sql);

            while (rs.next()){
          GroupPost g=new GroupPost();
                g.setId(rs.getInt("id"));
                g.setNomGroupe(rs.getString("nom_groupe"));
                g.setBut(rs.getString("but"));
                g.setUserId(rs.getInt("user_id"));

                g.setValid(rs.getBoolean("is_valid"));

                g.setDeleted(rs.getBoolean("is_deleted"));
                Date currentDate =rs.getDate("created_at");
                LocalDateTime localDateTime = Instant
                        .ofEpochMilli(currentDate.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                System.out.println("Locale date time is :" + localDateTime);

                g.setCreatedat(localDateTime);



                postsList.add(g);
                //   System.out.println(post1.getId());



            }
        }catch(SQLException e){

            e.printStackTrace();
        }

        return  postsList;

    }

    public void allposts(){
        cnx2= DataSource2.getInstance().getCnx();
        List<String> list1 = new ArrayList<String>();
        String firstName="";
        try {
            String req = "SELECT * FROM user where id=1 ";
            PreparedStatement pst = cnx2.prepareStatement(req);

            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                firstName=rs.getString("first_name");
                list1.add(firstName);
                System.out.println(firstName);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        for(int i=0; i<list1.size(); i++){

            System.out.println(list1.get(i));

        }














        ObservableList<GroupPost> list=afficherLesPosts();

        colID.setCellValueFactory(new PropertyValueFactory<Post, Integer>("id"));

        colTitle.setCellValueFactory(new PropertyValueFactory<Post, String>("nomGroupe"));
        colContent.setCellValueFactory(new PropertyValueFactory<Post, String>("but"));
        colUSer.setCellValueFactory(new PropertyValueFactory<Post, String>("userId"));

        colDate.setCellValueFactory(new PropertyValueFactory<>("createdat"));


        //  colValid.setCellValueFactory(new PropertyValueFactory<Post,Integer>("isValid"));
        //  colValid.setCellValueFactory(new PropertyValueFactory<>("isValid"));
        //colDeleted.setCellValueFactory(new PropertyValueFactory<Post ,Integer>("isDeleted"));

        tvPosts.setItems(list);

        FilteredList<GroupPost> filteredData = new FilteredList<>(list, b -> true);
        tfRechercherPost.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(groupPost -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (groupPost.getNomGroupe().toLowerCase().contains(lowerCaseFilter) || groupPost.getBut().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                    /* } else if (publication.getDesc_Pub().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } */
                } else {
                    return false;
                }
            });
        });

        SortedList<GroupPost> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tvPosts.comparatorProperty());
        tvPosts.setItems(sortedData);


    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {

        // webengine = webmap.getEngine();

        // url = this.getClass().getResource("/Template/Feed/map2/map/index.html");
        //  webengine.load(url.toString());


    }

    public void handleMouseAction(MouseEvent event) {
        GroupPost p= (GroupPost) tvPosts.getSelectionModel().getSelectedItem();
        tfId.setText(Integer.toString(p.getId()));
        tfId.setEditable(false);
        tfTitle.setText(p.getNomGroupe());
        txtContent.setText(p.getBut());
        // System.out.println("ttttttttttttttt"+p.getValid());
        if(p.getValid()==false) {
            tfValid.setText("non approuvé");
        }
        if(p.getValid()==true) {
            tfValid.setText("approuvé");
        }

        if(p.getDeleted()==false) {
            tfDelete.setText("non supprimé");
        }
        if(p.getDeleted()==true) {
            tfDelete.setText("supprimé");
        }
        tfDelete.setEditable(false);
        tfValid.setEditable(false);



        //  tfUser.setText(""+ p.getUserId());
        //  tfLongitude.setText(p.getLongitude());
//   tfLatitude.setText(p.getLatitude());


    }



    public void back(ActionEvent event) throws IOException {
        Parent page2 = FXMLLoader.load(getClass().getResource("/Template/Feed/Admin/Menu.fxml"));

        Scene scene2 = new Scene(page2);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene2);
        app_stage.show();
    }


}
