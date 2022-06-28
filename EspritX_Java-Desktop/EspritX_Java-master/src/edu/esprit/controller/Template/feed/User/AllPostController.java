package edu.esprit.controller.Template.feed.User;

import edu.esprit.DAO.UserDAO;
import edu.esprit.DAO.feed.CommentaireCrud;
import edu.esprit.DAO.feed.PostCrud;
import edu.esprit.entities.feed.Commentaire;
import edu.esprit.entities.feed.Post;
import edu.esprit.utils.DataSource;
import edu.esprit.utils.DataSource2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static edu.esprit.controller.Template.feed.User.BadWords.filterText;

public class AllPostController implements Initializable {
    public ScrollPane scroll;
    public GridPane gridPane;

    public List<Post> posts=new ArrayList<>();
    public Button btnCharger;

    private List<Post> getData(){

        List<Post> posts=afficherLesPosts();
        Post p;
        for (int i=0; i< posts.size();i++){
            p=new Post();
            p.setTitle(posts.get(i).getTitle());
            posts.add(p);

        }
        return posts;


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       posts=afficherLesPosts();
        int column=0;
        int row=0;
        try {
        for(int i=0;i<posts.size();i++){
            FXMLLoader fxml=new FXMLLoader();
            fxml.setLocation(getClass().getResource("/Template/Feed/User/Post.fxml"));

                AnchorPane ap=fxml.load();

            PostController postController=fxml.getController();


            gridPane.add(ap,column,row++);
            GridPane.setMargin(ap,new Insets(10));
postController.setData(posts.get(i));
        } }catch (IOException e) {
                throw new RuntimeException(e);
            }



        /*btnCharger.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {


            posts.clear();
                try{
                    String sql="select id,title,content,user_id,longitude , latitude , is_valid, is_deleted , created_at from post";

                    ResultSet rs;
                    PreparedStatement st = DataSource.getConnection().prepareStatement(sql);

                    rs=st.executeQuery(sql);

                    while (rs.next()){
                        Post post1=new Post();
                        post1.setId(rs.getInt("id"));
                        Date currentDate =rs.getDate("created_at");
                        LocalDateTime localDateTime = Instant
                                .ofEpochMilli(currentDate.getTime())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();

                        System.out.println("Locale date time is :" + localDateTime);

                        post1.setCeatedAt(localDateTime);

                        post1.setTitle(rs.getString("title"));
                        post1.setContent(rs.getString("content"));
                        post1.setUserId(rs.getInt("user_id"));
                        post1.setLongitude(rs.getString("longitude"));
                        post1.setLatitude(rs.getString("latitude"));
                        post1.setValid(rs.getBoolean("is_valid"));

                        post1.setDeleted(rs.getBoolean("is_deleted"));



                        posts.add(post1);
                        //   System.out.println(post1.getId());



                    }
                }catch(SQLException ex){

                    ex.printStackTrace();
                }

            }
        });*/













    }


    public List<Post> afficherLesPosts(){

        ObservableList<Post> postsList= FXCollections.observableArrayList();



        try{
            String sql="select id,title,content,user_id,longitude , latitude , is_valid, is_deleted , created_at from post where is_valid=true order by created_at Desc  ";

            ResultSet rs;
            PreparedStatement st = DataSource.getConnection().prepareStatement(sql);

            rs=st.executeQuery(sql);

            while (rs.next()){
                Post post1=new Post();
                post1.setId(rs.getInt("id"));
                Date currentDate =rs.getDate("created_at");
                LocalDateTime localDateTime = Instant
                        .ofEpochMilli(currentDate.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                System.out.println("Locale date time is :" + localDateTime);

                post1.setCeatedAt(localDateTime);

                post1.setTitle(rs.getString("title"));
                post1.setContent(rs.getString("content"));
                post1.setUserId(rs.getInt("user_id"));
                post1.setLongitude(rs.getString("longitude"));
                post1.setLatitude(rs.getString("latitude"));
                post1.setValid(rs.getBoolean("is_valid"));

                post1.setDeleted(rs.getBoolean("is_deleted"));



                posts.add(post1);
                //   System.out.println(post1.getId());



            }
        }catch(SQLException e){

            e.printStackTrace();
        }

        return  posts;

    }



public void refreshPosts(){

        posts.clear();
}

    public void back(ActionEvent event) throws IOException {
        Parent page2 = FXMLLoader.load(getClass().getResource("/Template/Feed/Admin/Menu.fxml"));

        Scene scene2 = new Scene(page2);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene2);
        app_stage.show();
    }

}

