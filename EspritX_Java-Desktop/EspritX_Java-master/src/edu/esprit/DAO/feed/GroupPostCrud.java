package edu.esprit.DAO.feed;


import edu.esprit.entities.feed.GroupPost;
import edu.esprit.utils.DataSource2;

import java.sql.*;
import java.time.LocalDateTime;

public class GroupPostCrud {
    Connection cnx2;
    
    public GroupPostCrud(){
         cnx2= DataSource2.getInstance().getCnx();
    }

    public void ajouterGroupPost(GroupPost gp){
        int isValid=0;
        int isDeleted=1;
        LocalDateTime createdAtUpAt = LocalDateTime. now();
        String dateCReUp=createdAtUpAt.toString();
        //     public Post(int id, String title, String content, int userId, String slug, Date ceatedAt, Boolean isValid, Date updatedAt, Boolean isDeleted, String longitude, String latitude, String image) {
        String req="insert into group_post (user_id,nom_groupe,slug,image,created_at,is_valid,is_deleted,but) values" +
                "('"


                +gp.getUserId()+ "','"
                +gp.getNomGroupe()+ "','"
                +gp.getSlug()+ "','"
                +gp.getImage()+ "','"
                +createdAtUpAt+ "','"
                +isValid+ "','"
                +isDeleted+ "','"
                +gp.getBut()+


                "')";
        try{
            Statement st=cnx2.createStatement();
            st.executeUpdate(req);
            System.out.println("Groupe  bien ajouté");

        }catch(SQLException e){
            System.out.println(e);
        }

    }
    public void SupprimerGroupPost(GroupPost gp){
        String sql = "DELETE FROM group_post WHERE id=?";
        try{
            PreparedStatement st=cnx2.prepareStatement(sql);

            st.setInt(1,gp.getId());

            st.executeUpdate();

            System.out.println("Bien groupe bien supprimé Definitivement");

        }catch(SQLException e){
            System.out.println(e);
        }


    }
    public void ChangerValiditéGroup(GroupPost gp){


        String sqlUpPost = "UPDATE group_post SET is_valid=? WHERE id=?";

        int valid=1;


        try{

            PreparedStatement st=cnx2.prepareStatement(sqlUpPost);

            if(gp.getValid()==false){
                valid=1;



            }
            if(gp.getValid()==true){

                valid=0;
            }
            st.setInt(1,valid);

            st.setInt(2,gp.getId());
            st.executeUpdate();

            System.out.println("groupe bien approuvé");

        }catch(SQLException e){
            System.out.println(e);
        }


    }
    public void changeDelete(GroupPost gp){

        String sqlUpPost = "UPDATE group_post SET is_deleted =? WHERE id=?";

        int deleted=1;


        try{

            PreparedStatement st=cnx2.prepareStatement(sqlUpPost);

            if(gp.getDeleted()==false){
                deleted=1;



            }
            if(gp.getDeleted()==true){

                deleted=0;
            }
            st.setInt(1,deleted);

            st.setInt(2,gp.getId());
            st.executeUpdate();

            System.out.println("Groupe  bien supprimé");

        }catch(SQLException e){
            System.out.println(e);
        }
    }
    public void afficherAllGroups(){
        String sql="select * from group_post";
        try{
            Statement st = cnx2.createStatement() ;
            ResultSet result = st.executeQuery(sql);
            int count = 0;
            while (result.next()){
                int id=result.getInt(1);
                int userid=result.getInt(2);
                String nomGroupe = result.getString(3);
                Date createdAt=result.getDate(6);
                int isValid=result.getInt(7);
                int isDeleted=result.getInt(8);
                String but= result.getString(9);

                System.out.println(  "id "+id +" user "+userid+" NomGroupe" +nomGroupe+ " valid  " +isValid +" deleted " +isDeleted+" crée le"+createdAt+ " But: "+but );
            }
        }catch(SQLException e){
            System.out.println(e);
        }

    }
}
