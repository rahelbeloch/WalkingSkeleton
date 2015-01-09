package de.hsrm.swt02.restserver.resource;

import java.io.IOException;
import java.util.logging.Level;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;

/**
 * 
 * Filter class managing every Request on the REST API and checking it.
 * 
 * @author akoen001
 *
 */
public class CheckLoginFilter implements ContainerRequestFilter {

    /**
     * Checks if the user is authorized for the requested action. Will continue as normal if that is the case, abort
     * the request with 500 Server Error if not.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final ConstructionFactory factory = ConstructionFactory.getInstance();
        final Logic logic = factory.getLogic();
        final UseLogger logger = new UseLogger();

        final String username = requestContext.getHeaderString("username");
        final String password = requestContext.getHeaderString("password");
        final String clientID = requestContext.getHeaderString("clientID");
        
        try {
        	System.out.println("username: " + username + ", password: " + password + ", clientID: " + clientID);
            logic.checkLogIn(username,password,"admin".equals(clientID));
        } catch (LogicException e2) {
            logger.log(Level.WARNING, e2);
            requestContext.abortWith(Response.serverError().entity(String.valueOf(e2.getErrorCode())).build());
        }
    }
}