package de.hsrm.swt02.restserver.resource;

import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.restserver.LogicResponse;
import de.hsrm.swt02.restserver.exceptions.JacksonException;

@Path("resource")
public class RoleResource {

    public static final ConstructionFactory FACTORY = ConstructionFactory.getInstance();
    public static final Logic LOGIC = FACTORY.getLogic();
    public static final ServerPublisher PUBLISHER = FACTORY.getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    public static final String PREFIX = "[restserver] ";
    LogicResponse logicResponse;
    
    @GET
    @Path("roles")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllRoles(@PathParam("username") String username) {
        final String loggingBody = PREFIX + "GET /resource/roles " + username;
        LOGGER.log(Level.INFO, loggingBody);
        final ObjectMapper mapper = new ObjectMapper();
        List<Role> roles;
            try {
                roles = LOGIC.getAllRoles();
            } catch (RoleNotExistentException e) {
                LOGGER.log(Level.INFO, e);
                return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
            }
        String rolesAsString;
        
        try {
            rolesAsString = mapper.writeValueAsString(roles);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.INFO, e);
            return Response.serverError().entity(String.valueOf(new JacksonException().getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(rolesAsString).build();
    }
    
    
    
}
