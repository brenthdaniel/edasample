package io.openliberty.beer.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.PathParam;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

import io.openliberty.beer.persistence.BeerAccess;
import io.openliberty.beer.models.Beer;

@RequestScoped
@Path("beers")
public class BeerResource {
    @Inject
    private BeerAccess beerList;


    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response addNewBeer(@FormParam("breweryName") String breweryName, @FormParam("name") String name) {
        Beer newBeer = new Beer(breweryName, name);

        if ( !beerList.findBeerByName(name).isEmpty() ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Beer already exists").build();
        }

        beerList.createBeer(newBeer);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response updateBeer(@PathParam("id") int id, @FormParam("breweryName") String breweryName, @FormParam("name") String name) {
        Beer beer = beerList.getBeer(id);
        if ( beer == null ) {
            return Response.status(Response.Status.NOT_FOUND).entity("Beer does not exist").build();
        }
        beer.setName(name);
        beer.setBreweryName(breweryName);
        beerList.updateBeer(beer);
        return Response.status(Response.Status.NO_CONTENT).build();
      
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteBeer(@PathParam("id") int id) {
        Beer beer = beerList.getBeer(id);
        if ( beer == null ) {
            return Response.status(Response.Status.NOT_FOUND).entity("Beer does not exist").build();
        }
        beerList.deleteBeer(beer);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

       /**
     * This method returns a specific existing/stored beer in Json format
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonObject getBeer(@PathParam("id") int beerId) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Beer beer = beerList.getBeer(beerId);
        if (beer != null) {
            builder.add("name", beer.getName()).add("breweryName", beer.getBreweryName()).add("id", beer.getId());
        }
        return builder.build();
    }

    /**
     * This method returns the existing/stored beers in Json format
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonArray getBeers() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder finalArray = Json.createArrayBuilder();
        for (Beer beer : beerList.getAllBeers()) {
            if ( beer.getName() == null || beer.getBreweryName() == null ) {
                continue;
            }
            builder.add("name", beer.getName()).add("breweryName", beer.getBreweryName()).add("id", beer.getId());
            finalArray.add(builder.build());
        }
        return finalArray.build();
    }


}
