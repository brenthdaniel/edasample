// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.beer.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.BadRequestException;

import io.openliberty.beer.ui.facelets.PageDispatcher;
import io.openliberty.beer.client.BeerClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.openliberty.beer.client.UnknownUrlException;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlInputHidden;
import jakarta.faces.component.html.HtmlPanelGroup;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.annotation.ManagedProperty;

import java.io.Serializable;


@Named
@ViewScoped
public class BeerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String breweryName;
    private int selectedId;
    private boolean notValid;
    private ComponentSystemEvent currentComponent;

    @Inject
    @RestClient
    private BeerClient beerClient;

    @Inject
    @ManagedProperty(value = "#{pageDispatcher}")
    private PageDispatcher pageDispatcher;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setBreweryName(String name) {
        this.breweryName = name;
    }

    public String getBreweryName() {
        return this.breweryName;
    }

    public boolean getNotValid() {
        return notValid;
    }

    public PageDispatcher getPageDispatcher() {
        return this.pageDispatcher;
    }

    public void setPageDispatcher(PageDispatcher pageDispatcher) {
        this.pageDispatcher = pageDispatcher;
    }

    public void setCurrentComponent(ComponentSystemEvent component) {
        this.currentComponent = component;
    }

    /**
     * Set a selected beer id.
     */
    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    /**
     * Remove stored beer id.
     */
    public void removeSelectedId() {
        this.selectedId = -1;
    }

    /**
     * Submit beer form data to back end service.
     */
    public void submitToService() {
   
        try {
            beerClient.addBeer(breweryName, name);
            pageDispatcher.showMainPage();
            clear();
       } catch (UnknownUrlException e) {
            System.err.println("submitToService: The given URL is unreachable.");
        } catch (BadRequestException e) {
            displayInvalidBeerError();
        }

    }

    /**
     * Submit updated beer form data to back end service.
     */
    public void submitUpdateToService() {      
        try {
            beerClient.updateBeer(this.selectedId, this.breweryName, this.name);
            pageDispatcher.showMainPage();
            clear();
        } catch (UnknownUrlException e) {
            System.err.println("The given URL is unreachable");
        } catch (BadRequestException e) {
            displayInvalidBeerError();
        }
    }

    public void editBeer() {
        JsonObject beer = retrieveBeerByCurrentId(this.selectedId);    
        this.name = beer.getString("name");   
        this.breweryName = beer.getString("breweryName");  
        this.selectedId = beer.getInt("id");

        pageDispatcher.showEditPage();
    }

    /**
     * Delete beer form data to back end service.
     */
    public void submitDeletetoService() {
        try {
            beerClient.deleteBeer(this.selectedId);
        } catch (UnknownUrlException e) {
            System.err.println("The given URL is unreachable");
        }

        pageDispatcher.showMainPage();
    }

    /**
     * Retrieve the list of beers from back end service.
     */
    public JsonArray retrieveBeerList() {
        try {
            return beerClient.getBeers();
        } catch (UnknownUrlException e) {
            System.err.println("CAn't retrieve beer list. The given URL is unreachable.");
            return null;
        }
    }


    /**
     * Retrieve a selected beer by Id
     */
    public JsonObject retrieveBeerByCurrentId(int currentId) {
        try {
            return beerClient.getBeer(currentId);
        } catch (UnknownUrlException e) {
            System.err.println("The given URL is unreachable");
            return null;
        }
    }


    /**
     * Displays the error message if time is not valid or the beer already exists
     */
    public void displayError(boolean display) {
        notValid = display;
    }

    /**
     *  Method to clean the bean after form submission and before beer creation form
     */
    public void clear() {
        setName(null);
        setBreweryName(null);
  
    }



    /**
     * Adds "Choose a valid time" message after selectOptions in user interface.
     */
    private void addErrorMessage(ComponentSystemEvent event, String errorMessage) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(errorMessage);
        HtmlPanelGroup divEventTime = (HtmlPanelGroup) event.getComponent()
            .findComponent("beerform:name");
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

    /**
     *  Display error message if Event already exists and don't allow form submission
     */
    private void displayInvalidBeerError() {
        allowSubmission(currentComponent, false);
        addErrorMessage(currentComponent, "Beer already exists!");
        displayError(true);
    }
}
