package edu.esprit.DAO.Request;

import edu.esprit.DAO.UserDAO;
import edu.esprit.entities.Request;
import edu.esprit.entities.Service;
import edu.esprit.entities.User;
import edu.esprit.utils.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RequestCRUD {

    public void CreateRequest(Request R) {
        try {
            String req = "INSERT INTO service_request (type_id,requester_id,title,description,email,status,created_at,updated_at) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement pst = DataSource.getConnection().prepareStatement(req);
            pst.setInt(1, R.typeProperty().get().idProperty().get());
            pst.setInt(2, R.requesterProperty().get().getId());
            pst.setString(3, R.titleProperty().get());
            pst.setString(4, R.descriptionProperty().get());
            if (!R.emailProperty().get().equals(""))
                pst.setString(5, R.emailProperty().get());
            pst.setString(5, null);
            pst.setString(6, R.statusProperty().get().toString());
            pst.setString(7, R.getCreated_at());
            pst.setString(8, R.getUpdated_at());
            pst.executeUpdate();
            System.out.println("Request envoyé avec succès");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public List<Request> ReadRequests() {
        List<Request> ListRequests = new ArrayList<Request>();
        try {
            String req = "SELECT * FROM service_request";
            Statement st = DataSource.getConnection().createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Service S = new Service();
                S.idProperty().set(rs.getInt("type_id"));
                UserDAO userDAO = new UserDAO();
                User U = userDAO.findById(rs.getInt("requester_id"));
                Request R = new Request(rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("email"),
                        S, U);
                R.setCreated_at(rs.getString("created_at"));
                R.setUpdated_at(rs.getString("updated_at"));
                R.setResponded_at(rs.getString("responded_at"));
                R.setstatus(rs.getString("status"));
                if (rs.getString("request_response") != null)
                    R.responseProperty().set(rs.getString("request_response"));
                R.idProperty().set(rs.getInt("id"));
                ListRequests.add(R);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return ListRequests;
    }

    public void UpdateRequest(Request R) {
        try {
            String req = "UPDATE service_request SET type_id=?,title=?,description=?,email=?,updated_at=? WHERE id=?";
            PreparedStatement pst = DataSource.getConnection().prepareStatement(req);
            pst.setInt(1, R.typeProperty().get().idProperty().get());
            pst.setString(2, R.titleProperty().get());
            pst.setString(3, R.descriptionProperty().get());
            if (R.emailProperty().get() != "")
                pst.setString(4, R.emailProperty().get());
            else pst.setString(4, null);
            pst.setString(5, R.getUpdated_at());
            pst.setInt(6, R.idProperty().get());
            pst.executeUpdate();
            System.out.println("Request modifiée avec succès");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void DeleteReuest(Request R) {
        try {
            Statement st = null;
            st = DataSource.getConnection().createStatement();
            String req = "DELETE FROM service_request WHERE id=" + R.idProperty().get();
            st.executeUpdate(req);
            System.out.println("Request suprimée avec succès");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
