package de.hsrm.swt02.restserver.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hsrm.swt02.businesslogic.Logic;
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
     * @return returns ok if it was successful and server error if it was not
     */
    @POST @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    public Response login() {
        return Response.ok().build();
    }
}
