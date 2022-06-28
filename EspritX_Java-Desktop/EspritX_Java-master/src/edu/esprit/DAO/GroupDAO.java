package edu.esprit.DAO;

import edu.esprit.entities.Group;
import edu.esprit.lib.persistence.AbstractDAO;
import edu.esprit.lib.persistence.annotation.DAO;

@DAO(targetEntity = Group.class)
public class GroupDAO extends AbstractDAO<Group> {
}
