package edu.esprit.DAO.feed;

import edu.esprit.entities.feed.Commentaire;
import edu.esprit.entities.feed.PostLikes;
import edu.esprit.utils.DataSource;
import edu.esprit.utils.DataSource2;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class PostLikeCrud {
    
    Connection cnx2;
    public PostLikeCrud(){
         cnx2= DataSource2.getInstance().getCnx();
    }
    

    public void Liker(PostLikes p){

        String req="insert into post_like (user_id,post_id) values" +
                "('"


                +p.getUserId()+ "','"
                +p.getPostId()+


                "')";
        try{
            Statement st=cnx2.createStatement();
            st.executeUpdate(req);
            System.out.println("Like  bien ajouté");

        }catch(SQLException e){
            System.out.println(e);
        }

    }
    public void unliker(PostLikes pl ){
        String sql = "DELETE FROM post_like WHERE id=?";
        try{
            PreparedStatement st=cnx2.prepareStatement(sql);

            st.setInt(1,pl.getId());

            st.executeUpdate();

            System.out.println("Like bien supprimé ");

        }catch(SQLException e){
            System.out.println(e);
        }


    }
    public void ajouterLike(PostLikes pl){



        String req = "INSERT INTO post_like(user_id,post_id) VALUES ('"+pl.userProperty().get().getId()+"','"+pl.postProperty().get().getId()+"')" ;
        try{
            PreparedStatement st = DataSource.getConnection().prepareStatement(req);
            //  Statement st=cnx2.createStatement();
            st.executeUpdate(req);
            System.out.println("Like bien ajouté");

        }
        catch(SQLException e){
            System.out.println("nooooooooooooooooo");
            System.out.println(e);
        }

    }
}
