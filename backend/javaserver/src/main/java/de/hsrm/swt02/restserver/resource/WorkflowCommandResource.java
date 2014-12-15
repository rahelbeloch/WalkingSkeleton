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
import de.hsrm.swt02.businesslogic.Message;
import de.hsrm.swt02.businesslogic.exceptions.ItemNotForwardableException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

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
    LogicResponse logicResponse;

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
    public Response startWorkflow(@PathParam("workflowid") int workflowid,
            @HeaderParam("username") String username) 
    {
        final String loggingBody = PREFIX + "POST /start/" + workflowid + "/" + username;
        LOGGER.log(Level.INFO, loggingBody);

        try {
            LOGIC.startWorkflow(workflowid, username);
        } catch (WorkflowNotExistentException e) {
            LOGGER.log(Level.WARNING, e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        } catch (StorageFailedException e) {
            LOGGER.log(Level.WARNING, e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        } catch (ItemNotExistentException e) {
            LOGGER.log(Level.WARNING, e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        }
        logicResponse = LOGIC.getProcessLogicResponse();
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, e);
            }
        }
        LOGIC.setProcessLogicResponse(new LogicResponse());
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
    public Response forward(@PathParam("stepid") int stepid,
            @PathParam("itemid") int itemid,
            @HeaderParam("username") String username) 
    {
        final String loggingBody = PREFIX + "POST /forward/" + stepid + "/" + itemid + "/" + username;
        LOGGER.log(Level.INFO, loggingBody);
        
        try {
            LOGIC.stepForward(itemid, stepid, username);
        } catch (ItemNotExistentException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        } catch (UserNotExistentException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        } catch (ItemNotForwardableException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        } catch (UserHasNoPermissionException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        } catch (StorageFailedException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        }
        logicResponse = LOGIC.getProcessLogicResponse();
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, e);
            }
        }
        LOGIC.setProcessLogicResponse(new LogicResponse());
        LOGGER.log(Level.INFO, loggingBody + " Worfklow forwarded.");
        return Response.ok().build();
    }
}
