package edu.esprit.controller.Template.feed.Admin;



import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import edu.esprit.DAO.feed.PostCrud;
import edu.esprit.entities.User;
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


import javax.xml.transform.Result;
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


public class GererPostsController implements Initializable {

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
    public TableColumn colLong;
    public TableColumn colLati;
  //  public TableColumn colValid;

    public TableColumn colDeleted;
    public Button btnAjouter;
    public Button btnUpdate;
    public Button btnValid;
    public Button btnDelete;
    public Button btnloc;
    public Button btnCharger;
    public TextField tfRechercherPost;
    public TextField txtimage;
public TextArea txtContent;
    String latitude;
    String longitude ;
    private Label label;
    @FXML
    public WebView webmap;
    public WebEngine webengine;

    //stuff and stuff
 public AnchorPane AnchorPane_Publication;
    public ImageView imageview_Publication;


    public void handleButtonaction(ActionEvent event) throws Exception {
        PostCrud pc = new PostCrud();
        Post p = new Post();
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
            alert.setContentText("Voulez vous approuver cette Publication ?");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.OK) {
          //  GoogleApp g = new GoogleApp();
            //g.start(new Stage());

            ObservableList<Post> list = afficherLesPosts();
            for (int i=0; i<list.size();i++) {
                if (Integer.parseInt(tfId.getText()) == list.get(i).getId()) {
                    pc.ChangerValidité(list.get(i));
                    Image img1=new Image("/check.png");
                    ImageView iv=new ImageView(img1);
                    iv.setFitHeight(100);
                    iv.setFitWidth(100);
                    Notifications notifSupp=Notifications.create().
                            title("Post bien approuvé")
                            .text("cette publication est approuvée ")

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

        if (event.getSource() == btnAjouter) {


            latitude = webmap.getEngine().executeScript("lat").toString();
            longitude = webmap.getEngine().executeScript("lon").toString();
            System.out.println("Lat :****************** " + latitude);
            System.out.println("LOn ************************" + longitude);


            p.setId(Integer.parseInt(tfid.getText()));
            p.setTitle(tfTitle.getText());
            p.setContent(tfContent.getText());
            p.setUserId(1);
            p.setLongitude(longitude);
            p.setLatitude(latitude);
            pc.ajouterPost(p);

        }
      if (event.getSource() == btnUpdate) {
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("Confirmation Message");
          alert.setHeaderText(null);
          alert.setContentText("Voulez vous Modifier cette Publication ?");
          Optional<ButtonType> buttonType = alert.showAndWait();
          if (buttonType.get() == ButtonType.OK) {
              //  GoogleApp g = new GoogleApp();
              //g.start(new Stage());

              ObservableList<Post> list = afficherLesPosts();
              for (int i=0; i<list.size();i++) {
                  if (Integer.parseInt(tfId.getText()) == list.get(i).getId()) {
                      list.get(i).setTitle(tfTitle.getText());
                      list.get(i).setContent(txtContent.getText());
                      pc.updatePost (list.get(i),list.get(i).getId());
                      Image img1=new Image("/check.png");
                      ImageView iv=new ImageView(img1);
                      iv.setFitHeight(100);
                      iv.setFitWidth(100);
                      Notifications notifSupp=Notifications.create().
                              title("Post bien modifiée")
                              .text("cette publication est modifiée ")

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






        else if (event.getSource() == btnDelete) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Voulez vous Supprimer cette Publication ?");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.OK) {
                //  GoogleApp g = new GoogleApp();
                //g.start(new Stage());

                ObservableList<Post> list = afficherLesPosts();
                for (int i=0; i<list.size();i++) {
                    if (Integer.parseInt(tfId.getText()) == list.get(i).getId()) {
                        pc.SupprimerPost(list.get(i));
                        Image img1=new Image("/check.png");
                        ImageView iv=new ImageView(img1);
                        iv.setFitHeight(100);
                        iv.setFitWidth(100);
                        Notifications notifSupp=Notifications.create().
                                title("Post bien supprimé")
                                .text("cette publication est supprimée ")

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

    public ObservableList<Post> afficherLesPosts(){

        ObservableList<Post> postsList= FXCollections.observableArrayList();
        String sql="select id,title,content,user_id,longitude , latitude , is_valid, is_deleted, created_at from post order by created_at Desc ";
        Statement st;
        ResultSet rs;
        cnx2= DataSource2.getInstance().getCnx();
        try{

            st=cnx2.createStatement();
            rs=st.executeQuery(sql);

            while (rs.next()){
                Post post1=new Post();
                post1.setId(rs.getInt("id"));
                post1.setTitle(rs.getString("title"));
                post1.setContent(rs.getString("content"));
                post1.setUserId(rs.getInt("user_id"));

                post1.setLongitude(rs.getString("longitude"));
                post1.setLatitude(rs.getString("latitude"));
                post1.setValid(rs.getBoolean("is_valid"));

                post1.setDeleted(rs.getBoolean("is_deleted"));
                Date currentDate =rs.getDate("created_at");
                LocalDateTime localDateTime = Instant
                        .ofEpochMilli(currentDate.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                System.out.println("Locale date time is :" + localDateTime);

                post1.setCeatedAt(localDateTime);



                postsList.add(post1);
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














        ObservableList<Post> list=afficherLesPosts();

        colID.setCellValueFactory(new PropertyValueFactory<Post, Integer>("id"));

        colTitle.setCellValueFactory(new PropertyValueFactory<Post, String>("title"));
        colContent.setCellValueFactory(new PropertyValueFactory<Post, String>("content"));
        colUSer.setCellValueFactory(new PropertyValueFactory<Post, String>("userId"));
        colLong.setCellValueFactory(new PropertyValueFactory<Post, String>("longitude"));
        colLati.setCellValueFactory(new PropertyValueFactory<Post, String>("latitude"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("ceatedAt"));


      //  colValid.setCellValueFactory(new PropertyValueFactory<Post,Integer>("isValid"));
      //  colValid.setCellValueFactory(new PropertyValueFactory<>("isValid"));
        //colDeleted.setCellValueFactory(new PropertyValueFactory<Post ,Integer>("isDeleted"));








        tvPosts.setItems(list);

        FilteredList<Post> filteredData = new FilteredList<>(list, b -> true);
        tfRechercherPost.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(post -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (post.getContent().toLowerCase().contains(lowerCaseFilter) || post.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                    /* } else if (publication.getDesc_Pub().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } */
                } else {
                    return false;
                }
            });
        });

        SortedList<Post> sortedData = new SortedList<>(filteredData);
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
    Post p= (Post) tvPosts.getSelectionModel().getSelectedItem();
   tfId.setText(Integer.toString(p.getId()));
   tfId.setEditable(false);
   tfTitle.setText(p.getTitle());
   txtContent.setText(p.getContent());
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


    @FXML
    public void PostToPDF(ActionEvent event) {
        try {
            Post post = (Post) tvPosts.getSelectionModel().getSelectedItem();
            Document document = new Document();


            PdfWriter.getInstance(document, new FileOutputStream("C:/Users/mouna/Downloads/Post.pdf"));
            document.open();

            document.open();
            String ch=null;
if(post.getValid()== true && post.getDeleted()== false){

    ch="publication valide et non supprimeé";
}
else {

    ch="publication non valide";

}
            Paragraph para = new Paragraph("Post numero : "+post.getId() +" \n"

                    +"Crée le: "+post.getCeatedAt().toLocalDate()+"\n"

                    +"etat: "+ch+"\n"
            +"sous titre: "+ post.getTitle() +"\n"
                    +"   "+ post.getContent()
            );
para.setAlignment(12);
para.setExtraParagraphSpace(12);

            document.add(para);

            //simple paragraph
            //add table
           /* PdfPTable pdfPTable = new PdfPTable(4);

            // PdfPCell pdfCell1 = new PdfPCell(new Phrase("idPub"));

            PdfPCell pdfCell2 = new PdfPCell(new Phrase("title"));
            PdfPCell pdfCell3 = new PdfPCell(new Phrase("content"));


            // pdfPTable.addCell(pdfCell1);
            pdfPTable.addCell(pdfCell2);
            pdfPTable.addCell(pdfCell3);


            // pdfPTable.addCell("" + reclamation.getIdReclamation() + "");
            pdfPTable.addCell("" + post.getTitle() + "");
            pdfPTable.addCell("" + post.getContent() + "");


            document.add(pdfPTable);*/
            // document.add(image);
            document.close();

            String pathpdf = "C:/Users/mouna/Downloads/Post.pdf";
            String[] params = {"cmd", "/c", pathpdf};
            try {
                System.out.println("ouiiiiiiii");
                Runtime.getRuntime().exec(params);
            } catch (Exception e) {
                System.out.println("noooooooo");
                System.out.println(e);
            }
        } catch (DocumentException | FileNotFoundException Exception) {
            System.out.println(Exception);
        }
    }
}
