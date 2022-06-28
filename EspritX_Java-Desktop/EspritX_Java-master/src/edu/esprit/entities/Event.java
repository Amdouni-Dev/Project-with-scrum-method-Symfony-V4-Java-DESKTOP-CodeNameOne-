/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.esprit.entities;

import edu.esprit.entities.User;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Mohzsen
 */
public class Event {
    private int id;
    private Date start;
    private Date end;
    private String title;
    private String description;
    private boolean allDay;
    private User owner;

    public Event() {
    }

    public Event(Date start, Date end, String title, String description, boolean allDay, User owner) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.allDay = allDay;
        this.owner = owner;
    }

    public Event(int id, Date start, Date end, String title, String description, boolean allDay, User owner) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.allDay = allDay;
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.start);
        hash = 97 * hash + Objects.hashCode(this.end);
        hash = 97 * hash + Objects.hashCode(this.title);
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + (this.allDay ? 1 : 0);
        hash = 97 * hash + this.owner.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Event other = (Event) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.allDay != other.allDay) {
            return false;
        }
        if (this.owner != other.owner) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.start, other.start)) {
            return false;
        }
        return Objects.equals(this.end, other.end);
    }

    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", start=" + start + ", end=" + end + ", title=" + title + ", description=" + description + ", allDay=" + allDay + ", owner=" + owner + '}';
    }
    
    
    
    
    

}
