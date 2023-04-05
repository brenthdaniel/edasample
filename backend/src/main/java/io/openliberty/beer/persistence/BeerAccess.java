package io.openliberty.beer.persistence;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.PersistenceContext;
import io.openliberty.beer.models.Beer;
import io.openliberty.beer.models.Checkin;
import io.openliberty.beer.models.User;

@RequestScoped
public class BeerAccess {
    
    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    public void createBeer(Beer b) {
        em.persist(b);
    }

    public Beer getBeer(int beerId) {
        return em.find(Beer.class, beerId);
    }

    public List<Beer> findBeerByName(String name) {
        return em.createNamedQuery("Beer.findBeer", Beer.class).setParameter("name", name).getResultList();
    }
    public void updateBeer(Beer b) {
        em.merge(b);
    }

    public void deleteBeer(Beer b) {
        em.remove(b);
    }

    public List<Beer> getAllBeers() {
        return em.createNamedQuery("Beer.findAll", Beer.class).getResultList();
    }

    public void createUser(User u) {
        em.persist(u);
    }

    public List<User> getAllUsers() {
        return em.createNamedQuery("User.findAll", User.class).getResultList();
    }

    public List<User> findUserByName(String name) {
        return em.createNamedQuery("User.findUser", User.class).setParameter("name", name).getResultList();
    }

    public void deleteUser(User u) {
        em.remove(u);
    }

    public User getUser(int id) {
        return em.find(User.class, id);
    }

    public void createCheckin(Checkin c) {
        em.persist(c);
        em.refresh(c.getUser());
    }

    public List<Checkin> getCheckins(Beer beer) {
       return em.createNamedQuery("Beer.findCheckins", Checkin.class).setParameter("id", beer.getId()).getResultList();
    }

    public List<Checkin> getCheckins() {
        return em.createNamedQuery("Checkin.findAll", Checkin.class).getResultList();
    }
}
