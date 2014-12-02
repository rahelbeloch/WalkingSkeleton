package de.hsrm.swt02.restserver.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;
import de.hsrm.swt02.restserver.LogicResponse;
import de.hsrm.swt02.restserver.Message;

@Path("command/workflow")
public class WorkflowCommandResource {

    public static final Logic LOGIC = ConstructionFactory.getLogic();
    public static final ServerPublisher PUBLISHER = ConstructionFactory
            .getPublisher();
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
        
        System.out.println("START -> " + workflowid + " " + username);
        try {
            LOGIC.startWorkflow(workflowid, username);
            //!! Must be yet tested!!
            logicResponse = LOGIC.getProcessLogicResponse();
            for(Message m : logicResponse.getMessages()){
                PUBLISHER.publish(m.getValue(), m.getTopic());
            }
        } catch (WorkflowNotExistentException | ServerPublisherBrokerException e) {
            // TODO use logger & return error code
            Response.status(4001).build();
        }
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
        System.out.println("FORWARD -> " + itemid);
        try {
            LOGIC.stepOver(itemid, stepid, username);
            //!! Must be yet tested!!
            logicResponse = LOGIC.getProcessLogicResponse();
            for(Message m : logicResponse.getMessages()){
                PUBLISHER.publish(m.getValue(), m.getTopic());
            }
        } catch (ItemNotExistentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UserNotExistentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServerPublisherBrokerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Response.ok().build();
    }

}
