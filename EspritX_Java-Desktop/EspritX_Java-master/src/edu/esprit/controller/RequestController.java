package edu.esprit.controller;

import edu.esprit.DAO.Request.RequestRepository;
import edu.esprit.DAO.Service.ServiceRepository;
import edu.esprit.entities.Request;
import edu.esprit.entities.Service;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class RequestController implements Initializable {

    @javafx.fxml.FXML
    private Label requester;
    @javafx.fxml.FXML
    private Label response;
    @javafx.fxml.FXML
    private Label year_creation;
    @javafx.fxml.FXML
    private Label name;
    @javafx.fxml.FXML
    private Label day_creation;
    @javafx.fxml.FXML
    private Label id;
    @javafx.fxml.FXML
    private Label type;
    @javafx.fxml.FXML
    private Circle requester_avatar;
    @javafx.fxml.FXML
    private Circle picstatus;
    @javafx.fxml.FXML
    private Label desc;
    @javafx.fxml.FXML
    private Label month_creation;
    @javafx.fxml.FXML
    private Label status;

    private Request r= new Request();

    public RequestController(Request request){
        r=request;
    }

    public void setRequest(Request r){
        requester.setText(r.requesterProperty().get().getFirstName()+" "+r.requesterProperty().get().getLastName());
        Image icoReq = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/avatar-s-11.jpg")));
        requester_avatar.setFill(new ImagePattern(icoReq));
        status.setText(r.statusProperty().get().toString());
        desc.setText(r.descriptionProperty().get());
        if (r.statusProperty().get().toString().equals("unseen"))
        picstatus.setFill(Color.GRAY);
        else if (r.statusProperty().get().toString().equals("denied"))
            picstatus.setFill(Color.RED);
        else if (r.statusProperty().get().toString().equals("processing"))
            picstatus.setFill(Color.YELLOW);
        else picstatus.setFill(Color.GREEN);
        id.setText(String.valueOf(r.idProperty().get()));
        name.setText(r.titleProperty().get());
        if (r.responded_atProperty().get()!=null)
            response.setText(r.responded_atProperty().get().toString());
        ServiceRepository SREPO = new ServiceRepository();
        Service S=SREPO.findById(r.typeProperty().get().getId());
        type.setText(S.nameProperty().get());
        LocalDate date= r.created_atProperty().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        day_creation.setText(String.valueOf(r.created_atProperty().get().getDate()));
        month_creation.setText(month);
        year_creation.setText(String.valueOf(r.created_atProperty().get().getYear()+1900));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setRequest(r);
    }
}
