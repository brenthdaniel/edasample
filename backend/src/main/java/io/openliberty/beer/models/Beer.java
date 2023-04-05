package io.openliberty.beer.models;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "Beer")
@NamedQuery(name = "Beer.findAll", query = "SELECT b FROM Beer b")
@NamedQuery(name = "Beer.findBeer", query = "SELECT b FROM Beer b WHERE b.name = :name")
@NamedQuery(name = "Beer.findCheckins", query = "SELECT c FROM Checkin c WHERE c.beer.id = :id")
public class Beer implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "beerId")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "brewery")
    private String breweryName;

    public Beer() {

    }

    public Beer(String breweryName, String beerName) {
        this.breweryName = breweryName;
        this.name = beerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreweryName() {
        return this.breweryName;
    }

    public void setBreweryName(String name) {
        this.breweryName = name;
    }
}