package de.hsrm.swt02.restserver.resource;

import java.util.logging.Level;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.LogicResponse;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;

/**
 * This class is called if client sends request to operate on workflows.
 *
 */
@Path("command/workflows")
public class WorkflowCommandResource {

    public static final ConstructionFactory FACTORY = ConstructionFactory.getInstance();
    public static final Logic LOGIC = FACTORY.getLogic();
    public static final ServerPublisher PUBLISHER = FACTORY.getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    public static final String PREFIX = "[restserver] ";

    /**
     * This method executes the request to start an existing workflow.
     * This operation will be published on the message broker.
     * @param workflowid states which workflow should be started
     * @param username is for checking if user is authorized to execute this request
     * @return 200 if successful, 500 + error code as String if not
     */
    @POST
    @Path("start/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response startWorkflow(@PathParam("workflowid") String workflowid,
            @HeaderParam("username") String username) 
    {
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "POST /start/" + workflowid + "/" + username;
        LOGGER.log(Level.INFO, loggingBody);

        // added LogicException to the following try&catch block, should be correct. -- dneux001
        try {
            logicResponse = LOGIC.startWorkflow(workflowid, username);
        } catch (LogicException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }

        PUBLISHER.publishEvent(logicResponse);
        
        LOGGER.log(Level.INFO, loggingBody + " Workflow started.");
        return Response.ok().build();
    }

    /**
     * This method sets steps in progress. 
     * This operation will be published on the message broker.
     * @param stepid indicates which step is currently worked on
     * @param itemid indicates which item is currently worked on
     * @param username is for checking if user is authorized to execute this request
     * @return 200 if successful, 500 + error code as String if not
     */
    @POST
    @Path("forward/{stepid}/{itemid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response forward(@PathParam("stepid") String stepid,
            @PathParam("itemid") String itemid,
            @HeaderParam("username") String username) 
    {
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "POST /forward/" + stepid + "/" + itemid + "/" + username;
        LOGGER.log(Level.INFO, loggingBody);
        
        try {
            logicResponse = LOGIC.stepForward(itemid, stepid, username);
        } catch (LogicException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }

        PUBLISHER.publishEvent(logicResponse);
        
        LOGGER.log(Level.INFO, loggingBody + " Worfklow forwarded.");
        return Response.ok().build();
    }
}
