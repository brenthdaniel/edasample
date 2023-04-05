package io.openliberty.beer.client;

import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.BadRequestException;

@Dependent
@RegisterRestClient
@RegisterProvider(UnknownUrlExceptionMapper.class)
@RegisterProvider(BadRequestExceptionMapper.class)
@Path("/login")
public interface LoginClient {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    void validate(@FormParam("name") String name, @FormParam("password") String password) throws
        UnknownUrlException, BadRequestException;


    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    void logout(@QueryParam("name") String name); 
    
}
