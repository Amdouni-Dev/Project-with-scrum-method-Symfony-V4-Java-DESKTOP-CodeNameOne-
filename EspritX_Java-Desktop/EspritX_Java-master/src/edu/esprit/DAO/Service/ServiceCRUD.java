package edu.esprit.DAO.Service;

import edu.esprit.DAO.GroupDAO;
import edu.esprit.DAO.Request.RequestCRUD;
import edu.esprit.DAO.Request.RequestRepository;
import edu.esprit.entities.Group;
import edu.esprit.entities.Request;
import edu.esprit.entities.Service;
import edu.esprit.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceCRUD {

    public void CreateService(Service S) {
        try {
            String req = "INSERT INTO service (responsible_id,name) VALUES (?,?)";
            PreparedStatement pst = DataSource.getConnection().prepareStatement(req);
            pst.setInt(1, S.responsibleProperty().get().getId());
            pst.setString(2, S.nameProperty().get());
            pst.executeUpdate();
            String req1 = "SELECT id FROM service ORDER BY ID DESC LIMIT 1";
            Statement st = DataSource.getConnection().createStatement();
            ResultSet rs = st.executeQuery(req1);
            while (rs.next()) {
                S.idProperty().set(rs.getInt("id"));
            }
            for (Group g : S.recipientsProperty().get()) {
                String req2 = "INSERT INTO service_group VALUES (?,?)";
                PreparedStatement pst1 = DataSource.getConnection().prepareStatement(req2);
                pst1.setInt(1, S.idProperty().get());
                pst1.setInt(2, g.getId());
                pst1.executeUpdate();
            }
            System.out.println("Service ajoutée avec succès");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public List<Service> ReadServices() {
        List<Service> ListServices = new ArrayList<Service>();
        GroupDAO groupDAO = new GroupDAO();
        List<Group> ListGroups = groupDAO.findAll();
        try {
            String req = "SELECT * FROM service";
            Statement st = DataSource.getConnection().createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Service S = new Service();
                String req1 = "SELECT * FROM service_group WHERE service_id=" + rs.getInt("id");
                String req2 = "SELECT * FROM service_request WHERE type_id=" + rs.getInt("id");
                S.idProperty().set(rs.getInt("id"));
                S.nameProperty().set(rs.getString("name"));
                Group g = groupDAO.findById(rs.getInt("responsible_id"));
                S.responsibleProperty().set(g);
                List<Group> Recipients = new ArrayList<>();
                Statement st1 = DataSource.getConnection().createStatement();
                ResultSet rs1 = st1.executeQuery(req1);
                while (rs1.next()) {
                    Group g1 = groupDAO.findById(rs1.getInt("group_id"));
                    Recipients.add(g1);
                }
                rs1.close();
                S.recipientsProperty().set(FXCollections.observableList(Recipients));
                List<Request> Requests = new ArrayList<>();
                Statement st2 = DataSource.getConnection().createStatement();
                ResultSet rs2 = st2.executeQuery(req2);
                while (rs2.next()) {
                    RequestRepository RREPO=new RequestRepository();
                    Request r=RREPO.findById(rs2.getInt("id"));
                    Requests.add(r);
                }
                S.requestsProperty().set(FXCollections.observableList(Requests));
                rs2.close();
                ListServices.add(S);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return ListServices;
    }

    public void UpdateService(Service S) {
        try {
            String req = "UPDATE service SET responsible_id =?,name=? WHERE id=?";
            PreparedStatement pst = DataSource.getConnection().prepareStatement(req);
            pst.setInt(1, S.responsibleProperty().get().getId());
            pst.setString(2, S.nameProperty().get());
            pst.setInt(3, S.idProperty().get());
            pst.executeUpdate();
            String req1 = "DELETE FROM service_group WHERE service_id=" + S.idProperty().get();
            Statement st = DataSource.getConnection().createStatement();
            st.executeUpdate(req1);
            for (Group g : S.recipientsProperty().get()) {
                String req2 = "INSERT INTO service_group VALUES(?,?)";
                PreparedStatement pst1 = DataSource.getConnection().prepareStatement(req2);
                pst1.setInt(1, S.idProperty().get());
                pst1.setInt(2, g.getId());
                pst1.executeUpdate();
                pst1.close();
            }
            System.out.println("Service modifiée avec succès");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void DeleteService(Service S) {
        try {
            Statement st = null;
            st = DataSource.getConnection().createStatement();
            String req1 = "DELETE FROM service_group WHERE service_id=" + S.idProperty().get();
            String req2 = "DELETE FROM service_request WHERE type_id=" + S.idProperty().get();
            String req3 = "DELETE FROM service WHERE id=" + S.idProperty().get();
            st.executeUpdate(req1);
            st.executeUpdate(req2);
            st.executeUpdate(req3);
            System.out.println("Service suprimée avec succès");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
