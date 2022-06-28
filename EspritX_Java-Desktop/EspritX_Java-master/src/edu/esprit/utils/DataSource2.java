package edu.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource2 {

    private String url="jdbc:mysql://localhost:3306/espritx";
    private String login="root";
    private String password="";


    Connection cnx;
    public static DataSource2 instance;
    private DataSource2(){

        try{
            cnx= DriverManager.getConnection(url,login,password);
            System.out.println("Connexion etablie");
        }catch(SQLException e){
            System.out.println("Connexion non etablie"+e);
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public void setCnx(Connection cnx) {
        this.cnx = cnx;
    }
    public static DataSource2 getInstance(){
        if(instance == null){
            instance = new DataSource2();

        }
        return instance;

    }


}
