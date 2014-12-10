package de.hsrm.swt02.restserver.resource;

import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;

/**
 * This class is called if client wants to process user information.
 *
 */
@Path("command/user")
public class UserCommandResource {
    
    public static final ConstructionFactory FACTORY = ConstructionFactory.getInstance();
    public static final Logic LOGIC = FACTORY.getLogic();
    public static final ServerPublisher PUBLISHER = FACTORY.getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    
    /**
     * This method is called if some client wants to login.
     * @param formParams is a wrapper for login data
     * @return returns ok if it was succesful and server error if it was not
     */
    @POST @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response login(MultivaluedMap<String, String> formParams) {
        final String username = formParams.get("username").get(0);
        final String password = formParams.get("password").get(0);
        final String loggingBody = "LOGIN -> " + username + " : " + password;
        try {
            if (LOGIC.checkLogIn(username)) {
                LOGGER.log(Level.INFO,loggingBody + " Login successful.");
                return Response.ok().build();
            } else {
                LOGGER.log(Level.INFO,loggingBody + " Login failed.");
                //TODO: remove Hardcoded errorcode
                return Response.serverError().entity("11120").build();
            }
        } catch (UserNotExistentException e) {
            LOGGER.log(Level.WARNING,e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        }
    }
}
