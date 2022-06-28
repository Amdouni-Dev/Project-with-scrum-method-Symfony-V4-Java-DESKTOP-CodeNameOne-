package edu.esprit.controller;


import edu.esprit.DAO.Chat.ChatCrud;
import edu.esprit.DAO.UserDAO;
import edu.esprit.entities.Channel;
import edu.esprit.entities.Message;
import edu.esprit.entities.User;
import edu.esprit.gui.MainView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private VBox WithinMessage;

    @FXML
    private VBox ChannelContainer;

    @FXML
    private ImageView testchatis;

    @FXML
    private Label TextPlaceholder;

    @FXML
    private TextField SendMessageField;

    @FXML
    private ImageView SendMessageButt;

    int i = 0;
    Channel CurrentConver = new Channel(0);
    List<Message> CurrentMessages = new ArrayList<>();
    User currentUser = new User();

    public void loadnewConver() {

    }

    public void refresh(Channel c1) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainView.class.getResource("/Chat/Chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("EspritX");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/icon.png"))));
        stage.setWidth(1030);
        stage.setHeight(740);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserDAO UserDAO = new UserDAO();
        currentUser = UserDAO.findById(3312);
        List<Channel> Concernedchannels = new ArrayList<>();
        List<Channel> ALLChannels = new ArrayList<>();
        ChatCrud cc = new ChatCrud();
        ALLChannels = cc.ReadChannels();
        for (Channel ctemp : ALLChannels) {
            if (ctemp.participantsProperty().get(0).getId() == currentUser.getId()) {
                Concernedchannels.add(ctemp);
            }
            if (ctemp.participantsProperty().get(1).getId() == currentUser.getId()) {
                Concernedchannels.add(ctemp);
            }

        }

        CurrentConver = Concernedchannels.get(0);
        Integer Length = Concernedchannels.size();
        Node[] nodes = new Node[Length];
        for (i = 0; i < nodes.length; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Chat/Cell.fxml")
                );
                nodes[i] = loader.load();
                CellController c1 = loader.getController();
                if (Concernedchannels.get(i).participantsProperty().get(0).getId() == currentUser.getId()) {
                    c1.initData(Concernedchannels.get(i).participantsProperty().get(1).getFirstName(), Concernedchannels.get(i).getId());
                }
                if (Concernedchannels.get(i).participantsProperty().get(1).getId() == currentUser.getId()) {
                    c1.initData(Concernedchannels.get(i).participantsProperty().get(0).getFirstName(), Concernedchannels.get(i).getId());
                }
                /*nodes[i].onD*/
                // on double click on the cell
                nodes[i].setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        cc.DeleteChannel(CurrentConver);

                    } else {
                        System.out.println("On a pointé sur : //" + c1.GetConversation());
                        WithinMessage.getChildren().clear();
                        CurrentConver = cc.FindChannelbyId((Integer.parseInt(c1.GetConversation())));
                        CurrentMessages = cc.ReadMessagesChannel(CurrentConver);
                        Node[] Messagenodes = new Node[CurrentMessages.size()];
                        for (i = 0; i < Messagenodes.length; i++) {
                            if (CurrentMessages.get(i).getAuthor().getId() == currentUser.getId()) {
                                try {
                                    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/Chat/OutGoingCell.fxml")
                                    );
                                    Messagenodes[i] = loader2.load();
                                    OutGoingCellController c2 = loader2.getController();
                                    c2.setContent(CurrentMessages.get(i).getContent());
                                    WithinMessage.getChildren().add(Messagenodes[i]);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/Chat/IncomingCell.fxml")
                                    );
                                    Messagenodes[i] = loader2.load();
                                    IncomingCellController c2 = loader2.getController();
                                    c2.setContent(CurrentMessages.get(i).getContent());
                                    WithinMessage.getChildren().add(Messagenodes[i]);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                SendMessageButt.setOnMouseClicked(mouseEvent -> {
                    Message m1 = new Message();
                    m1.setContent(SendMessageField.getText());
                    m1.setCurrentConversation(CurrentConver);
                    m1.setAuthor(currentUser);
                    cc.CreateMessage(m1);
                    WithinMessage.getChildren().clear();
                    CurrentMessages = cc.ReadMessagesChannel(CurrentConver);
                    Node[] Messagenodes = new Node[CurrentMessages.size()];
                    for (i = 0; i < Messagenodes.length; i++) {
                        if (CurrentMessages.get(i).getAuthor().getId() == currentUser.getId()) {
                            try {
                                FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/Chat/OutGoingCell.fxml")
                                );
                                Messagenodes[i] = loader2.load();
                                OutGoingCellController c2 = loader2.getController();
                                c2.setContent(CurrentMessages.get(i).getContent());
                                WithinMessage.getChildren().add(Messagenodes[i]);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/Chat/IncomingCell.fxml")
                                );
                                Messagenodes[i] = loader2.load();
                                IncomingCellController c2 = loader2.getController();
                                c2.setContent(CurrentMessages.get(i).getContent());
                                WithinMessage.getChildren().add(Messagenodes[i]);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    SendMessageField.setText("");
                });
                ChannelContainer.getChildren().add(nodes[i]);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CurrentMessages = cc.ReadMessagesChannel(CurrentConver);
        Node[] Messagenodes = new Node[CurrentMessages.size()];
        for (i = 0; i < Messagenodes.length; i++) {
            if (CurrentMessages.get(i).getAuthor().getId() == currentUser.getId()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Chat/OutGoingCell.fxml")
                    );
                    Messagenodes[i] = loader.load();
                    OutGoingCellController c1 = loader.getController();
                    c1.setContent(CurrentMessages.get(i).getContent());
                    WithinMessage.getChildren().add(Messagenodes[i]);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Chat/IncomingCell.fxml")
                    );
                    Messagenodes[i] = loader.load();
                    IncomingCellController c1 = loader.getController();
                    c1.setContent(CurrentMessages.get(i).getContent());
                    WithinMessage.getChildren().add(Messagenodes[i]);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
