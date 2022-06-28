package edu.esprit.controller;

import edu.esprit.DAO.Request.RequestRepository;
import edu.esprit.DAO.Service.ServiceCRUD;
import edu.esprit.controller.Template.MainViewController;
import edu.esprit.entities.Group;
import edu.esprit.entities.Request;
import edu.esprit.entities.Service;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceController extends MainViewController {

    @javafx.fxml.FXML
    private ScrollPane show;
    @javafx.fxml.FXML
    private AnchorPane title;
    @javafx.fxml.FXML
    private ScrollPane items;
    @javafx.fxml.FXML
    private AnchorPane actions;
    @javafx.fxml.FXML
    private VBox pnl_scroll;
    @javafx.fxml.FXML
    private TableColumn<Service, String> SerResponsible;
    @javafx.fxml.FXML
    private TableColumn<Service, List<Group>> ServiceRecipients;
    @javafx.fxml.FXML
    private TableColumn<Service, String> servicetitle;
    @javafx.fxml.FXML
    private TableView<Service> serviceTable;
    @javafx.fxml.FXML
    private TableColumn<Service, Integer> serviceId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //super.initialize(url,resourceBundle);
        refresh_requests(null);
        ServiceCRUD SCRUD = new ServiceCRUD();
        List<Service> ListServices = SCRUD.ReadServices();
        ObservableList<Service> Services = FXCollections.observableList(ListServices);
        serviceId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        SerResponsible.setCellValueFactory(cellData -> cellData.getValue().responsibleProperty().get().displayNameProperty());

        ServiceRecipients.setCellValueFactory(
                ( TableColumn.CellDataFeatures<Service, List<Group>> S ) ->
                {
                    List<Group> groups = S.getValue().recipientsProperty();
                    return (ObservableValue<List<Group>>) groups;
                });
        ServiceRecipients.setCellFactory( col -> {
            ComboBox<Group> listView = new ComboBox<>();
            listView.setPromptText("See recipients");
            listView.setPrefWidth(130.0);
            listView.setCellFactory(lv -> new ListCell<Group>() {
                @Override
                public void updateItem(Group group, boolean empty) {
                    super.updateItem(group, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(group.getDisplayName());
                        setDisable(true);
                    }
                }
            });
            return new TableCell<Service, List<Group>>() {
                @Override
                public void updateItem(List<Group> groups, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        listView.getItems().setAll(groups);
                        setGraphic(listView);
                    }
                }
            };
        });
        servicetitle.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        serviceTable.setItems(Services);
        serviceTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    refresh_requests(newValue);
                    Stage stage = (Stage) serviceTable.getScene().getWindow();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(stage);
                    alert.setTitle("No Selection");
                    alert.setHeaderText("No Person Selected");
                    alert.setContentText("Please select a person in the table.");

                    alert.showAndWait();
                }
        );
    }

    public void refresh_requests(Service S) {
        pnl_scroll.getChildren().clear();
        show.setFitToWidth(true);
        RequestRepository rrepo = new RequestRepository();
        List<Request> requests = rrepo.findByService(S);
        Node[] nodes = new Node[100];
        for (int i = 0; i < requests.size(); i++) {
            try {
                Request req = requests.get(i);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Item.fxml"));
                RequestController controller = new RequestController(req);
                loader.setController(controller);
                nodes[i] = (Node) loader.load();
                pnl_scroll.getChildren().add(nodes[i]);
            } catch (IOException ex) {
                Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
