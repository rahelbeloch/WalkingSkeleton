package de.hsrm.swt02.restserver.resource;

import java.io.IOException;
import java.util.logging.Level;

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
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.restserver.LogicResponse;
import de.hsrm.swt02.restserver.Message;

/**
 * This class is called if client requests user operations.
 * @author jvanh001
 *
 */
@Path("resource")
public class UserResource {

    public static final Logic LOGIC = ConstructionFactory.getLogic();
    public static final ServerPublisher PUBLISHER = ConstructionFactory.getPublisher();
    LogicResponse logicResponse;
    public static final UseLogger LOGGER = new UseLogger();
    
    /**
     * This method returns a requested user.
     * 
     * @param username indicates which user is looked for
     * @return the requested user
     * @throws UserNotExistentException 
     */
    @GET
    @Path("user/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getUser(@PathParam("username") String username) throws UserNotExistentException {
        final String loggingBody = "GETUSER -> " + username;
        final ObjectMapper mapper = new ObjectMapper();
        final User user = LOGIC.getUser(username);
        String userAsString;
        try {
            userAsString = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.INFO,loggingBody + " JACKSON parsing-error occured.");
            return Response.serverError().entity("11210").build();
        }
        LOGGER.log(Level.INFO,loggingBody + " Request successful.");
        return Response.ok(userAsString).build();
    }

    /**
     * 
     * receives a user and stores it into the database.
     * 
     * @param formParams is a wrapper for a sent user
     * @return 200 ok if successful
     */
    @POST
    @Path("user")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response saveUser(MultivaluedMap<String, String> formParams) {
        final ObjectMapper mapper = new ObjectMapper();
        final String loggingBody = "SENDUSER";
        final String userAsString = formParams.get("data").get(0);
        User user;
        try {
            user = mapper.readValue(userAsString, User.class);
        } catch (IOException e) {
            LOGGER.log(Level.INFO,loggingBody + " JACKSON parsing-error occured.");
            return Response.serverError().entity("11210")
                    .build();
        }
        try {
            logicResponse = LOGIC.addUser(user);
        } catch (UserAlreadyExistsException e) {
            LOGGER.log(Level.INFO,loggingBody + " User already exists.");
            return Response.serverError().entity("11220").build();
        }

        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, "Publisher not responding!");
            }
        }

        LOGGER.log(Level.INFO,loggingBody + " User successfully stored.");

        return Response.ok("User stored").build();
    }

    /**
     * This method updates a user.
     * @param username indicates which user should be updated
     * @param formParams is a wrapper of a sent user 
     * @return 200 ok if successful
     */
    @PUT
    @Path("user/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response updateUser(@PathParam("username") String username,
            MultivaluedMap<String, String> formParams) 
    {
        final String loggingBody = "UPDATE -> " + username;
        final ObjectMapper mapper = new ObjectMapper();
        final String userAsString = formParams.get("data").get(0);
        User user;
        try {
            user = mapper.readValue(userAsString, User.class);
        } catch (IOException e) {
            LOGGER.log(Level.INFO,loggingBody + " JACKSON parsing-error occured.");
            return Response.serverError().entity("11210")
                    .build();
        }
        try {
            logicResponse = LOGIC.addUser(user);
        } catch (UserAlreadyExistsException e) {
            LOGGER.log(Level.INFO,loggingBody + " User already exists.");
            return Response.serverError().entity("11220").build();
        }

        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, "Publisher not responding!");
            }
        }

        LOGGER.log(Level.INFO,loggingBody + " User successfully updated.");

        return Response.ok().build();
    }

    /**
     * This method deletes an user.
     * @param username indicates which user should be deleted
     * @return deleted user, if successful
     */
    @DELETE
    @Path("user/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUser(@PathParam("username") String username) {
        final String loggingBody = "DELETE -> " + username;
        final ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            user = LOGIC.getUser(username);
            logicResponse = LOGIC.deleteUser(username);
        } catch (UserNotExistentException e1) {
            LOGGER.log(Level.INFO,loggingBody + " User does not exist.");
            return Response.serverError().entity("11260").build();
        }
        String userAsString;
        try {
            userAsString = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.INFO,loggingBody + " JACKSON parsing-error occured.");
            return Response.serverError().entity("11210")
                    .build();
        }

        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, "Publisher not responding!");
            }
        }

        LOGGER.log(Level.INFO,loggingBody + " User successfully deleted.");

        return Response.ok(userAsString).build();
    }

}