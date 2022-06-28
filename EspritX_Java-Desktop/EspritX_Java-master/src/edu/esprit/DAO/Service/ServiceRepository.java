package edu.esprit.DAO.Service;

import edu.esprit.entities.Group;
import edu.esprit.entities.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceRepository {

    ServiceCRUD SCRUD = new ServiceCRUD();
    List<Service> ListeService = SCRUD.ReadServices();

    public Service findById(Integer id) {
        for (Service S : ListeService) {
            if (S.idProperty().get() == id)
                return S;
        }
        return null;
    }

    public Service findByName(String str) {
        for (Service S : ListeService) {
            if (S.nameProperty().get().equals(str))
                return S;
        }
        return null;
    }

    public List<Service> findByResponsible(Group g) {
        List<Service> Liste = new ArrayList<Service>();
        for (Service S : ListeService) {
            if (S.responsibleProperty().get().equals(g)) {
                Liste.add(S);
            }
        }
        return Liste;
    }

    public List<Service> findByRecpient(Group g) {
        List<Service> Liste = new ArrayList<Service>();
        for (Service S : ListeService) {
            if (S.recipientsProperty().get().contains(g)) {
                Liste.add(S);
            }
        }
        return Liste;
    }

}
