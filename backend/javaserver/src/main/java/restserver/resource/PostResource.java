package restserver.resource;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import manager.ProcessManager;
import messaging.ServerPublisher;
import moduledi.SingleModule;
import backingbeans.Item;
import backingbeans.User;
import backingbeans.Workflow;

import com.google.inject.Guice;
import com.google.inject.Injector;

import abstractbeans.AbstractStep;
import abstractbeans.AbstractUser;
import abstractbeans.AbstractWorkflow;
import persistence.Persistence;
import processors.StartTrigger;

/**
 * 
 * @author akoen001
 *
 * REST Service, storing given information in the database
 */
@Path("post")
public class PostResource {
	
	Injector i = Guice.createInjector(new SingleModule());
	
	ServerPublisher sp = i.getInstance(ServerPublisher.class);
	Persistence p = i.getInstance(Persistence.class);
	ProcessManager pm = i.getInstance(ProcessManager.class);
	
	/**
	 * 
	 * receives a workflow and stores it into the database
	 * 
	 * @param receivedWorkflow
	 * @return Response if storage was successful
	 */
	@POST @Path("send/workflow")
	public Response saveWorkflow (@BeanParam AbstractWorkflow receivedWorkflow) {
		System.out.println("SEND -> " + receivedWorkflow.getId());
		p.storeWorkflow(receivedWorkflow);
		return Response.status(200).build();
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param workflowid
	 * @param userid
	 * @return HTTP 200 OK if successful
	 */
	@POST @Path("start/{workflowid}/{userid}")
	public Response startWorkflow (@PathParam("workflowid") int workflowid, @PathParam("userid") int userid) {
		Workflow workflow = (Workflow)p.loadWorkflow(workflowid);
		StartTrigger start = new StartTrigger(workflow, pm, p);
		start.startWorkflow();
		Item item = (Item)workflow.getItemByPos(0);
		System.out.println("START -> " + workflowid + " " + item.getState());
		return Response.status(200).build();
	}
	
	/**
	 * 
	 * @param stepid
	 * @param itemid
	 * @param username
	 * @return HTTP 200 OK if successful
	 */
	@POST @Path("forward/{stepid}/{itemid}/{username}")
	public Response forward (@PathParam("stepid") int stepid, @PathParam("itemid") int itemid,
							 @PathParam("username") String username) {
		AbstractStep step = p.loadStep(stepid);
		Item item = (Item)p.loadItem(itemid);
		AbstractUser user = p.loadUser(username);
		pm.selectProcessor(step, item, (User)user);
		System.out.println("FORWARD -> " + itemid + " " + item.getState());
		return Response.status(200).build();
	}
	
	@POST @Path("simple")
    @Produces(MediaType.TEXT_HTML)
    public String getHelpAsHTML() {
    	return "<html>" +
    			"<head><title>GeoInfos</title></head>" +
    			"<body><h1>Info</h1>" +
    			"Dies ist eine <strong>coole</strong>Anwendung." +
    			"</body></html>";
    }
	
}
