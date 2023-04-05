package io.openliberty.beer.test;

import java.util.HashMap;

import io.openliberty.beer.models.Beer;
import io.openliberty.beer.models.User;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;

public class BeerTest {
    private WebTarget webTarget;

    protected Form form;
    protected Client client;
    protected Response response;
    protected HashMap<String, String> beerForm;

    protected static String baseUrl;
    protected static String port;
    protected static final String BEERS = "beers";
    protected static final String USERS = "users";
    protected static final String JSONFIELD_NAME = "name";
    protected static final String JSONFIELD_BREWERY = "breweryName";
    protected static final String JSONFIELD_USERNAME = "name";
    protected static final String JSONFIELD_PASSWORD = "password";
    protected static final String JSONFIELD_CHECKIN_NUMBER = "checkins";

  
    protected Response createBeers(HashMap<String,String> formDataMap) {
        return postRequest(formDataMap, BEERS);
    }

    protected Response createUsers(HashMap<String,String> formDataMap) {
        return postRequest(formDataMap, USERS);
    }
    protected Response postRequest(HashMap<String, String> formDataMap, String path) {
        formDataMap.forEach((formField, data) -> {
            form.param(formField, data);
        });
        webTarget = client.target(baseUrl + path);
        response = webTarget.request().post(Entity.form(form));
        form = new Form();
        return response;
    }

    /**
     *  Makes a PUT request to the /beers/{beerId} endpoint
     */
    protected int updateRequest(HashMap<String, String> formDataMap, int beerId) {
        formDataMap.forEach((formField, data) -> {
            form.param(formField, data);
        });
        webTarget = client.target(baseUrl + BEERS + "/" + beerId);
        response = webTarget.request().put(Entity.form(form));
        form = new Form();
        return response.getStatus();
    }

    /**
     *  Makes a DELETE request to /beers/{beerId} endpoint and return the response
     *  code
     */
    protected Response deleteBeer(int beerId) {
        return deleteRequest(beerId, BEERS);
    }

    protected Response deleteUser(int userId) {
        return deleteRequest(userId, USERS);
    }

    protected Response deleteRequest(int id, String path) {
        webTarget = client.target(baseUrl + path + "/" + id);
        response = webTarget.request().delete();
        return response;
    }

    /**
     *  Makes a GET request to the /beers endpoint and returns result in a JsonArray
     */
    protected JsonArray getBeers() {
        webTarget = client.target(baseUrl + BEERS);
        response = webTarget.request().get();
        return response.readEntity(JsonArray.class);
    }

    protected JsonArray getUsers() {
        webTarget = client.target(baseUrl + USERS);
        response = webTarget.request().get();
        return response.readEntity(JsonArray.class);
    }
    /**
     *  Makes a GET request to the /beers/{beerId} endpoint and returns a JsonObject
     */
    protected JsonObject getIndividualBeer(int beerId) {
        webTarget = client.target(baseUrl + BEERS + "/" + beerId);
        response = webTarget.request().get();
        return response.readEntity(JsonObject.class);
    }

    protected JsonObject getIndividualUser(int userId) {
        webTarget = client.target(baseUrl + USERS + "/" + userId);
        response = webTarget.request().get();
        return response.readEntity(JsonObject.class);
    }

    /**
     *  Makes a GET request to the /beers endpoint and returns the beer provided
     *  if it exists.
     */
    protected JsonObject findBeer(Beer e) {
        JsonArray beers = getBeers();
        for (int i = 0; i < beers.size(); i++) {
            JsonObject testBeer = beers.getJsonObject(i);
            Beer test = new Beer(testBeer.getString(JSONFIELD_BREWERY),
                    testBeer.getString(JSONFIELD_NAME));
            if (compareBeers(test, e)) {
                return testBeer;
            }
        }
        return null;
    }

    protected JsonObject findUser(User u) {
        JsonArray users = getUsers();
        for (int i = 0; i < users.size(); i++) {
            JsonObject testUser = users.getJsonObject(i);
                  
            if ( u.getName().equals(testUser.getString(JSONFIELD_USERNAME)))
                return testUser;
        }
        return null;
    }



    private boolean compareBeers(Beer one, Beer two) {
        String name = one.getName();
        String breweryName = one.getBreweryName();
        if ( name == null ) {
            if (two.getName() != null ) 
                return false;
        }
        if ( breweryName == null ) {
            if ( two.getBreweryName() != null )
                return false;
        }

        return ( name.equals(two.getName()) && breweryName.equals(two.getBreweryName()));
    }


}