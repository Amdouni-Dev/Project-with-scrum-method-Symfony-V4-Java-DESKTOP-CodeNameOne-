package edu.esprit.controller.Template.feed.User;

import edu.esprit.DAO.UserDAO;
import edu.esprit.DAO.feed.CommentaireCrud;
import edu.esprit.DAO.feed.PostCrud;
import edu.esprit.DAO.feed.PostLikeCrud;
import edu.esprit.HelloApplication;
import edu.esprit.entities.User;
import edu.esprit.entities.feed.Commentaire;
import edu.esprit.entities.feed.Post;
import edu.esprit.entities.feed.PostLikes;
import edu.esprit.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static edu.esprit.controller.Template.feed.User.BadWords.badWordsFound;
import static edu.esprit.controller.Template.feed.User.BadWords.filterText;

public class PostController  implements Initializable {
    public Label LabTitleId;
    public Label TitleId;
    public Post p;
    public Label LabUserId;
    public TextArea contentId;
    public Label LabDate;
    public TextField tfCommentaire;
    public Button btnCommentaire;
    public WebView webmap;


    public WebEngine webengine;

    public String longitude;
    public String latitude;
    public Label nbComments;
    public ImageView likeImage;
    public Label nbLikes;
    public List<User> listUserId=new ArrayList<>();
    public List<User> postLikelistUserId=new ArrayList<>();
    public List<PostLikes> postLikelistId=new ArrayList<>();
    User currentUser = new User();
    public void setData(Post post){

        LabTitleId.setText(post.getTitle());

LabDate.setText(post.getCeatedAt().toString());


        contentId.setText(post.getContent());
        contentId.setEditable(false);

PostCrud pcd=new PostCrud();
int n=0;
n=pcd.getNbComments(post);

nbComments.setText( n+"");
int nL=0;
nL=pcd.getNbLikes(post);
nbLikes.setText(nL+"");


postLikelistId=afficherLesPostLikesId(post);


        btnCommentaire.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                BadWords.loadConfigs();




                if (tfCommentaire.getText().equals("")) {
                    Notifications notifAjout = Notifications.create().
                            title("Commentaire!")
                            .text("cher utilisateur veuillez remplir le champs commentaire ! ")

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
                } else if (tfCommentaire.getText().matches("^[0-9]+$")) {
                    Notifications notifAjout = Notifications.create().
                            title("Commentaire!")
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




                else if (BadWords.filterText(tfCommentaire.getText())) {
tfCommentaire.replaceText(0,tfCommentaire.getLength(),"*************");
System.out.println("leklem hedha leeeeeeeeeee c interdit");
tfCommentaire.setEditable(false);
                    Notifications notifAjout = Notifications.create().
                            title("notre application n'accepte pas ces termes !")
                            .text("cher utilisateur vous ne pouvez pas vous commenter ! \n merci de vous reconnecter ")

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
                else{
                    Image img1 = new Image("/check.png");
                    ImageView iv = new ImageView(img1);
                    iv.setFitHeight(100);
                    iv.setFitWidth(100);

                    CommentaireCrud cd=new CommentaireCrud();
                    Commentaire c=new Commentaire();
                    UserDAO UserDAO = new UserDAO();
                    currentUser = UserDAO.findById(125);
                    System.out.println("********************"+currentUser.getId());
                    c.setUser(currentUser);
                    c.setPost(post);

                c.setContent(  tfCommentaire.getText());

                    cd.ajouterComment(c);

                    Notifications notifAjout = Notifications.create().
                            title("Commntaire!")
                            .text("votre ommentaire est ajouté ! merci !")

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
        });
        likeImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                Image img1 = new Image("/Like.png");
                ImageView iv = new ImageView(img1);
                iv.setFitHeight(100);
                iv.setFitWidth(100);

                PostLikeCrud  cd=new PostLikeCrud();
                PostLikes pl=new PostLikes();
                UserDAO UserDAO = new UserDAO();
                currentUser = UserDAO.findById(126);

                pl.setUser(currentUser);
                pl.setPost(post);

System.out.println("ccccccccccccccccccc1"+currentUser.getId());


for(int i=0;i<postLikelistId.size();i++){



if(currentUser.getId() ==postLikelistId.get(i).getUserId()){
    Notifications notifAjout = Notifications.create().
            title("Liuke")
            .text("vous avez liké ce post !")

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
    break;
}

else{

    cd.ajouterLike(pl);
    Notifications notifAjout = Notifications.create().
            title("Like post")
            .text("Merci !!!")

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
    break;
}


}
if(pcd.getNbLikes(post) ==0){
    PostLikes postL=new PostLikes();
    postL.setPost(post);
    postL.setUser(currentUser);



    cd.ajouterLike(postL);
    Notifications notifAjout = Notifications.create().
            title("Like Post")
            .text("Merci !!!")

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

/*for (int i=0;i<listUserId.size();i++){


                        for (int i2 = 0; i2 < postLikelistId.size(); i2++) {
                            if (currentUser.getId()== listUserId.get(i).getId()

                            ) {

                                if(postLikelistId.get(i2).getId() != post.getId() )
if( currentUser.getId() != 1 ){

}

                                cd.ajouterLike(pl);
                            } else {

                                System.out.println("leeeeeeeeeeeeeeeeee");

                            }



                    }


            }*/



            }
        });

nbComments.setOnMouseClicked(new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent event) {
List<Commentaire> commentaires;
AllCommentsController cmd=new AllCommentsController();

        commentaires=cmd.afficherLesCommentaires(post);
        int column=0;
        int row=0;
        try {
            for(int i=0;i<commentaires.size();i++){



                FXMLLoader fxml=new FXMLLoader();
                fxml.setLocation(getClass().getResource("/Template/Feed/User/Commentaire.fxml"));
                Stage stage=new Stage();
                Scene scene = new Scene(fxml.load());
                stage.setTitle("Welcome User!");
                stage.setScene(scene);
                System.out.println("77777777777777777777"+commentaires.get(i).getContent());

              //  AnchorPane ap=fxml.load();

                CommentaireController commentaireController=fxml.getController();



                commentaireController.setData(commentaires.get(i));

                stage.resizableProperty().set(false);
                stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/icon.png"))));
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((screenBounds.getWidth() - scene.getWidth()) / 2);
                stage.setY((screenBounds.getHeight() - scene.getHeight()) / 2);
                //stage.centerOnScreen();
                //stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
            } }catch (IOException e) {
            throw new RuntimeException(e);
        }




    }
});



    }

    public void handleButton(ActionEvent event) {


        if(event.getSource()==  btnCommentaire){
            Commentaire c=new Commentaire();
            UserDAO UserDAO = new UserDAO();
            currentUser = UserDAO.findById(125);





        }



    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        webengine = webmap.getEngine();



        url = this.getClass().getResource("/Template/Feed/map2/map/recupMap.html");
        webengine.load(url.toString());
    }

    public void  refrsh(Post p){
        PostCrud pcd=new PostCrud();
        int n=0;
        n=pcd.getNbComments(p);


    }


    public List<User> afficherLesUserId(){

        ObservableList<Post> postsList= FXCollections.observableArrayList();



        try{
            String sql="select id from user";

            ResultSet rs;
            PreparedStatement st = DataSource.getConnection().prepareStatement(sql);

            rs=st.executeQuery(sql);

            while (rs.next()){
             User u=new User();
                u.setId(rs.getInt("id"));






                listUserId.add(u);
                //   System.out.println(post1.getId());



            }
        }catch(SQLException e){

            e.printStackTrace();
        }

        return  listUserId;

    }
    public List<User> afficherLesPostLikesUserId(){

        ObservableList<Post> postsList= FXCollections.observableArrayList();



        try{
            String sql="select user_id from post_like";

            ResultSet rs;
            PreparedStatement st = DataSource.getConnection().prepareStatement(sql);

            rs=st.executeQuery(sql);

            while (rs.next()){
                User u=new User();
                u.setId(rs.getInt("user_id"));







                postLikelistUserId.add(u);
                //   System.out.println(post1.getId());



            }
        }catch(SQLException e){

            e.printStackTrace();
        }

        return  postLikelistUserId;

    }

    public List<PostLikes> afficherLesPostLikesId(Post p){

        ObservableList<Post> postsList= FXCollections.observableArrayList();



        try{
            String sql="select * from post_like where post_id ='" + p.getId() + "'";

            ResultSet rs;
            PreparedStatement st = DataSource.getConnection().prepareStatement(sql);

            rs=st.executeQuery(sql);

            while (rs.next()){
PostLikes pl=new PostLikes();
                pl.setId(rs.getInt("post_id"));
                pl.setUserId(rs.getInt("user_id"));







                postLikelistId.add(pl);
                //   System.out.println(post1.getId());



            }
        }catch(SQLException e){

            e.printStackTrace();
        }

        return  postLikelistId;

    }


}
