package edu.esprit.DAO.feed;

import edu.esprit.entities.feed.Commentaire;
import edu.esprit.entities.feed.Post;
import edu.esprit.utils.DataSource;
import edu.esprit.utils.DataSource2;

import java.sql.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PostCrud {
    
    Connection cnx2;
    
    public PostCrud(){
     cnx2= DataSource2.getInstance().getCnx();
    
    }

    public void ajouterPost(Post p){
        int isValid=0;
        int isDeleted=0;
        LocalDateTime createdAtUpAt = LocalDateTime. now();
        String dateCReUp=createdAtUpAt.toString();
        //     public Post(int id, String title, String content, int userId, String slug, Date ceatedAt, Boolean isValid, Date updatedAt, Boolean isDeleted, String longitude, String latitude, String image) {

        String req="insert into post (title,content,user_id,slug,created_at,is_valid,updated_at,is_deleted,longitude,latitude,image) values" +
                "('"

                +p.getTitle()+  "','"
                +p.getContent()+  "','"
                +p.userP().get().getId()+ "','"
                +p.getSlug()+ "','"
                +createdAtUpAt+ "','"
                +isValid+ "','"
                +createdAtUpAt+ "','"
                +isDeleted+ "','"
                +p.getLongitude()+ "','"
                +p.getLatitude()+ "','"
                +p.getImage()+


                "')";
        try{
            PreparedStatement st = DataSource.getConnection().prepareStatement(req);
          //  Statement st=cnx2.createStatement();
            st.executeUpdate(req);
            System.out.println("Bien post bien ajouté");

        }catch(SQLException e){
            System.out.println(e);
        }

    }
    public void updatePost(Post p,int id) throws SQLException{
        PreparedStatement ps;
        String query = "UPDATE `post` SET `title`=?,`content`=? WHERE id=?";

        PreparedStatement st = DataSource.getConnection().prepareStatement(query);
        st.setString(1, p.getTitle());
        st.setString(2, p.getContent());

        st.setInt(3, id);
        st.execute();

    }
    public void SupprimerPost(Post p){
        String sql = "DELETE FROM post WHERE id=?";
        try{
            PreparedStatement st=cnx2.prepareStatement(sql);

            st.setInt(1,p.getId());

            st.executeUpdate();

            System.out.println("Bien post bien supprimé Definitivement");

        }catch(SQLException e){
            System.out.println(e);
        }


    }
    public void afficherAllPosts(){
        String sql="select * from post";
        try{
            Statement st = cnx2.createStatement() ;
            ResultSet result = st.executeQuery(sql);
            int count = 0;
            while (result.next()){
                int id=result.getInt(1);
                String title = result.getString(4);
                String content = result.getString(6);
                int isValid=result.getInt(8);
                int isDeleted=result.getInt(10);
                System.out.println(  "id "+id +" "+title+" " +content+ " valid  " +isValid +" deleted " +isDeleted);
            }
        }catch(SQLException e){
            System.out.println(e);
        }

    }
    public void ChangerValidité(Post p){


        String sqlUpPost = "UPDATE post SET is_valid=? WHERE id=?";

int valid=1;


        try{

            PreparedStatement st=cnx2.prepareStatement(sqlUpPost);

            if(p.getValid()==false){
                valid=1;



            }
            if(p.getValid()==true){

                valid=0;
            }
            st.setInt(1,valid);

            st.setInt(2,p.getId());
            st.executeUpdate();

            System.out.println("post bien approuvé");

        }catch(SQLException e){
            System.out.println(e);
        }


    }
    public void changeDelete(Post p){

        String sqlUpPost = "UPDATE post SET is_deleted =? WHERE id=?";

        int deleted=1;


        try{

            PreparedStatement st=cnx2.prepareStatement(sqlUpPost);

            if(p.getDeleted()==false){
                deleted=1;



            }
            if(p.getDeleted()==true){

                deleted=0;
            }
            st.setInt(1,deleted);

            st.setInt(2,p.getId());
            st.executeUpdate();

            System.out.println("post bien supprimé");

        }catch(SQLException e){
            System.out.println(e);
        }
    }

    public Integer getNbComments(Post p){
int nb=0;
        ResultSet rs = null;
        String sqlNBC = "select count(*) from commentaire   where post_id ='" + p.getId() + "'";

// stmt = conn.getPreparedStatement("select id_usuario, id_grupo from usuarios_grupos where id_grupo ='" + var + "'");

        try{
            int numberOfRows=0;
            PreparedStatement st = DataSource.getConnection().prepareStatement(sqlNBC);
            rs = st.executeQuery();


            if (rs.next()) {

                numberOfRows = rs.getInt(1);

                System.out.println(  "numberOfRows= " + numberOfRows+p.getId());
            } else {
                System.out.println("error: could not get the record counts");
            }
nb=numberOfRows;
            System.out.println("yyyyyyyyyy"+nb);
        }catch(SQLException e){
            System.out.println(e);
        }
 return  nb;
    }
    public Integer getNbLikes(Post p){
        int nb=0;
        ResultSet rs = null;
        String sqlNBC = "select count(*) from post_like   where post_id ='" + p.getId() + "'";

// stmt = conn.getPreparedStatement("select id_usuario, id_grupo from usuarios_grupos where id_grupo ='" + var + "'");

        try{
            int numberOfRows=0;
            PreparedStatement st = DataSource.getConnection().prepareStatement(sqlNBC);
            rs = st.executeQuery();


            if (rs.next()) {

                numberOfRows = rs.getInt(1);

                System.out.println(  "numberOfRows= " + numberOfRows+p.getId());
            } else {
                System.out.println("error: could not get the record counts");
            }
            nb=numberOfRows;
            System.out.println("yyyyyyyyyy"+nb);
        }catch(SQLException e){
            System.out.println(e);
        }
        return  nb;
    }

    public List<Commentaire> getCommentsForPost(Post p){
List<Commentaire> listComment=new ArrayList<>();
Commentaire c=new Commentaire();

        String sqlNBC = "select *  from commentaire   where post_id ='" + p.getId() + "'";

// stmt = conn.getPreparedStatement("select id_usuario, id_grupo from usuarios_grupos where id_grupo ='" + var + "'");

        try{

            PreparedStatement st = DataSource.getConnection().prepareStatement(sqlNBC);
            ResultSet result = st.executeQuery(sqlNBC);
            int count = 0;
            while (result.next()){
                int id=result.getInt(1);
                int userId=result.getInt(2);
                int postId=result.getInt(3);
                Date createdat=result.getDate(4);
                c.setId(id);
                c.setUserId(userId);
                c.setPostId(postId);
                listComment.add(c);

                String content = result.getString(5);
                System.out.println(  "id "+id + " userId "+userId+ " postId "+postId+" crée le "+createdat+ " content :"+content);
            }
        }catch(SQLException e){
            System.out.println(e);
        }

return listComment;
    }

}
