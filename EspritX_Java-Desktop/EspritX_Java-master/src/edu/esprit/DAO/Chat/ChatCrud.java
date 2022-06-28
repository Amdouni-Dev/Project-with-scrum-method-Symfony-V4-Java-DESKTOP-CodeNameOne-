package edu.esprit.DAO.Chat;

import edu.esprit.DAO.UserDAO;
import edu.esprit.entities.Channel;
import edu.esprit.entities.Message;
import edu.esprit.entities.User;
import edu.esprit.utils.DataSource;
import javafx.collections.FXCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ChatCrud {

    public List<Channel> ReadChannels() {
        List<Channel> ListChannels = new ArrayList<Channel>();
        UserDAO UserDAO = new UserDAO();
        User u1=new User();
     /*   u1=UserDAO.findById(518);*/
        try {
        String req = "SELECT * FROM channel";
        Statement st = DataSource.getConnection().createStatement();
        ResultSet rsChannels = st.executeQuery(req);
          while (rsChannels.next()) {
              int currentid = rsChannels.getInt("id");
              String req2 = "SELECT * FROM channel_user WHERE channel_id=" + currentid;
              Statement st2 = DataSource.getConnection().createStatement();
              ResultSet rsUsers = st2.executeQuery(req2);
              Channel temporaryToadd = new Channel(currentid);
              List<User> Userstoadd = null;
              Userstoadd = new ArrayList<>();
              while (rsUsers.next()) {
                  Userstoadd.add(UserDAO.findById(rsUsers.getInt("user_id")));
              }
              temporaryToadd.setParticipants(FXCollections.observableArrayList(Userstoadd));
                ListChannels.add(temporaryToadd);
          }


        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return ListChannels;
    }

    public void CreateChannel(Channel c) {
        try {
            String req = "INSERT INTO channel (id,created_at,updated_at) VALUES (NULL, '2022-04-20 00:13:06.000000', '2022-04-20 00:13:06.000000')";
            PreparedStatement pst = DataSource.getConnection().prepareStatement(req);
            pst.executeUpdate();
          String req1 = "SELECT id FROM channel ORDER BY ID DESC LIMIT 1";
            Statement st = DataSource.getConnection().createStatement();
            ResultSet rs = st.executeQuery(req1);
            while (rs.next()) {
                c.idProperty().set(rs.getInt("id"));
            }

            for(User u :c.participantsProperty().get()){
                String req2 = "INSERT INTO channel_user VALUES (?,?)";
                PreparedStatement pst1 = DataSource.getConnection().prepareStatement(req2);
                pst1.setInt(1, c.idProperty().get());
                pst1.setInt(2, u.getId());
                pst1.executeUpdate();
            }
            System.out.println("Created Channel");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    public void DeleteChannel(Channel c) {
        try {
            Statement st = null;
            st = DataSource.getConnection().createStatement();
            String req1 = "DELETE FROM message WHERE channel_id=" + c.idProperty().get();
            String req3 = "DELETE FROM channel WHERE id=" + c.idProperty().get();
            st.executeUpdate(req1);
            st.executeUpdate(req3);
            System.out.println("Delted channel");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
    public Channel FindChannelbyId(int id) {
        UserDAO UserDAO = new UserDAO();
        User u1 = new User();
        Channel temporaryToadd = null;
        try {
            String req = "SELECT * FROM channel WHERE id=" + id;
            Statement st = DataSource.getConnection().createStatement();
            ResultSet rsChannels = st.executeQuery(req);
            temporaryToadd = null;
            while (rsChannels.next()) {
                int currentid = rsChannels.getInt("id");
                String req2 = "SELECT * FROM channel_user WHERE channel_id=" + currentid;
                Statement st2 = DataSource.getConnection().createStatement();
                ResultSet rsUsers = st2.executeQuery(req2);
                temporaryToadd = new Channel(currentid);
                List<User> Userstoadd = null;
                Userstoadd = new ArrayList<>();
                while (rsUsers.next()) {
                    Userstoadd.add(UserDAO.findById(rsUsers.getInt("user_id")));
                }
                temporaryToadd.setParticipants(FXCollections.observableArrayList(Userstoadd));

            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return temporaryToadd;
    }
    public List<Message> ReadMessagesChannel(Channel c1) {
        List<Message> ListMessages = new ArrayList<Message>();
        UserDAO UserDAO = new UserDAO();
        User u1=new User();
        /*   u1=UserDAO.findById(518);*/
        try {
            String req = "SELECT * FROM message WHERE channel_id=" + c1.idProperty().get();
            Statement st = DataSource.getConnection().createStatement();
            ResultSet rsMessage = st.executeQuery(req);
            while (rsMessage.next()) {
                int currentid = rsMessage.getInt("id");
            String currentContent=rsMessage.getString("content");
                int currentauthorid = rsMessage.getInt("author_id");
                int currentchannelid = rsMessage.getInt("channel_id");
Message TemporaryMessage=new Message();
                TemporaryMessage.setId(currentid);
                TemporaryMessage.setAuthor(UserDAO.findById(currentauthorid));
                TemporaryMessage.setCurrentConversation(this.FindChannelbyId(currentchannelid));
                TemporaryMessage.setContent(currentContent);
                ListMessages.add(TemporaryMessage);
            }

        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return ListMessages;
    }
    public void CreateMessage(Message c) {
        try {
            /*INSERT INTO `message` (`id`, `author_id`, `channel_id`, `content`, `created_at`, `updated_at`) VALUES (NULL, '429', '30', 'dqssqd', '2022-04-20 01:07:35.000000', '2022-04-20 01:07:35.000000');*/
            String req = "INSERT INTO message (id,author_id,channel_id,content,created_at,updated_at) VALUES (NULL,?,?,?, '2022-04-20 00:13:06.000000', '2022-04-20 00:13:06.000000')";
            PreparedStatement pst = DataSource.getConnection().prepareStatement(req);
            pst.setInt(1, c.authorProperty().get().getId());
            pst.setInt(2, c.currentConversationProperty().get().getId());
          pst.setString(3, c.contentProperty().get());
            pst.executeUpdate();
            System.out.println("Created Message");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    public void DeletMessage(Message m) {
        try {
            Statement st = null;
            st = DataSource.getConnection().createStatement();
            String req1 = "DELETE FROM message WHERE id=" + m.idProperty().get();
            st.executeUpdate(req1);
            System.out.println("Delted Message");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

}
