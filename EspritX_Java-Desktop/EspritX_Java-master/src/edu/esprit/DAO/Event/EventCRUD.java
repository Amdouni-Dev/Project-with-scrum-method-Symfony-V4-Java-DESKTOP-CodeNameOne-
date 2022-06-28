/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.esprit.DAO.Event;
import edu.esprit.DAO.UserDAO;
import edu.esprit.entities.Event;
import edu.esprit.utils.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 *
 * @author Mohzsen
 */
public class EventCRUD {
    edu.esprit.DAO.UserDAO UserDAO = new UserDAO();
    public List<Event> showAll() throws ParseException {
        List<Event> list = new ArrayList<>();

        String requete = "SELECT * FROM Event";
        try {
            Statement pst = DataSource.getConnection().createStatement();
            ResultSet rs = pst.executeQuery(requete);
            while (rs.next()) {

                Event e = new Event();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

                e.setId(rs.getInt("id"));
                e.setOwner(UserDAO.findById(rs.getInt("user_id")));
                e.setDescription(rs.getString("description"));
                e.setTitle(rs.getString("title"));
                e.setStart(df.parse(rs.getObject("start").toString()));
                e.setEnd(df.parse(rs.getObject("end").toString()));
                e.setAllDay(rs.getBoolean("all_day"));

                list.add(e);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return list;
    }
    
    public void addEvent(Event e) {
        String requete = "INSERT INTO event (title,description,start,end,all_day,user_id) VALUES (?,?,?,?,?,?)";
        try {

            PreparedStatement pst = DataSource.getConnection().prepareStatement(requete);

            pst.setString(1, e.getTitle());
            pst.setString(2, e.getDescription());
            pst.setObject(3, e.getStart());
            pst.setObject(4, e.getEnd());
            pst.setBoolean(5, e.isAllDay());
            pst.setInt(6, e.getOwner().getId());

            pst.executeUpdate();

            System.out.println("Event  added !");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void updateEvent(int id, Event e) {

        String req = "UPDATE  event SET title=?,description=?,start=?,end=? where id = " + id;
        try {
            PreparedStatement pst = DataSource.getConnection().prepareStatement(req);

            pst.setString(1, e.getTitle());
            pst.setString(2, e.getDescription());
            pst.setObject(3, e.getStart());
            pst.setObject(4, e.getEnd());
            //System.out.println("----   " +req);
            pst.executeUpdate();
            System.out.println("Event updated");
        } catch (SQLException ex) {
            System.out.println("SQLException " + ex.getMessage());
        }

    }

    public void deleteEvent(int id) {

        String req = "delete from event where id = " + id;
        try {
            Statement st = DataSource.getConnection().createStatement();
            if (st.executeUpdate(req) != 0) {
                st.executeUpdate(req);
                System.out.println(st.executeUpdate(req));
                System.out.println("Event deleted");
            } else {
                System.out.println("invalid id");
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }

    }
    
    public void reminder() throws ParseException{
        Timer timer = new Timer();
        List<Event> events = this.showAll();
        for (Event c : events){
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {                   
                  java.awt.Toolkit.getDefaultToolkit().beep();
                    System.out.println(c.getTitle()+" is here");
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                         System.out.println("Thread interrupted.");
                    }
                    System.out.println("Thread exiting.");
                }
            };
            if(c.getStart().after(new Date())){
                timer.schedule(timerTask,c.getStart());
            }
        }

    }
}
