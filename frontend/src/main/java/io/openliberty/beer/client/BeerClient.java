package io.openliberty.beer.client;

import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.BadRequestException;

import jakarta.json.JsonObject;
import jakarta.json.JsonArray;

@Dependent
@RegisterRestClient
@RegisterProvider(UnknownUrlExceptionMapper.class)
@RegisterProvider(BadRequestExceptionMapper.class)
@Path("/beers")
public interface BeerClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    JsonArray getBeers() throws UnknownUrlException;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject getBeer(@PathParam("id") int beerId) throws UnknownUrlException;

    @DELETE
    @Path("/{id}")
    void deleteBeer(@PathParam("id") int beerId) throws UnknownUrlException;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    void addBeer(@FormParam("breweryName") String breweryName, @FormParam("name") String name) throws
        UnknownUrlException, BadRequestException;

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    void updateBeer(@PathParam("id") int beerId, @FormParam("breweryName") String breweryName, @FormParam("name") String name) throws UnknownUrlException, BadRequestException;

}
