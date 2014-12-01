package de.hsrm.swt02.restserver.resource;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;

@Path("resource")
public class UserResource {

    public static final Logic logic = ConstructionFactory.getLogic();
    public static final ServerPublisher publisher = ConstructionFactory.getPublisher();
    
    /**
     * 
     * @param username
     * @return the requested user
     * @throws UserNotExistentException 
     */
    @GET
    @Path("user/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getUser(@PathParam("username") String username) throws UserNotExistentException {
        System.out.println("GET -> " + username);
        ObjectMapper mapper = new ObjectMapper();
        User user = logic.getUser(username);
        String userAsString;
        try {
            userAsString = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            return Response.serverError().build();
        }
        return Response.ok(userAsString).build();
    }

    /**
     * 
     * receives a user and stores it into the database
     * 
     * @param receivedUser
     * @return 200 ok if successful
     */
    @POST
    @Path("user")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response saveUser(MultivaluedMap<String, String> formParams) {
        ObjectMapper mapper = new ObjectMapper();
        // TODO use logger
        String userAsString = formParams.get("data").get(0);
        System.out.println(userAsString);
        User user;
        try {
            user = mapper.readValue(userAsString, User.class);
        } catch (IOException e) {
            return Response.serverError().entity("Jackson parsing-error")
                    .build();
        }
        System.out.println("SEND -> " + user.getUsername());
        try {
            logic.addUser(user);
        } catch (UserAlreadyExistsException e) {
            return Response.serverError().entity("User already exists").build();
        }
        return Response.ok("User stored").build();
    }

    /**
     * 
     * @param user
     * @return 200 ok if successful
     */
    @PUT
    @Path("user/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response updateUser(@PathParam("username") String username,
            MultivaluedMap<String, String> formParams) {
        System.out.println("UPDATE -> " + username);
        ObjectMapper mapper = new ObjectMapper();
        String userAsString = formParams.get("data").get(0);
        User user;
        try {
            user = mapper.readValue(userAsString, User.class);
        } catch (IOException e) {
            return Response.serverError().entity("Jackson parsing-error")
                    .build();
        }
        logic.addUser(user);
        return Response.ok().build();
    }

    /**
     * 
     * @param username
     * @return deleted user, if successful
     */
    @DELETE
    @Path("user/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUser(@PathParam("username") String username) {
        System.out.println("DELETE -> " + username);
        ObjectMapper mapper = new ObjectMapper();
        User user = logic.getUser(username);
        logic.deleteUser(username);
        try {
            userAsString = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            return Response.serverError().entity("Jackson parsing-error")
                    .build();
        }
        return Response.ok(userAsString).build();
    }

}