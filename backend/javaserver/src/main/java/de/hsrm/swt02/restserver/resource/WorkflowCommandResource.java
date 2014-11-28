package de.hsrm.swt02.restserver.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("command")
public class WorkflowCommandResource {

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
        // Workflow workflow = (Workflow)p.loadWorkflow(workflowid);
        // pm.startBroker();
        // StartTrigger start = new StartTrigger(workflow, pm, p);
        // start.startWorkflow();
        // Item item = (Item)workflow.getItemByPos(0);
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
        // AbstractStep step = p.loadStep(stepid);
        // Item item = (Item)p.loadItem(itemid);
        // AbstractUser user = p.loadUser(username);
        // pm.selectProcessor(step, item, (User)user);
        return Response.ok().build();
    }

}
