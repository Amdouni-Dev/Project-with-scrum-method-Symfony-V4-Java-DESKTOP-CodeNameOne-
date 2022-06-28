package edu.esprit.gui.Feed.Admin;


import edu.esprit.entities.feed.Post;


import edu.esprit.utils.DataSource2;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GererPostsAd extends Application {
Connection cnx2;
    @Override
    public void start(Stage stage) {
        cnx2= DataSource2.getInstance().getCnx();
        TableView<Post> table = new TableView<Post>();

        // Editable
        table.setEditable(true);

        TableColumn<Post, String> titleCol //
                = new TableColumn<Post, String>("title");
        TableColumn<Post, String> contentcol
                = new TableColumn<Post, String>("content");
        TableColumn<Post, Boolean> validCol
                = new TableColumn<Post, Boolean>("valide ?");



        // title//////////////////////////////////////////
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        titleCol.setCellFactory(TextFieldTableCell.<Post> forTableColumn());

        titleCol.setMinWidth(200);
        /// Content //////////////////////////////////////
        contentcol.setCellValueFactory(new PropertyValueFactory<>("content"));
        contentcol.setCellFactory(TextFieldTableCell.<Post> forTableColumn());

        contentcol.setMinWidth(200);




        /////////////////////////////// Valid ou non valid

       // validCol.setCellFactory(TextFieldTableCell.<Post> forTableColumn());

        validCol.setMinWidth(200);




        // nbvadel l content mte3 title
        titleCol.setOnEditCommit((CellEditEvent<Post, String> event) -> {
            TablePosition<Post, String> pos = event.getTablePosition();

            String newTitle = event.getNewValue();


            int row = pos.getRow();

            Post post = event.getTableView().getItems().get(row);

            /****************************/
            String sql="select id from post";
            int id=0;
            ArrayList<Integer> listId=new ArrayList<>() ;
            try{
                Statement st = cnx2.createStatement() ;
                ResultSet result = st.executeQuery(sql);
                int count = 0;
                while (result.next()){
                     id=result.getInt(1);
                     listId.add(id);

                    System.out.println(  "id "+id );
                }
            }catch(SQLException e){
                System.out.println(e);
            }
            int i=0;
      int idd;
            String sqlUpPost = "UPDATE post SET title=?  WHERE id=?";
         try{


                PreparedStatement st=cnx2.prepareStatement(sqlUpPost);
                st.setString(1,newTitle);

             for( i=0 ; i<listId.size(); i++ ){


                 st.setInt(2, listId.get(i));
             }


                st.executeUpdate();

                System.out.println("Bien post bien modifié");

            }catch(SQLException e){
                System.out.println(e);
            }


        });

// nbadel l content mte3 content
        contentcol.setOnEditCommit((CellEditEvent<Post, String> event) -> {
            TablePosition<Post, String> pos = event.getTablePosition();

            String newContent = event.getNewValue();


            int row = pos.getRow();

            Post post = event.getTableView().getItems().get(row);

            /****************************/
            String sql="select id from post";
            int id=0;
            ArrayList<Integer> listId=new ArrayList<>() ;
            try{
                Statement st = cnx2.createStatement() ;
                ResultSet result = st.executeQuery(sql);
                int count = 0;
                while (result.next()){
                    id=result.getInt(1);
                    listId.add(id);

                    System.out.println(  "id "+id );
                }
            }catch(SQLException e){
                System.out.println(e);
            }
            int i=0;
            int idd;
            String sqlUpPost = "UPDATE post SET content=?  WHERE id=?";
            try{


                PreparedStatement st=cnx2.prepareStatement(sqlUpPost);
                st.setString(1,newContent);

                for( i=0 ; i<listId.size(); i++ ){


                    st.setInt(2, listId.get(i));
                }


                st.executeUpdate();

                System.out.println("Bien post bien modifié");

            }catch(SQLException e){
                System.out.println(e);
            }


        });
// nbadel kenou valid w lee
        validCol.setCellValueFactory(new Callback<CellDataFeatures<Post, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<Post, Boolean> param) {




                Post post = param.getValue();

                String sql="select id , is_valid from post";
                int id=0;
                Boolean v=true;
                ArrayList<Integer> listId=new ArrayList<>() ;
                ArrayList<Boolean> listValid=new ArrayList<>();
                try{
                    Statement st = cnx2.createStatement() ;
                    ResultSet result = st.executeQuery(sql);
                    int count = 0;
                    while (result.next()){
                        id=result.getInt(1);
                        v=result.getBoolean(2);
                        listId.add(id);
                        listValid.add(v);

                        System.out.println(  "id "+id );
                        System.out.println("Valid"+v);
                    }
                }catch(SQLException e){
                    System.out.println(e);
                }
                ObservableList<Post> list=afficherLesPosts();



                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(post.getValid());
                System.out.println("uuuuuuuuuuu"+post.getValid());



                booleanProp.addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                        Boolean newValue) {

                        String sqlUpPost = "UPDATE post SET is_valid=? WHERE id=?";
                        int valid=1;




                            try{

                                PreparedStatement st=cnx2.prepareStatement(sqlUpPost);
                                for(int i=0;i<list.size(); i++) {
                                    if (list.get(i).getValid() == false) {
                                        valid = 1;


                                    }
                                    if (list.get(i).getValid() == true) {

                                        valid = 0;
                                    }


                                    st.setInt(1, valid);
                                }
                                for( int i=0 ; i<listId.size(); i++ ){


                                    st.setInt(2, listId.get(i));
                                }


                                st.executeUpdate();

                                System.out.println("post bien approuvé");

                            }catch(SQLException e){
                                System.out.println(e);
                            }
                        }


























                });

                return booleanProp;
            }


        });

        validCol.setCellFactory(new Callback<TableColumn<Post, Boolean>, //
                TableCell<Post, Boolean>>() {
            @Override
            public TableCell<Post, Boolean> call(TableColumn<Post, Boolean> p) {
                CheckBoxTableCell<Post, Boolean> cell = new CheckBoxTableCell<Post, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });



        ObservableList<Post> list = getPostList();
        table.setItems(list);

        table.getColumns().addAll(titleCol,contentcol,validCol);

        StackPane root = new StackPane();
        root.setPadding(new Insets(5));
        root.getChildren().add(table);

        stage.setTitle("Admin Table");

        Scene scene = new Scene(root, 450, 300);
        stage.setScene(scene);
        stage.show();
    }

    private ObservableList<Post> getPostList() {
        Connection cnx2;


        ObservableList<Post> postsList= FXCollections.observableArrayList();
        String sql="select title , content , is_valid from post";
        Statement st;
        ResultSet rs;
        cnx2= DataSource2.getInstance().getCnx();
        try{

            st=cnx2.createStatement();
            rs=st.executeQuery(sql);

            while (rs.next()){
                Post post1=new Post();

                post1.setTitle(rs.getString("title"));
                post1.setContent(rs.getString("content"));
                post1.setValid(rs.getBoolean("is_valid"));




                postsList.add(post1);
                //   System.out.println(post1.getId());



            }
        }catch(SQLException e){

            e.printStackTrace();
        }

        return  postsList;



    }

    public static void main(String[] args) {
        launch(args);

    }
public int getPostId(Post p){
        return p.getId();

}

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

}