package edu.esprit.controller.Template;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    public Circle Avatar;
    @FXML
    private ImageView calendar;
    @FXML
    private ImageView search;
    @FXML
    private ImageView notif;
    @FXML
    private ImageView theme;
    @FXML
    private ImageView convo;
    @FXML
    private ImageView home;
    @FXML
    private ImageView logo;

    public Boolean LightTheme = false;
    @FXML
    private AnchorPane principal;
    @FXML
    private VBox topBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image icoAv = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/avatar-s-11.jpg")));
        Avatar.setFill(new ImagePattern(icoAv));
        Image icoCalendar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/calendar.png")));
        Image icoSearch = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/search.png")));
        Image icoNotif = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/notification.png")));
        Image icoTheme = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/sun.png")));
        Image icoConvo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/conversation.png")));
        Image icoHome = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/home.png")));
        Image icoLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/logo-light.png")));
        calendar.setImage(icoCalendar);
        search.setImage(icoSearch);
        notif.setImage(icoNotif);
        theme.setImage(icoTheme);
        convo.setImage(icoConvo);
        home.setImage(icoHome);
        logo.setImage(icoLogo);
    }

    @FXML
    public void changetheme() {
        this.LightTheme = !LightTheme;
        Image icoCalendar;
        Image icoSearch;
        Image icoNotif;
        Image icoTheme;
        Image icoConvo;
        Image icoHome;
        Image icoLogo;
        if (!LightTheme) {
            principal.setStyle("-fx-background-color: #171e31;");
            topBar.setStyle("-fx-background-color: #283046;");
            icoCalendar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/calendar.png")));
            icoSearch = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/search.png")));
            icoNotif = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/notification.png")));
            icoTheme = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/sun.png")));
            icoConvo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/conversation.png")));
            icoHome = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/home.png")));
            icoLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/logo-light.png")));
        } else {
            principal.setStyle("-fx-background-color: #FFFFFF;");
            topBar.setStyle("-fx-background-color: #F8F8F8;");
            icoCalendar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/calendar-dark.png")));
            icoSearch = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/search-dark.png")));
            icoNotif = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/notification-dark.png")));
            icoTheme = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/moon.png")));
            icoConvo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/conversation-dark.png")));
            icoHome = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/home-dark.png")));
            icoLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/icon.png")));
        }
        calendar.setImage(icoCalendar);
        search.setImage(icoSearch);
        notif.setImage(icoNotif);
        theme.setImage(icoTheme);
        convo.setImage(icoConvo);
        home.setImage(icoHome);
        logo.setImage(icoLogo);

    }


}
