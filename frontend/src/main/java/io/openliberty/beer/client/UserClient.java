package io.openliberty.beer.client;

import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
@Path("/users")
public interface UserClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    JsonArray getUsers() throws UnknownUrlException;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject getUser(@PathParam("id") int id) throws UnknownUrlException;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    void addUser(@FormParam("name") String name, @FormParam("password") String password) throws
        UnknownUrlException, BadRequestException;


}
