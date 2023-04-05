package io.openliberty.beer.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.FormParam;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import io.openliberty.beer.persistence.BeerAccess;
import io.openliberty.beer.models.User;

@RequestScoped
@Path("users")
public class UserResource {
    @Inject
    private BeerAccess beerList;


    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addNewUser(@FormParam("name") String userName, @FormParam("password") String password) {

        if ( userName == null ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User name was null").build();
        }

        User user = new User(userName, password);

        if ( !beerList.findUserByName(userName).isEmpty() ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User already exists").build();
        }

        beerList.createUser(user);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonArray getUsers() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder finalArray = Json.createArrayBuilder();
        for (User user : beerList.getAllUsers()) {
            if ( user.getName() != null ) {
            builder.add("name", user.getName())
                   .add("id", user.getId())
                   .add("checkins", user.checkins.size());
            finalArray.add(builder.build());
            }
        }
        return finalArray.build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") int id) {
        User user = beerList.getUser(id);
    
        if ( user == null ) {
            return Response.status(Response.Status.NOT_FOUND).entity("User does not exist").build();
        }
        beerList.deleteUser(user);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonObject getUser(@PathParam("id") int id) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        User user = beerList.getUser(id);
        if (user != null) {
            builder.add("name", user.getName())
                   .add("id", user.getId())
                   .add("checkins", user.getCheckins().size());
        }
        return builder.build();
    }

}
