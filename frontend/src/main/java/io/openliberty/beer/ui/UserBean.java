package io.openliberty.beer.ui;

import java.io.Serializable;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.openliberty.beer.client.LoginClient;
import io.openliberty.beer.client.UnknownUrlException;
import io.openliberty.beer.client.UserClient;
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
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String password;
    private boolean notValid;
    private ComponentSystemEvent currentComponent;

    
    @Inject
    @RestClient
    private UserClient userClient;

    @Inject
    @RestClient
    private LoginClient loginClient;

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

    public void setPageDispatcher(PageDispatcher pageDispatcher) {
        this.pageDispatcher = pageDispatcher;
    }    

    public void validate() {
        try {
          loginClient.validate(name, password);
          pageDispatcher.showMainPage();
        clear();
        } catch (UnknownUrlException e ) {
            System.err.println("submitToService: The given URL is unreachable.");
        } catch (BadRequestException e) {
            displayInvalidBeerError();
        }
    }
    public void submitToService() {
        try {
            userClient.addUser(name, password);
            pageDispatcher.showUsersPage();
            clear();
       } catch (UnknownUrlException e) {
            System.err.println("submitToService: The given URL is unreachable.");
        } catch (BadRequestException e) {
            displayInvalidBeerError();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void clear() {
       setName(null);
       setPassword(null);
    }

    public void displayError(boolean display) {
        this.notValid = display;
    }

    private void addErrorMessage(ComponentSystemEvent event, String errorMessage) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(errorMessage);
        HtmlPanelGroup divEventTime = (HtmlPanelGroup) event.getComponent()
            .findComponent("userform:name");
        context.addMessage(divEventTime.getClientId(context), message);
    }

    
    /**
     * Allow if user can submit or not.
     */
    private void allowSubmission(ComponentSystemEvent event, boolean allowSubmission) {
        UIComponent components = event.getComponent();
        HtmlInputHidden formInput = (HtmlInputHidden) components
            .findComponent("userform:userSubmit");
        formInput.setValid(allowSubmission);
    }

    private void displayInvalidBeerError() {
        allowSubmission(currentComponent, false);
        addErrorMessage(currentComponent, "User can't be added");
        displayError(true);
    }

    public JsonArray getUsers() {
        try {
            return userClient.getUsers();
        } catch (UnknownUrlException e) {
            System.err.println("Can't retrieve user list. The given URL is unreachable.");
            return null;
        }
    }
 
}
