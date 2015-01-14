package de.hsrm.swt02.restserver.resource;

import java.util.logging.Level;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.NoPermissionException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
import de.hsrm.swt02.restserver.exceptions.JacksonException;

/**
 * 
 * Class enabling Clients to perform operations on items.
 * 
 * @author akoen001
 *
 */
@Path("resource")
public class ItemResource {

    public static final ConstructionFactory FACTORY = ConstructionFactory.getInstance();
    public static final Logic LOGIC = FACTORY.getLogic();
    public static final ServerPublisher PUBLISHER = FACTORY.getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    public static final String PREFIX = "[restserver] ";
    
    /**
     * This Method grants the Clients access to an Item stored in persistence via the itemid.
     * @param itemid the id of the item
     * @param username the user requesting the service
     * @return the item as string if successful, else an exception
     */
    @GET
    @Path("items/{itemid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getItemById(@PathParam("itemid") String itemid, @HeaderParam("username") String username) {
        final String loggingBody = PREFIX + "GET /items/" + itemid;
        
        LOGGER.log(Level.INFO, loggingBody);
        final ObjectMapper mapper = new ObjectMapper();
        Item item = null;
        try {
            item = LOGIC.getItem(itemid, username);
        } catch (LogicException e) {
            LOGGER.log(Level.INFO, loggingBody);
            LOGGER.log(Level.WARNING, e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        }
        String itemAsString;
        
        try {
            itemAsString = mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.INFO, e);
            return Response.serverError().entity(String.valueOf(new JacksonException().getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(itemAsString).build();
    }
    
}
