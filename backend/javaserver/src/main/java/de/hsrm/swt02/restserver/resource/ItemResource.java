package de.hsrm.swt02.restserver.resource;

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
import de.hsrm.swt02.businesslogic.LogicResponse;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.restserver.exceptions.JacksonException;

/**
 * 
 * Class enabling Clients to perform operations on items
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
    LogicResponse logicResponse;
    
    /**
     * 
     * This Method grants the Clients access to all Roles stored in persistence
     * 
     * @return All Roles in the persistence as string if successful, 500 Server Error if not
     */
    @GET
    @Path("item/{itemid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getItemById(@PathParam("itemid") int itemid) {
        final String loggingBody = PREFIX + "GET /item/" + itemid;
        LOGGER.log(Level.INFO, loggingBody);
        final ObjectMapper mapper = new ObjectMapper();
        Item item = null;
        //TODO: get item from logic
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
