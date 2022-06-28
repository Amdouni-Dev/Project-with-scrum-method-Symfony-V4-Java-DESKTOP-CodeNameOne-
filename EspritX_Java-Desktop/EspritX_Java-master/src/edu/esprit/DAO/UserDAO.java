package edu.esprit.DAO;

import edu.esprit.entities.User;
import edu.esprit.lib.persistence.AbstractDAO;
import edu.esprit.lib.persistence.annotation.DAO;

@DAO(targetEntity = User.class)
public class UserDAO extends AbstractDAO<User> {
}
