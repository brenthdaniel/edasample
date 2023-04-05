package io.openliberty.beer.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import io.openliberty.beer.models.Beer;
import io.openliberty.beer.models.Checkin;
import io.openliberty.beer.models.User;
import io.openliberty.beer.persistence.BeerAccess;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.FormParam;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

@RequestScoped
@Path("checkins")
public class CheckinResource {
    @Inject
    private BeerAccess beerList;


    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addNewCheckin(@FormParam("beerID") int beerID, 
                                  @FormParam("userID") int userID,
                                  @FormParam("comments") String comments, 
                                  @FormParam("rating") int rating) {

        if ( beerID < 0 ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Beer ID is required").build();
        }

        Beer beer = beerList.getBeer(beerID);
        if ( beer == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Could not locate beer " + beerID).build();
        }

        User user = beerList.getUser(userID);
        if ( user == null ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Could not locate user " + userID).build();
        }
        
        Checkin c = new Checkin();
        c.setBeer(beer);
        c.setComments(comments);
        c.setLocalDateTime(LocalDateTime.now());
        c.setRating(rating);
        c.setUser(user);
        

        beerList.createCheckin(c);
        return Response.status(Response.Status.NO_CONTENT).build();
    }



    @GET
    @Path("{beerName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonArray getCheckinsForBeer(@PathParam("beerName") String beerName) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder finalArray = Json.createArrayBuilder();

        List<Beer> beer = beerList.findBeerByName(beerName);
        if ( beer.isEmpty() ) {
            return finalArray.build();
        }
        for (Checkin c : beerList.getCheckins(beer.get(0))) {
        
            builder.add("beerName", beerName)
                .add("rating", c.getRating())
                .add("checkinTime", c.getLocalDateTime().toString())
                .add("comments", c.getComments())
                .add("userName", c.getUser().getName());
            finalArray.add(builder.build());
            
        }
        return finalArray.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonArray getCheckins() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder finalArray = Json.createArrayBuilder();


        for (Checkin c : beerList.getCheckins()) {
        
            builder.add("beerName", c.getBeer().getName())
                .add("rating", c.getRating())
                .add("checkinTime", c.getLocalDateTime().toString())
                .add("comments", c.getComments())
                .add("userName", c.getUser().getName());
            finalArray.add(builder.build());
            
        }
        return finalArray.build();
    }
 
}
