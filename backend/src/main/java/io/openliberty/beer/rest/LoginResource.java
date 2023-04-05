package io.openliberty.beer.rest;

import java.io.Serializable;
import java.util.List;

import io.openliberty.beer.models.User;
import io.openliberty.beer.persistence.BeerAccess;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("login")
public class LoginResource {

    @Inject
    private BeerAccess accessor;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response validate(@FormParam("name") String userName, @FormParam("password") String password)  {
        if ( userName == null || password == null ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User name or password was null").build();
        }

        List<User> users = accessor.findUserByName(userName);

        if ( users.isEmpty() ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User not found").build();
        }

        if ( password.equals(users.get(0).getPassword()))
            return Response.status(Response.Status.NO_CONTENT).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).entity("Password was incorrect").build();
    }    

    @GET
    @Path("/logout")
    public Response logout(@QueryParam("name") String name) {
        if ( name == null ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User name was null").build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();
        
    }
}
