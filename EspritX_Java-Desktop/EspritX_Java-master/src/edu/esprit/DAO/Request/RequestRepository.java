package edu.esprit.DAO.Request;

import edu.esprit.enums.ReqStatus;
import edu.esprit.entities.Request;
import edu.esprit.entities.Service;
import edu.esprit.entities.User;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class RequestRepository {

    RequestCRUD RCRUD = new RequestCRUD();
    List<Request> RequestsList = RCRUD.ReadRequests();

    public Request findById(Integer id) {
        Optional<Request> R = RequestsList.stream()
                .filter(request -> request.idProperty().get() == id)
                .reduce((a, b) -> null);
        if (R.isEmpty())
            throw new NoSuchElementException();
        else return R.get();
    }

    public Request findByTitle(String str) {
        Optional<Request> R = RequestsList.stream()
                .filter(request -> request.titleProperty().get().equals(str))
                .reduce((a, b) -> null);
        if (R.isEmpty())
            throw new NoSuchElementException();
        else return R.get();
    }

    public List<Request> findByService(Service S) {
        return RequestsList.stream()
                .filter(request -> request.typeProperty().get().equals(S))
                .collect(Collectors.toList());

    }

    public List<Request> findByUser(User U) {
        return RequestsList.stream()
                .filter(request -> request.requesterProperty().get().equals(U))
                .collect(Collectors.toList());
    }

    public List<Request> findByCreation(Date d) {
        return RequestsList.stream()
                .filter(request -> request.created_atProperty().get().equals(d))
                .collect(Collectors.toList());
    }

    public List<Request> findByStatus(ReqStatus status) {
        return RequestsList.stream()
                .filter(request -> request.statusProperty().get().equals(status))
                .collect(Collectors.toList());
    }

    public List<Request> findByResponseDate(Date d) {
        return RequestsList.stream()
                .filter(request -> request.responded_atProperty().get().equals(d))
                .collect(Collectors.toList());
    }

}
