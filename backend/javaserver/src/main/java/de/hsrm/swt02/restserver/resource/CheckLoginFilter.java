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
 * @author akoen001
 *
 */
public class CheckLoginFilter implements ContainerRequestFilter {

    /**
     * 
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
            logic.checkLogIn(username,password,"admin".equals(clientID));
        } catch (LogicException e2) {
            logger.log(Level.WARNING, e2);
            requestContext.abortWith(Response.serverError().entity(String.valueOf(e2.getErrorCode())).build());
        }
    }

}
