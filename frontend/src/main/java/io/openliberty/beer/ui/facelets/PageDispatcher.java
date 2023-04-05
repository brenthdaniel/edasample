package io.openliberty.beer.ui.facelets;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.annotation.ManagedProperty;

@Named
@SessionScoped
public class PageDispatcher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    @ManagedProperty(value = "#{pageLoader}")
    private PageLoader pageLoader;

    String currentRole = null;

    public PageLoader getPageLoader() {
        return pageLoader;
    }

    public void setPageLoader(PageLoader pageLoader) {
        this.pageLoader = pageLoader;
    }

    public void showBeerForm() {
        pageLoader.setContent("content/beerForm.xhtml");
        pageLoader.setCurrentPage("Beer Creation");
    }

    public void showMainPage() {
        pageLoader.setContent("content/mainPage.xhtml");
        pageLoader.setCurrentPage("Beers");
    }

    public void showEditPage() {
        pageLoader.setContent("content/updateBeerForm.xhtml");
        pageLoader.setCurrentPage("Edit Beer");
    }

    public void showCheckinForm() {
        pageLoader.setContent("content/checkinForm.xhtml");
        pageLoader.setCurrentPage("Checkin");
    }

    public void showCheckinsPage() {
        pageLoader.setContent("content/checkinsPage.xhtml");
        pageLoader.setCurrentPage("All Checkins");
    }

    public void showUsersPage() {
        pageLoader.setContent("content/users.xhtml");
        pageLoader.setCurrentPage("All Users");
    }

    public void showUsersForm() {
        pageLoader.setContent("content/createUser.xhtml");
        pageLoader.setCurrentPage("New User");
    }

}
