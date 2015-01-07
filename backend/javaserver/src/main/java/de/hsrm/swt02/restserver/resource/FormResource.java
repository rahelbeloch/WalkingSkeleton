package de.hsrm.swt02.restserver.resource;

import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.LogicResponse;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.restserver.exceptions.JacksonException;

/**
 * 
 * @author akoen001
 * Resource class for forms, allowing the clients to perform crud operations on forms
 *
 */
@Path("resource")
public class FormResource {

    public static final ConstructionFactory FACTORY = ConstructionFactory
            .getInstance();
    public static final Logic LOGIC = FACTORY.getLogic();
    public static final ServerPublisher PUBLISHER = FACTORY.getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    private static final String PREFIX = "[restserver] ";
    
    /**
     * This method returns all forms currently stored in the database.
     * @return all forms currently stored in the database.
     */
    @GET
    @Path("forms")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getForms() {
        final String loggingBody = PREFIX + "GET /resource/forms";
        LOGGER.log(Level.INFO, loggingBody);
        String fListAsString;
        List<Form> formList = null;


        try {
            formList = LOGIC.getAllForms();
        } catch (PersistenceException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }

        try {
            fListAsString = JsonParser.marshall(formList);
            LOGGER.log(Level.FINE, loggingBody + fListAsString);
        } catch (JacksonException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response
                    .serverError()
                    .entity(String.valueOf(e
                            .getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(fListAsString).build();
    }
    
    /**
     * Receives a form and stores it into the database. This operation will
     * be published on the message broker.
     * @param formParams wrapper for a sent form
     * @return "true" if everything was successful OR "jackson exception" if
     *         serialization crashed
     */
    @POST
    @Path("forms")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response saveForm(MultivaluedMap<String, String> formParams) {
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "POST /resource/forms";
        LOGGER.log(Level.INFO, loggingBody);
        final String formAsString = formParams.get("data").get(0);
        Form form = new Form();

        try {
            form = (Form)JsonParser.unmarshall(formAsString, form);
        } catch (JacksonException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response
                    .serverError()
                    .entity(String.valueOf(e
                            .getErrorCode())).build();
        }
        try {
            logicResponse = LOGIC.addForm(form);
        } catch (LogicException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError()
                    .entity(String.valueOf(e1.getErrorCode())).build();
        }
        
        PUBLISHER.publishEvent(logicResponse);
        
        LOGGER.log(Level.INFO, loggingBody + " Form successfully saved.");
        return Response.ok().build();
    }
    
}
