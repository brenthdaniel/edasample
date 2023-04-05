package io.openliberty.beer.ui;

import java.io.Serializable;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.openliberty.beer.client.CheckinClient;
import io.openliberty.beer.client.UnknownUrlException;
import io.openliberty.beer.ui.facelets.PageDispatcher;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlInputHidden;
import jakarta.faces.component.html.HtmlPanelGroup;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonArray;
import jakarta.ws.rs.BadRequestException;

@Named
@ViewScoped
public class CheckinBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int beerID;
  

    private int userID;
    private int rating;
    private String comments; 
    private boolean notValid;
    private ComponentSystemEvent currentComponent;
    private int selectedId;
    
    @Inject
    @RestClient
    private CheckinClient beerClient;

    public ComponentSystemEvent getCurrentComponent() {
        return currentComponent;
    }

    public void setCurrentComponent(ComponentSystemEvent currentComponent) {
        this.currentComponent = currentComponent;
    }

    @Inject
    @ManagedProperty(value = "#{pageDispatcher}")
    private PageDispatcher pageDispatcher;

    public boolean isNotValid() {
        return notValid;
    }

    public void setNotValid(boolean notValid) {
        this.notValid = notValid;
    }

    public PageDispatcher getPageDispatcher() {
        return pageDispatcher;
    }

    public int getBeerID() {
        return beerID;
    }

    public void setBeerID(int beerID) {
        this.beerID = beerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setPageDispatcher(PageDispatcher pageDispatcher) {
        this.pageDispatcher = pageDispatcher;
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

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    public void removeSelectedId() {
        this.selectedId = -1;
    }

    public void submitToService() {
        try {
            beerClient.addCheckin(beerID, userID, rating, comments);
            pageDispatcher.showCheckinsPage();
            clear();
       } catch (UnknownUrlException e) {
            System.err.println("submitToService: The given URL is unreachable.");
        } catch (BadRequestException e) {
            displayInvalidBeerError();
        }
    }

    public void clear() {
        setComments(null);
        setBeerID(-1);
        setUserID(-1);
        setRating(-1);
    }

    public void displayError(boolean display) {
        this.notValid = display;
    }

    private void addErrorMessage(ComponentSystemEvent event, String errorMessage) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(errorMessage);
        HtmlPanelGroup divEventTime = (HtmlPanelGroup) event.getComponent()
            .findComponent("checkinform:beerName");
        context.addMessage(divEventTime.getClientId(context), message);
    }

    
    /**
     * Allow if user can submit or not.
     */
    private void allowSubmission(ComponentSystemEvent event, boolean allowSubmission) {
        UIComponent components = event.getComponent();
        HtmlInputHidden formInput = (HtmlInputHidden) components
            .findComponent("eventform:eventSubmit");
        formInput.setValid(allowSubmission);
    }

    private void displayInvalidBeerError() {
        allowSubmission(currentComponent, false);
        addErrorMessage(currentComponent, "Checkin can't be added");
        displayError(true);
    }

    public JsonArray retrieveCheckinList() {
        try {
            return beerClient.getCheckins();
        } catch (UnknownUrlException e) {
            System.err.println("Can't retrieve checkin list. The given URL is unreachable.");
            return null;
        }
    }
}
