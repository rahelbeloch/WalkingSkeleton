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

@Path("command/workflow")
public class WorkflowCommandResource {

    public static final Logic logic = ConstructionFactory.getLogic();
    public static final ServerPublisher publisher = ConstructionFactory.getPublisher();
    public static final UseLogger logger = new UseLogger();
    LogicResponse logicResponse;

    /**
     * 
     * 
     * 
     * @param workflowid
     * @param userid
     * @return true or false as String
     */
    @POST
    @Path("start/{workflowid}/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response startWorkflow(@PathParam("workflowid") int workflowid,
            @PathParam("username") String username) {

        String loggingBody = "START -> " + workflowid + " " + username;

        try {
            logic.startWorkflow(workflowid, username);
            //!! Must be yet tested!!
            logicResponse = logic.getProcessLogicResponse();
            for(Message m : logicResponse.getMessages()){
                publisher.publish(m.getValue(), m.getTopic());
            }
        } catch (WorkflowNotExistentException | ServerPublisherBrokerException e) {
            logger.log(Level.INFO,loggingBody + " Workflow does not exist.");
            Response.serverError().entity("11250").build();
        }
        logger.log(Level.INFO, loggingBody + " Workflow started.");
        return Response.ok().build();
    }

    /**
     * 
     * @param stepid
     * @param itemid
     * @param username
     * @return true or false as String
     */
    @POST
    @Path("forward/{stepid}/{itemid}/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response forward(@PathParam("stepid") int stepid,
            @PathParam("itemid") int itemid,
            @PathParam("username") String username) {
        String loggingBody = "FORWARD -> " + itemid;
        try {
			logic.stepOver(itemid, stepid, username);
		} catch (ItemNotExistentException e) {
			logger.log(Level.INFO, loggingBody + " Item does not exist.");
			return Response.serverError().entity("11250").build();
		} catch (UserNotExistentException e) {
			logger.log(Level.INFO, loggingBody + " User does not exist.");
			return Response.serverError().entity("11260").build();
		}
        logger.log(Level.INFO, loggingBody + " Worfklow forwarded.");
        return Response.ok().build();
    }

}
