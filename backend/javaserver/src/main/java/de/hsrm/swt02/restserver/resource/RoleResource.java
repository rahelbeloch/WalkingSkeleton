package de.hsrm.swt02.restserver.resource;

import java.io.IOException;
import java.util.List;
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
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.persistence.exceptions.RoleAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.restserver.LogicResponse;
import de.hsrm.swt02.restserver.Message;
import de.hsrm.swt02.restserver.exceptions.JacksonException;

@Path("resource")
public class RoleResource {

    public static final ConstructionFactory FACTORY = ConstructionFactory.getInstance();
    public static final Logic LOGIC = FACTORY.getLogic();
    public static final ServerPublisher PUBLISHER = FACTORY.getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    public static final String PREFIX = "[restserver] ";
    LogicResponse logicResponse;
    
    /**
     * 
     * This Method grants the Clients access to all Roles stored in persistence
     * 
     * @return All Roles in the persistence as string if successful, 500 Server Error if not
     */
    @GET
    @Path("roles")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllRoles() {
        final String loggingBody = PREFIX + "GET /resource/roles";
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
    
    /**
     * 
     * This Method enables Clients to save Roles into the persistence
     * 
     * @param formParams the role to be saved is available via key "data"
     * @return 200 OK if successful, 500 Server Error if not
     */
    @POST
    @Path("role")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response saveRole(MultivaluedMap<String, String> formParams) {
        final ObjectMapper mapper = new ObjectMapper();
        final String loggingBody = PREFIX + "POST /resource/role";
        LOGGER.log(Level.INFO, loggingBody);
        final String roleAsString = formParams.get("data").get(0);
        Role role;
        
        try {
            role = mapper.readValue(roleAsString, Role.class);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, loggingBody + " JACKSON parsing-error occured.");
            return Response.serverError().entity(String.valueOf(new JacksonException().getErrorCode()))
                    .build();
        }
        try {
            logicResponse = LOGIC.addRole(role);
        } catch (RoleAlreadyExistsException e1) {
            LOGGER.log(Level.INFO, e1);
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
        return Response.ok("Role stored").build();
    }
    
    /**
     * 
     * This Method enables Clients to update existing Roles
     * 
     * @param rolename the name of the Role to be updated
     * @param formParams the key "data" holds the instance of Role to be saved
     * @return 200 OK if successful, 500 Server Error if not
     */
    @PUT
    @Path("role/{rolename}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response updateRole(@PathParam("rolename") String rolename,
            MultivaluedMap<String, String> formParams) 
    {
        final String loggingBody = PREFIX + "PUT /resource/role/" + rolename;
        LOGGER.log(Level.INFO, loggingBody);
        final ObjectMapper mapper = new ObjectMapper();
        final String roleAsString = formParams.get("data").get(0);
        Role role;
        
        try {
            role = mapper.readValue(roleAsString, Role.class);
        } catch (IOException e) {
            LOGGER.log(Level.INFO,loggingBody + " JACKSON parsing-error occured.");
            return Response.serverError().build();
        }
        try {
            logicResponse = LOGIC.addRole(role);
        } catch (RoleAlreadyExistsException e1) {
            LOGGER.log(Level.INFO, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }
            
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, e);
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " Role successfully updated.");
        return Response.ok().build();
    }
    
    /**
     * 
     * This method enables Clients to delete Roles from the persistence
     * 
     * @param rolename the name of the role to be deleted
     * @return 200 OK if successful, 500 ServerError if not
     */
    @DELETE
    @Path("role/{rolename}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUser(@PathParam("rolename") String rolename) {
        final String loggingBody = PREFIX + "DELETE /resource/role/" + rolename;
        LOGGER.log(Level.INFO, loggingBody);
        
        try {
            // TODO: get Role from logic, serialize it and send it with answer
            logicResponse = LOGIC.deleteRole(rolename);
        } catch (RoleNotExistentException e1) {
            LOGGER.log(Level.INFO,e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }
        
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, e);
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " User successfully deleted.");
        return Response.ok().build();
    }
    
}
