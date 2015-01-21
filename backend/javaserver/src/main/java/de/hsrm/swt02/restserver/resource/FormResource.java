package de.hsrm.swt02.restserver.resource;

import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.restserver.exceptions.JacksonException;

/**
 * 
 * Class enabling clients to perform operations on forms.
 * @author akoen001
 *
 */
@Path("resource")
public class FormResource {

    public static final ConstructionFactory FACTORY = ConstructionFactory.getInstance();
    public static final Logic LOGIC = FACTORY.getLogic();
    public static final ServerPublisher PUBLISHER = FACTORY.getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    public static final String PREFIX = "[restserver] ";
    
    /**
     * 
     * This method grants the clients access to all forms stored in persistence.
     * @param username the user requesting the service
     * @return All forms in the persistence as string if successful, 500 server error if not
     * @throws PersistenceException 
     */
    @GET
    @Path("forms")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllForms(@HeaderParam("username") String username) {
        
        try {
            LOGIC.checkUserIsAdmin(username);
        } catch (LogicException e2) {
            LOGGER.log(Level.INFO, e2);
            return Response.serverError().entity(String.valueOf(e2.getErrorCode())).build();
        }
        
        final String loggingBody = PREFIX + "GET /resource/forms";
        LOGGER.log(Level.INFO, loggingBody);
        List<Form> forms;

        try {
            forms = LOGIC.getAllForms();
        } catch (PersistenceException e1) {
            LOGGER.log(Level.INFO, loggingBody + e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode()))
                    .build();
        }

        String formsAsString;
        
        try {
            formsAsString = JsonParser.marshall(forms);
        } catch (JacksonException e) {
            LOGGER.log(Level.INFO, e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        }
        
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(formsAsString).build();
    }
    
    /**
     * 
     * This method grants the clients access to get one specific form from the persistence.
     * @param username the user requesting the service
     * @param formid indicates which form is requested
     * @return All forms in the persistence as string if successful, 500 server error if not
     * @throws PersistenceException 
     */
    @GET
    @Path("forms/{formid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getForm(@PathParam("formid") String formid, @HeaderParam("username") String username) {
        
        try {
            LOGIC.checkUserIsAdmin(username);
        } catch (LogicException e2) {
            LOGGER.log(Level.INFO, e2);
            return Response.serverError().entity(String.valueOf(e2.getErrorCode())).build();
        }        
        
        final String loggingBody = PREFIX + "GET /resource/forms/" + formid;
        LOGGER.log(Level.INFO, loggingBody);
        Form form;

        try {
            form = LOGIC.getForm(formid);
        } catch (PersistenceException e1) {
            LOGGER.log(Level.INFO, loggingBody + e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode()))
                    .build();
        }

        String formsAsString;
        
        try {
            formsAsString = JsonParser.marshall(form);
        } catch (JacksonException e) {
            LOGGER.log(Level.INFO, e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(formsAsString).build();
    }
    
    /**
     * 
     * This method enables clients to save forms into the persistence.
     * @param username the user requesting the service
     * @param formParams the form to be saved is available via key "data"
     * @return 200 OK if successful, 500 server error if not
     */
    @POST
    @Path("forms")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response saveForm(MultivaluedMap<String, String> formParams, @HeaderParam("username") String username) {
        
        try {
            LOGIC.checkUserIsAdmin(username);
        } catch (LogicException e2) {
            LOGGER.log(Level.INFO, e2);
            return Response.serverError().entity(String.valueOf(e2.getErrorCode())).build();
        }
        
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "POST /resource/forms";
        LOGGER.log(Level.INFO, loggingBody);
        final String formAsString = formParams.get("data").get(0);
        Form form = new Form();
        
        try {
            form = (Form)JsonParser.unmarshall(formAsString, form);
        } catch (JacksonException e) {
            LOGGER.log(Level.INFO, loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode()))
                    .build();
        }

        try {
            logicResponse = LOGIC.addForm(form);
            PUBLISHER.publishEvent(logicResponse);
        } catch (PersistenceException e) {
            LOGGER.log(Level.INFO, loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode()))
                    .build();
        }
        
        LOGGER.log(Level.INFO, loggingBody + " Form successfully stored.");
        return Response.ok("Form stored").build();
    }
    
    
    /**
     * This method enables Clients to delete forms from the persistence.
     * @param formId the id of the to be deleted
     * @param username is the logged in user
     * @return 200 OK if successful, 500 ServerError if not
     */
    @DELETE
    @Path("forms/{formId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteForm(@PathParam("formId") String formId, @HeaderParam("username") String username) {
        
        try {
            LOGIC.checkUserIsAdmin(username);
        } catch (LogicException e2) {
            LOGGER.log(Level.INFO, e2);
            return Response.serverError().entity(String.valueOf(e2.getErrorCode())).build();
        }
        
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "DELETE /resource/forms/" + formId;
        LOGGER.log(Level.INFO, loggingBody);
        final Form form = null;
        String formAsString;
        
        try {
            logicResponse = LOGIC.deleteForm(formId);
        } catch (LogicException e1) {
            LOGGER.log(Level.INFO,e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }
        try {
            formAsString = JsonParser.marshall(form);
        } catch (JacksonException e) {
            LOGGER.log(Level.INFO, loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode()))
                    .build();
        }
        
        PUBLISHER.publishEvent(logicResponse);
        
        LOGGER.log(Level.INFO, loggingBody + " Form successfully deleted.");
        return Response.ok(formAsString).build();
    }
}