package edu.esprit.tests;

import edu.esprit.DAO.GroupDAO;
import edu.esprit.DAO.UserDAO;
import edu.esprit.entities.Group;
import edu.esprit.entities.Request;
import edu.esprit.entities.Service;
import edu.esprit.entities.User;
import edu.esprit.DAO.Request.RequestCRUD;
import edu.esprit.DAO.Request.RequestRepository;
import edu.esprit.services.RequestStatistics;
import edu.esprit.DAO.Service.ServiceCRUD;

import java.util.ArrayList;
import java.util.List;

public class ServiceTests {
    public static void main(String[] args) {
        GroupDAO groupDAO = new GroupDAO();
        Group g1 = groupDAO.findById(47);
        Group g2 = groupDAO.findById(46);
        List<Group> Listgroups = new ArrayList<Group>();
        List<Request> ListRequests = new ArrayList<Request>();
        Listgroups.add(g1);
        Listgroups.add(g2);
        Service S = new Service("Test",g1,Listgroups,ListRequests);
        ServiceCRUD SCRUD = new ServiceCRUD();
        S.idProperty().set(119);
        //SCRUD.UpdateService(S);
        List<Service> ListServices = SCRUD.ReadServices();
        System.out.println(ListServices);
        UserDAO uDAO = new UserDAO();
        User u=uDAO.findById(253);

        Request R = new Request("hello", "test java", null,S, u);
        RequestCRUD RCRUD = new RequestCRUD();
        //RCRUD.CreateRequest(R);
        R.idProperty().set(294);
        R.titleProperty().set("badel");
        R.emailProperty().set("test@test.com");
        //RCRUD.UpdateRequest(R);
        //List<Request> ListRequest = RCRUD.ReadRequests();
        //System.out.println(ListRequest);
        //RCRUD.DeleteReuest(R);
        RequestRepository repo= new RequestRepository();
        System.out.println(repo.findById(260));
        try {
            System.out.println(repo.findById(500));
        } catch (Exception e) {
            System.err.println("No such Request, verify your entry.");
        }
        System.out.println(repo.findByService(S));
        //System.out.println(repo.findByTitle("vitae praesentium"));
        //RequestStatistics stat = new RequestStatistics();
        //System.out.println(stat.ResponseStatsByGroup(g1));
        //System.out.println(stat.ResponseStatsByService(S));
    }
}
