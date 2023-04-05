package io.openliberty.beer.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "checkins")
@NamedQuery(name = "Checkin.findAll", query = "SELECT c FROM Checkin c")
public class Checkin implements Serializable{
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "checkinId")
    private int id;

    @ManyToOne(optional = false)
    private Beer beer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userID", referencedColumnName="userID")
    private User user;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comments")
    private String comments;

    @Column(name = "checkinTime", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;   


    public Beer getBeer() {
        return beer;
    }

    public User getUser() {
        return user;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }


 
}
