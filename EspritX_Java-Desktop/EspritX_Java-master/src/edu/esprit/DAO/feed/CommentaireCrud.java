package edu.esprit.DAO.feed;

import edu.esprit.entities.feed.Commentaire;
import edu.esprit.entities.feed.Post;
import edu.esprit.utils.DataSource;
import edu.esprit.utils.DataSource2;


import java.sql.*;
import java.time.LocalDateTime;

public class CommentaireCrud {
Connection cnx2;
    
    public CommentaireCrud() {
    cnx2= DataSource2.getInstance().getCnx();
    }
    
    
    
  /*  public void ajouterCommentaire(Commentaire c){
        LocalDateTime createdAtUpAt = LocalDateTime. now();
        String dateCRe=createdAtUpAt.toString();
        String req="insert into commentaire (user_id,post_id,created_at,content) values" +
                "('"
                +c.getUserId()+  "','"
                +c.getPostId()+  "','"
                +dateCRe+        "','"
                +c.getContent()+
                "')";
        try{
            Statement st=cnx2.createStatement();
            st.executeUpdate(req);
            System.out.println("Bien Commentaire bien ajouté");

        }catch(SQLException e){
            System.out.println(e);
        }

    }*/
    public void ajouterComment(Commentaire c){

        LocalDateTime createdAtUpAt = LocalDateTime. now();
        String dateCReUp=createdAtUpAt.toString();
        //     public Post(int id, String title, String content, int userId, String slug, Date ceatedAt, Boolean isValid, Date updatedAt, Boolean isDeleted, String longitude, String latitude, String image) {

        String req="insert into commentaire (content,user_id,post_id,created_at) values" +
                "('"

                +c.getContent()+  "','"
                +c.userP().get().getId()+ "','"
                +c.postProperty().get().getId()+ "','"

                +createdAtUpAt+

                "')";



        try{
            PreparedStatement st = DataSource.getConnection().prepareStatement(req);
            //  Statement st=cnx2.createStatement();
            st.executeUpdate(req);
            System.out.println("Commentaire bien ajouté");

        }
        catch(SQLException e){
            System.out.println(e);
        }

    }













    public void updateCommentaire(Commentaire c){




        String sqlUpCommentaire = "UPDATE commentaire SET created_at=?, content=?  WHERE id=?";

        try{
            PreparedStatement st=cnx2.prepareStatement(sqlUpCommentaire);
            LocalDateTime today = LocalDateTime. now();
            String formattedDate = today.toString();
            st.setString(1,formattedDate);
            st.setString(2,c.getContent());
            st.setInt(3,c.getId());

            st.executeUpdate();

            System.out.println("Commentaire  bien modifié");

        }catch(SQLException e){
            System.out.println(e);
        }

    }
    public void SupprimerCommentaire(Commentaire  c){
        String sql = "DELETE FROM commentaire WHERE id=?";
        try{
            PreparedStatement st=cnx2.prepareStatement(sql);

            st.setInt(1,c.getId());

            st.executeUpdate();

            System.out.println("Bien Commentaire bien supprimée");

        }catch(SQLException e){
            System.out.println(e);
        }


    }
    public void afficherAllComments(){
        String sql="select * from commentaire";
        try{
            Statement st = cnx2.createStatement() ;
            ResultSet result = st.executeQuery(sql);
            int count = 0;
            while (result.next()){
                int id=result.getInt(1);
                int userId=result.getInt(2);
                int postId=result.getInt(3);
                Date createdat=result.getDate(4);

                String content = result.getString(5);
                System.out.println(  "id "+id + " userId "+userId+ " postId "+postId+" crée le "+createdat+ " content :"+content);
            }
        }catch(SQLException e){
            System.out.println(e);
        }

    }


}
