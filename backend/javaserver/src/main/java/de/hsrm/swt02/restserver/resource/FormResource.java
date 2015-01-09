package de.hsrm.swt02.restserver.resource;

import java.util.List;
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
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.restserver.exceptions.JacksonException;

/**
 * 
 * Class enabling Clients to perform operations on Forms.
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
     * This Method grants the Clients access to all Forms stored in persistence.
     * 
     * @return All Forms in the persistence as string if successful, 500 Server Error if not
     */
    @GET
    @Path("forms")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllForms() {
        final String loggingBody = PREFIX + "GET /resource/forms";
        LOGGER.log(Level.INFO, loggingBody);
        List<Form> forms;

        forms = LOGIC.getAllForms();

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
     * This Method enables Clients to save Forms into the persistence.
     * 
     * @param formParams the form to be saved is available via key "data"
     * @return 200 OK if successful, 500 Server Error if not
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
            LOGGER.log(Level.INFO, loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode()))
                    .build();
        }

        logicResponse = LOGIC.addForm(form);


        //PUBLISHER.publishEvent(logicResponse);
        
        LOGGER.log(Level.INFO, loggingBody + " Form successfully stored.");
        return Response.ok("Form stored").build();
    }
    
    
    
}
