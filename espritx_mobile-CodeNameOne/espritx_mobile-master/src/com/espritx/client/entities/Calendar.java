package com.espritx.client.entities;

import java.util.Date;

public class Calendar {
    private int id;
    private Date start;
    private Date end;
    private String title;
    private String description;
    private boolean allDay;
    private int userId;
    private String firstname;
    private String lastname;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Calendar() {}

    public Calendar(int id, Date start, Date end, String title, String description, boolean allDay, int userId, String firstname, String lastname) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.allDay = allDay;
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Calendar(Date start, Date end, String title, String description, boolean allDay) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.allDay = allDay;
    }

    public Calendar(int id, Date start, Date end, String title, String description, boolean allDay) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.allDay = allDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    @Override
    public String toString() {
        return "Event { " +
                "id = " + id +
                ", start = " + start +
                ", end = " + end +
                ", title = '" + title + '\'' +
                ", description = '" + description + '\'' +
                ", allDay = " + allDay +
                '}';
    }
}
