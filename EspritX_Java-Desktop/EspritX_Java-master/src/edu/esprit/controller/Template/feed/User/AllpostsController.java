package edu.esprit.controller.Template.feed.User;

import edu.esprit.entities.feed.Post;
import edu.esprit.utils.DataSource2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AllpostsController implements Initializable {


    public AnchorPane id_AllPostst;
    public Label tfTitle;



    Connection cnx2;

    public ObservableList<Post> afficherLesPosts(){

        ObservableList<Post> postsList= FXCollections.observableArrayList();
        String sql="select id,title,content,user_id,longitude , latitude , is_valid, is_deleted from post";
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



                postsList.add(post1);
                //   System.out.println(post1.getId());



            }
        }catch(SQLException e){

            e.printStackTrace();
        }

        return  postsList;

    }

    public void allposts(){
        ObservableList<Post> list=afficherLesPosts();

        for (int i=0; i<list.size(); i++) {
 id_AllPostst= new  AnchorPane();
            // tfTitle.setCellValueFactory(new PropertyValueFactory<Post, String>("title"));
            tfTitle.setText(String.valueOf(new PropertyValueFactory<Post, String>("title")));

        }





    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {

        // webengine = webmap.getEngine();

        // url = this.getClass().getResource("/Template/Feed/map2/map/index.html");
        //  webengine.load(url.toString());


    }
}
