/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espritx.client.services.serviceCalendar;

import com.codename1.components.ToastBar;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.DateFormat;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.events.ActionListener;
import com.codename1.util.DateUtil;
import com.espritx.client.entities.Calendar;
import com.espritx.client.utils.Statics;

import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 * @author Mohzsen
 */
public class ServiceCalendar {

    public ConnectionRequest req;
    public boolean resultOK;
    public ArrayList<Calendar> calendar;
    private static ServiceCalendar instance;

    private ServiceCalendar() {
        req = new ConnectionRequest();
    }

    public static ServiceCalendar getInstance() {
        if (instance == null) {
            instance = new ServiceCalendar();
        }
        return instance;
    }

    public boolean addEvent(Calendar calendar) {
        String url = Statics.BASE_URL + "/addEvent";
        req.setUrl(url);
        req.setPost(false);
        req.addArgument("start", calendar.getStart().toString());
        req.addArgument("end", calendar.getEnd().toString());
        req.addArgument("title", calendar.getTitle());
        req.addArgument("description", calendar.getDescription());
        req.addArgument("allDay", calendar.isAllDay() + "");

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResposeCode() == 200;
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;

    }

    public ArrayList<Calendar> getEventByDate(String date) {

        req = new ConnectionRequest();
        String url = Statics.BASE_URL + "/events/" + date;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
                                    @Override
                                    public void actionPerformed(NetworkEvent evt) {
                                        calendar = parseEvent(new String(req.getResponseData()));
                                        req.removeResponseListener(this);
                                    }
                                }
        );
        NetworkManager.getInstance().addToQueueAndWait(req);
        return calendar;
    }

    public ArrayList<Calendar> getAllEvents() {
        req = new ConnectionRequest();
        String url = Statics.BASE_URL + "/events";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
                                    @Override
                                    public void actionPerformed(NetworkEvent evt) {
                                        calendar = parseEvent(new String(req.getResponseData()));
                                        req.removeResponseListener(this);
                                    }
                                }
        );
        NetworkManager.getInstance().addToQueueAndWait(req);
        return calendar;
    }

    private ArrayList<Calendar> parseEvent(String jsonText) {
        calendar = new ArrayList<>();
        Timer timer = new Timer();
        JSONParser json = new JSONParser();
        try {
            Map<String, Object> eventslistJson = json.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            if (eventslistJson.size() == 0)
                ToastBar.showMessage("NO DATA FOR THIS DAY", FontImage.MATERIAL_DANGEROUS);
            else {
                List<Map<String, Object>> list = (List<Map<String, Object>>) eventslistJson.get("root");
                for (Map<String, Object> obj : list) {

                    Float id;
                    Boolean allDay = false;
                    Date start;
                    Date end;
                    String title;
                    String description;
                    String firstname;
                    String lastname;
                    Float userId;

                    if (obj.get("start") != null && obj.get("end") != null && obj.get("title").toString() != null && obj.get("description").toString() != null) {
                        start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.get("start").toString());
                        end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.get("end").toString());
                        title = obj.get("title").toString();
                        description = obj.get("description").toString();
                        id = Float.parseFloat(obj.get("id").toString());
                        userId = Float.parseFloat(obj.get("userId").toString());
                        firstname = obj.get("Userfirstname").toString();
                        lastname = obj.get("Userlastname").toString();
                        if ("true".equals(obj.get("allDay").toString())) {
                            allDay = true;
                        }
                        Calendar c = new Calendar(Math.round(id), start, end, title, description, allDay, Math.round(userId), firstname, lastname);
                        calendar.add(c);
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("iopException");
        } catch (ParseException ex) {
            System.err.println("erreur lors du cast du date");
        }
        return calendar;
    }

    public String convertt(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
       // System.out.println(sd.format(date));
        return sd.format(date);
        /*
        int year ;
        int day ;
        int month ;
        String chhar = null;
        int aam ;
        String yawmon = null;

        year = date.getYear();
        aam =  2000 + year % 100;
        month = date.getMonth();
        day = date.getDate();

        switch (month){
            case 0:
                chhar="01";
                break;
            case 1:
                chhar="02";
                break;
            case 2:
                chhar="03";
                break;
            case 3:
                chhar="04";
                break;
            case 4:
                chhar="05";
                break;
            case 5:
                chhar="06";
                break;
            case 6:
                chhar="07";
                break;
            case 7:
                chhar="08";
                break;
            case 8:
                chhar="09";
                break;
            case 9:
                chhar="10";
                break;
            case 10:
                chhar="11";
                break;
            case 11:
                chhar="12";
                break;

            default:
                break;
        }

        switch (day){
            case 1:
                yawmon="01";
                break;
            case 2:
                yawmon="02";
                break;
            case 3:
                yawmon="03";
                break;
            case 4:
                yawmon="04";
                break;
            case 5:
                yawmon="05";
                break;
            case 6:
                yawmon="06";
                break;
            case 7:
                yawmon="07";
                break;
            case 8:
                yawmon="08";
                break;
            case 9:
                yawmon="09";
                break;
              default:
                  yawmon=Integer.toString(day);
                break;
        }
        String e = aam+"-"+chhar+"-"+yawmon;
        return e;
         */
    }

    public boolean deleteEvent(int id) {
        String url = Statics.BASE_URL + "/events/delete/" + id;
        req = new ConnectionRequest();
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200;
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public boolean updateEvent(Calendar calendar) {
        String url = Statics.BASE_URL + "/events/update/" + calendar.getId();
        req = new ConnectionRequest();
        req.setUrl(url);
        req.addArgument("start", calendar.getStart().toString());
        req.addArgument("end", calendar.getEnd().toString());
        req.addArgument("title", calendar.getTitle());
        req.addArgument("description", calendar.getDescription());
        req.addArgument("allDay", calendar.isAllDay() + "");

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResposeCode() == 200;
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }
}
