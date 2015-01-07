package de.hsrm.swt02.restserver.resource;

import java.util.logging.Level;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;

/**
 * This class is called if client wants to process user information.
 *
 */
@Path("command/users")
public class UserCommandResource {
    
    public static final ConstructionFactory FACTORY = ConstructionFactory.getInstance();
    public static final Logic LOGIC = FACTORY.getLogic();
    public static final ServerPublisher PUBLISHER = FACTORY.getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    public static final String PREFIX = "[restserver] ";
    
    /**
     * This method is called if some client wants to login.
     * @param username The user to login
     * @param password The users password
     * @return returns ok if it was successful and server error if it was not
     */
    @POST @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(@HeaderParam("username") String username, @HeaderParam("password") String password) {
        final String loggingBody = PREFIX + "POST /command/user/login";
        LOGGER.log(Level.INFO, loggingBody);
        try {
            LOGIC.checkLogIn(username,password,false);
        } catch (LogicException e) {
            LOGGER.log(Level.WARNING,e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        }
        LOGGER.log(Level.INFO,loggingBody + " Login successful.");
        return Response.ok().build();
    }
}
