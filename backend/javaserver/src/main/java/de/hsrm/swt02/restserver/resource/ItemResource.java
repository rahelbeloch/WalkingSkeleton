package de.hsrm.swt02.restserver.resource;

import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.LogicResponse;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.model.Item;
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
    

    /**
     * Receives an item and stores it into the database. This operation will
     * be published on the message broker.
     * @param formParams wrapper for an sent item
     * @param username indicates which user wants to update the item
     * @return "true" if everything was successful OR "jackson exception" if
     *         serialization crashed
     */
    @POST
    @Path("items")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response saveItem(MultivaluedMap<String, String> formParams, @HeaderParam("username") String username) {
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "POST /resource/items";
        LOGGER.log(Level.INFO, loggingBody);
        final String itemAsString = formParams.get("data").get(0);
        Item item = new Item();

        try {
            item = (Item)JsonParser.unmarshall(itemAsString, item);
        } catch (JacksonException e) {
            LOGGER.log(Level.INFO, loggingBody);
            LOGGER.log(Level.WARNING, e);
            return Response
                    .serverError()
                    .entity(String.valueOf(e
                            .getErrorCode())).build();
        }
        try {
            
            logicResponse = LOGIC.updateItem(item, username);
        } catch (LogicException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError()
                    .entity(String.valueOf(e1.getErrorCode())).build();
        }
        
        PUBLISHER.publishEvent(logicResponse);
        
        LOGGER.log(Level.INFO, loggingBody + " Item successfully saved.");
        return Response.ok().build();
    }
    
}
