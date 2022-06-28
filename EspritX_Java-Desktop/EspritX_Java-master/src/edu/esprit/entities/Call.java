package edu.esprit.entities;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Call {
    private int id;
    private String title;
    private String description;
    private Date start;
    private Date end;
    private Boolean allday;
    private User owner;
    private User[] users;

    public Call() {
    }

    public Call(int id, String title, String description, Date start, Date end, Boolean allday, User owner, User[] users) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.allday = allday;
        this.owner = owner;
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Boolean getAllday() {
        return allday;
    }

    public void setAllday(Boolean allday) {
        this.allday = allday;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Call call)) return false;
        return id == call.id &&
                title.equals(call.title) &&
                description.equals(call.description) &&
                start.equals(call.start) &&
                end.equals(call.end) &&
                allday.equals(call.allday) &&
                owner.equals(call.owner) &&
                Arrays.equals(users, call.users);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, description, start, end, allday, owner);
        result = 31 * result + Arrays.hashCode(users);
        return result;
    }

    @Override
    public String toString() {
        return "Call { " +
                "id = " + id +
                ", title = '" + title + '\'' +
                ", description = '" + description + '\'' +
                ", start = " + start +
                ", end = " + end +
                ", allday = " + allday +
                ", owner = " + owner +
                ", users = " + Arrays.toString(users) +
                '}';
    }
}
