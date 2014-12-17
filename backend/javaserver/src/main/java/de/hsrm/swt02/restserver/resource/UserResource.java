package de.hsrm.swt02.restserver.resource;

import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.LogicResponse;
import de.hsrm.swt02.businesslogic.Message;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.restserver.exceptions.JacksonException;

/**
 * This class is called if client requests user operations.
 *
 */
@Path("resource")
public class UserResource {

    public static final ConstructionFactory FACTORY = ConstructionFactory.getInstance();
    public static final Logic LOGIC = FACTORY.getLogic();
    public static final ServerPublisher PUBLISHER = FACTORY.getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    private static final String PREFIX = "[restserver] ";
    
    /**
     * This method returns a requested user.
     * @param username indicates which user is looked for
     * @return the requested user
     * @throws UserNotExistentException 
     */
    @GET
    @Path("users/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getUser(@PathParam("username") String username) {
        final String loggingBody = PREFIX + "GET /resource/users/" + username;
        LOGGER.log(Level.INFO, loggingBody);
        User user;
        try {
            user = LOGIC.getUser(username);
        } catch (UserNotExistentException e1) {
            LOGGER.log(Level.INFO, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }
        String userAsString;
        
        try {
            userAsString = JsonParser.marshall(user);
        } catch (JacksonException e) {
            LOGGER.log(Level.INFO, e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(userAsString).build();
    }

    /**
     * Receives a user and stores it into the database. 
     * This operation will be published on the message broker.
     * @param formParams is a wrapper for a sent user
     * @return 200 ok if successful
     */
    @POST
    @Path("users")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response saveUser(MultivaluedMap<String, String> formParams) {
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "POST /resource/users";
        LOGGER.log(Level.INFO, loggingBody);
        final String userAsString = formParams.get("data").get(0);
        User user = new User();
        try {
            user = (User)JsonParser.unmarshall(userAsString, user);
        } catch (JacksonException e) {
            LOGGER.log(Level.INFO, loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode()))
                    .build();
        }
        try {
            logicResponse = LOGIC.addUser(user);
        } catch (PersistenceException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }

        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, e);
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " User successfully stored.");
        return Response.ok("User stored").build();
    }

    /**
     * This method updates a user.
     * This operation will be published on the message broker.
     * @param username indicates which user should be updated
     * @param formParams is a wrapper of a sent user 
     * @return 200 ok if successful
     */
    @PUT
    @Path("users/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response updateUser(@PathParam("username") String username,
            MultivaluedMap<String, String> formParams) 
    {
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "PUT /resource/users/" + username;
        LOGGER.log(Level.INFO, loggingBody);
        final String userAsString = formParams.get("data").get(0);
        User user = new User();
        
        try {
            user = (User)JsonParser.unmarshall(userAsString, user);
        } catch (JacksonException e) {
            LOGGER.log(Level.INFO,loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        }
        try {
            logicResponse = LOGIC.addUser(user);
        } catch (PersistenceException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }

        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, e);
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " User successfully updated.");
        return Response.ok().build();
    }

    /**
     * This method deletes an user.
     * This operation will be published on the message broker.
     * @param username indicates which user should be deleted
     * @return deleted user, if successful
     */
    @DELETE
    @Path("users")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUser(@HeaderParam("username") String username) {
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "DELETE /resource/users/" + username;
        LOGGER.log(Level.INFO, loggingBody);
        User user = null;
        String userAsString;
        
        try {
            user = LOGIC.getUser(username);
            logicResponse = LOGIC.deleteUser(username);
        } catch (UserNotExistentException e1) {
            LOGGER.log(Level.INFO,e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }
        try {
            userAsString = JsonParser.marshall(user);
        } catch (JacksonException e) {
            LOGGER.log(Level.INFO, loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode()))
                    .build();
        }
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, e);
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " User successfully deleted.");
        return Response.ok(userAsString).build();
    }
    
    /**
     * This Method grants the Clients access to all users stored in persistence.
     * @return All Users in the persistence as string if successful, 500 Server Error if not
     */
    @GET
    @Path("users")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllUsers() {
        final String loggingBody = PREFIX + "GET /resource/users";
        LOGGER.log(Level.INFO, loggingBody);
        List<User> users;
        try {
            users = LOGIC.getAllUsers();
        } catch (UserNotExistentException e) {
            LOGGER.log(Level.INFO, e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        }
        String usersAsString;
        
        try {
            usersAsString = JsonParser.marshall(users);
        } catch (JacksonException e) {
            LOGGER.log(Level.INFO, e);
            return Response.serverError().entity(String.valueOf(new JacksonException().getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(usersAsString).build();
    }

}