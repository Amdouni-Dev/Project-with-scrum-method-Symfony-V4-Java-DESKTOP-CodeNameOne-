package edu.esprit.tests;

import com.github.javafaker.Faker;
import edu.esprit.DAO.GroupDAO;
import edu.esprit.DAO.UserDAO;
import edu.esprit.entities.Group;
import edu.esprit.enums.GroupType;

import java.util.List;

public class GroupTests {
    public static void main(String[] args) {
        Faker instance = Faker.instance();
        GroupDAO groupDAO = new GroupDAO();
        UserDAO userDAO = new UserDAO();

        System.out.println("=== testing fetch all");
        List<Group> groups = groupDAO.findAll();
        assert groups != null;
        assert groups.size() > 0;

        System.out.println("=== testing fetch by id");
        Group group = groupDAO.findById(groups.get(0).getId());
        assert group != null;

        int size = group.getMembers().size();
        assert size > 0;
        System.out.println(group);

        // test insert
        System.out.println("=== testing insert");
        Group testing_group = new Group();
        String testing_role_title = instance.bothify("ROLE_TESTING_#####");
        testing_group.setDisplayName(instance.job().title());
        testing_group.setGroupType(GroupType.FACULTY_STAFF);
        testing_group.setSecurityTitle(testing_role_title);
        testing_group.addMember(userDAO.findById(3272));
        testing_group.addMember(userDAO.findById(3273));
        groupDAO.persist(testing_group);
        testing_group = groupDAO.findById(testing_group.getId());
        assert testing_group.getDisplayName().equals(testing_role_title);
        assert testing_group.getMembers().size() == 2;
        assert testing_group.getMembers().contains(userDAO.findById(3272));


        // test update
        System.out.println("=== testing update");
        String new_display_name = instance.bothify("NEW_DISPLAY_NAME_#####");
        testing_group.setDisplayName(new_display_name);
        groupDAO.persist(testing_group);
        testing_group = groupDAO.findById(testing_group.getId());
        assert testing_group.getDisplayName().equals(new_display_name);

        testing_group.addMember(userDAO.findById(3274));
        testing_group.removeMember(userDAO.findById(3272));
        groupDAO.persist(testing_group);
        testing_group = groupDAO.findById(testing_group.getId());
        assert testing_group.getMembers().size() == 2;
        assert !testing_group.getMembers().contains(userDAO.findById(3272));
        assert testing_group.getMembers().contains(userDAO.findById(3273));
        assert testing_group.getMembers().contains(userDAO.findById(3274));


        // test delete
        System.out.println("=== testing delete");
        groupDAO.delete(testing_group);
        assert groupDAO.findById(testing_group.getId()) == null;
    }
}
