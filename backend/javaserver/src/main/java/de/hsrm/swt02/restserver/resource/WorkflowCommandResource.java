package de.hsrm.swt02.restserver.resource;

import java.util.logging.Level;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;
import de.hsrm.swt02.restserver.LogicResponse;
import de.hsrm.swt02.restserver.Message;

/**
 * This class is called if client sends request to operate on workflows.
 *
 */
@Path("command/workflow")
public class WorkflowCommandResource {

    public static final Logic LOGIC = ConstructionFactory.getLogic();
    public static final ServerPublisher PUBLISHER = ConstructionFactory
            .getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    LogicResponse logicResponse;

    /**
     * This method executes the request to start an existing workflow.
     * 
     * @param workflowid states which workflow should be started
     * @param username is for checking if user is authorized to execute this request
     * @return true or false as String
     */
    @POST
    @Path("start/{workflowid}/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response startWorkflow(@PathParam("workflowid") int workflowid,
            @PathParam("username") String username) 
    {

        final String loggingBody = "START -> " + workflowid + " " + username;

        try {
            LOGIC.startWorkflow(workflowid, username);
        } catch (WorkflowNotExistentException e) {
            LOGGER.log(Level.INFO, loggingBody + " Workflow does not exist.");
            Response.serverError().entity("11250").build();
        }
     // !! Must be yet tested!!
        logicResponse = LOGIC.getProcessLogicResponse();
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, "Publisher not responding!");
            }
        }
        LOGIC.setProcessLogicResponse(new LogicResponse());
        LOGGER.log(Level.INFO, loggingBody + " Workflow started.");
        return Response.ok().build();
    }

    /**
     * This method sets steps in progress. 
     * 
     * @param stepid indicates which step is currently worked on
     * @param itemid indicates which item is currently worked on
     * @param username is for checking if user is authorized to execute this request
     * @return true or false as String
     */
    @POST
    @Path("forward/{stepid}/{itemid}/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response forward(@PathParam("stepid") int stepid,
            @PathParam("itemid") int itemid,
            @PathParam("username") String username) 
    {
        final String loggingBody = "FORWARD -> " + itemid;
        try {
            LOGIC.stepForward(itemid, stepid, username);
        } catch (ItemNotExistentException e) {
            LOGGER.log(Level.INFO, loggingBody + " Item does not exist.");
            return Response.serverError().entity("11250").build();
        } catch (UserNotExistentException e) {
            LOGGER.log(Level.INFO, loggingBody + " User does not exist.");
            return Response.serverError().entity("11260").build();
        }
     // !! Must be yet tested!!
        logicResponse = LOGIC.getProcessLogicResponse();
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, "Publisher not responding!");
            }
        }
        LOGIC.setProcessLogicResponse(new LogicResponse());
        LOGGER.log(Level.INFO, loggingBody + " Worfklow forwarded.");
        return Response.ok().build();
    }

//    /**
//     * This method executes the request of closing a step within an item.
//     * 
//     * @param stepId indicates which step is currently worked on
//     * @param itemId indicates which item is currently worked on
//     * @param username is for checking if user is even authorized to execute this request
//     * @return Response if it worked or not
//     */
//    @POST
//    @Path("finish/{stepid}/{itemid}/{username}")
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response finish(@PathParam("stepid") int stepId,
//            @PathParam("itemid") int itemId,
//            @PathParam("username") String username) 
//    {
//        final String loggingBody = "FINISH -> " + itemId;
//        try {
//            LOGIC.stepFinished(itemId, stepId, username);
//        } catch (ItemNotExistentException e) {
//            LOGGER.log(Level.INFO, loggingBody + " Item does not exist.");
//            return Response.serverError().entity("11250").build();
//        } catch (UserNotExistentException e) {
//            LOGGER.log(Level.INFO, loggingBody + " User does not exist.");
//            return Response.serverError().entity("11260").build();
//        }
//     // !! Must be yet tested!!
//        logicResponse = LOGIC.getProcessLogicResponse();
//        for (Message m : logicResponse.getMessages()) {
//            try {
//                PUBLISHER.publish(m.getValue(), m.getTopic());
//            } catch (ServerPublisherBrokerException e) {
//                LOGGER.log(Level.WARNING, "Publisher not responding!");
//            }
//        }
//        LOGIC.setProcessLogicResponse(new LogicResponse());
//        return Response.ok().build();
//    }

}
