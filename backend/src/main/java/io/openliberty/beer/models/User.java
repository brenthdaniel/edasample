package io.openliberty.beer.models;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
@NamedQuery(name = "User.findUser", query = "SELECT u FROM User u WHERE u.name = :name")
public class User implements Serializable {


    public User() {

    }

    public User(String n, String p) {
        this.name = n;
        this.password = p;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "userID")
    private int id;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;
    
    @OneToMany(mappedBy="user", targetEntity = Checkin.class)
    public List<Checkin> checkins;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Checkin> getCheckins() {
        return this.checkins;
    }
}
