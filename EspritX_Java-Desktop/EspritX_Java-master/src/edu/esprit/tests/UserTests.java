package edu.esprit.tests;

import edu.esprit.DAO.UserDAO;
import edu.esprit.entities.User;
import edu.esprit.enums.UserStatus;

import java.util.List;

public class UserTests {
    public static void main(String[] args) {
        // test fetch all
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.findAll();
        System.out.println(users);

        System.out.println("last id: " + userDAO.findLastId());

        User user = userDAO.findById(3185);
        System.out.println(user);

        // test update
        user.setFirstName("Mohamed");
        userDAO.update(user);

        user.setFirstName("Mohamed 2");
        user.setUserStatus(UserStatus.PENDING);
        userDAO.update(user);

        // test delete
        //userDAO.delete(userDAO.findById(3313));
    }
}
