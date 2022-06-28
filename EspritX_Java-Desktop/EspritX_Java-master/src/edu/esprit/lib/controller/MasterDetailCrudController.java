package edu.esprit.lib.controller;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.model.DefaultElementProvider;
import edu.esprit.lib.fx.TableViewBuilder;
import edu.esprit.lib.persistence.AbstractDAO;
import edu.esprit.lib.persistence.IdentifiableEntity;
import edu.esprit.utils.fxml.FXUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class MasterDetailCrudController<Entity extends IdentifiableEntity, DAO extends AbstractDAO<Entity>> {
    protected DAO dao;
    protected Class<Entity> entityClass;
    protected Class<DAO> daoClass;
    private ObservableList<Entity> list;

    public MasterDetailCrudController() {
        Type genericSuperClass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperClass;
        entityClass = (Class<Entity>) parameterizedType.getActualTypeArguments()[0];
        daoClass = (Class<DAO>) parameterizedType.getActualTypeArguments()[1];
        try {
            dao = daoClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @FXML
    public AnchorPane masterView = new AnchorPane();

    @FXML
    public AnchorPane detailView = new AnchorPane();

    @FXML
    public TableView<Entity> tableView;

    @FXML
    public Button submitButton;

    @FXML
    public Button deleteButton;

    @FXML
    public Button resetButton;


    public void initialize() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        tableView = TableViewBuilder.makeForEntity(entityClass);
        list = FXCollections.observableList(dao.findAll());
        tableView.setItems(list);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayDetail(newValue);
            }
        });
        masterView.getChildren().add(tableView);
        FXUtils.SetAllAnchors(tableView, 0.0);
        displayDetail((Entity) entityClass.getDeclaredConstructors()[0].newInstance());
        Object t = new DefaultElementProvider();
    }


    void displayDetail(Entity entity) {
        FXForm fxForm = new FXFormBuilder()
                .buffered(true, true)
                .build();
        fxForm.setSource(entity);
        fxForm.setTitle(entity.getClass().getSimpleName() + " Details");

        submitButton.setOnAction(event -> {
            if (FXUtils.AlertHelper(Alert.AlertType.CONFIRMATION,
                    "Confirmation",
                    "Commit changes?",
                    "This operation will push changes to the database. This operation cannot be undone.")
                    .showAndWait().equals(Optional.of(ButtonType.OK))) {
                boolean committed = fxForm.commit();
                if (committed) {
                    boolean isNew = !entity.getPersisted();
                    dao.persist(entity);
                    if (isNew)
                        tableView.getItems().add(entity);
                    tableView.refresh();
                    FXUtils.AlertHelper(Alert.AlertType.INFORMATION, "Information", "Changes saved", "Operation completed successfully.").show();
                } else {
                    FXUtils.AlertHelper(Alert.AlertType.ERROR, "Error", "Changes not saved", "Operation failed. Please check for errors.").show();
                }
            }
        });

        deleteButton.setOnAction(event -> {
            if (!entity.getPersisted()) {
                FXUtils.AlertHelper(Alert.AlertType.ERROR, "Error", "Cannot delete", "This element is not saved yet.").show();
                return;
            }
            if (FXUtils.AlertHelper(Alert.AlertType.CONFIRMATION,
                    "Confirmation",
                    "Delete " + entity.getClass().getSimpleName() + "?",
                    "This operation will push changes to the database. This operation cannot be undone.")
                    .showAndWait().equals(Optional.of(ButtonType.OK))) {
                list.remove(entity);
                tableView.getItems().remove(entity);
                dao.delete(entity);
                tableView.refresh();
                FXUtils.AlertHelper(Alert.AlertType.INFORMATION, "Information", entity.getClass().getSimpleName() + " deleted.", "Operation completed successfully.").show();
                try {
                    displayDetail((Entity) entityClass.getDeclaredConstructors()[0].newInstance());
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        resetButton.setOnAction(event -> {
            if (FXUtils.AlertHelper(Alert.AlertType.CONFIRMATION,
                    "Confirmation",
                    "Do you want to discard your changes?",
                    "Any unsaved changes will be lost. Are you sure you want to discard your changes?")
                    .showAndWait().equals(Optional.of(ButtonType.OK))) {
                fxForm.reload();
            }
        });

        detailView.getChildren().clear();
        detailView.getChildren().add(fxForm);
        FXUtils.SetAllAnchors(fxForm, 0.0);
    }
}
