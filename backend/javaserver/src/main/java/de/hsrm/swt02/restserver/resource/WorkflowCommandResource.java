package de.hsrm.swt02.restserver.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hsrm.swt02.businesslogic.LogicImp;

@Path("command/workflow")
public class WorkflowCommandResource {

    public static final LogicImp logic = LFFactory.getLogic();
    public static final ServerPublisherImp publisher = LFFactory.getPublisher();
    
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
        logic.startWorkflow(workflowid, logic.getUser(username));
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
        //TODO: perform action in logic
        return Response.ok().build();
    }

}
