package restserver.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import manager.ProcessManager;
import messaging.ServerPublisher;
import moduleDI.SingleModule;
import backingbeans.Item;
import backingbeans.User;
import backingbeans.Workflow;

import com.google.inject.Guice;
import com.google.inject.Injector;

import abstractbeans.AbstractItem;
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
public class PostResource {
	
	Injector i = Guice.createInjector(new SingleModule());
	
	ServerPublisher sp = i.getInstance(ServerPublisher.class);
	Persistence p = i.getInstance(Persistence.class);
	ProcessManager pm = i.getInstance(ProcessManager.class);
	
	/**
	 * 
	 * @param receivedWorkflow
	 * @return Response if storage was successful
	 */
	@POST @Path("send/workflow")
	@Consumes(MediaType.APPLICATION_XML)
	public Response saveWorkflow (AbstractWorkflow receivedWorkflow) {
		p.storeWorkflow(receivedWorkflow);
		return Response.status(201).build();
	}
	
	@POST @Path("start/{workflowid}/{userid}")
	public Response startWorkflow (@PathParam("workflowid") int workflowid, @PathParam("userid") int userid) {
		Workflow workflow = (Workflow)p.loadWorkflow(workflowid);
		StartTrigger start = new StartTrigger(workflow, pm, p);
		start.startWorkflow();
		return Response.status(200).build();
	}
	
	@POST @Path("forward/{stepid}/{itemid}/{username}")
	public Response forward (@PathParam("stepid") int stepid, @PathParam("itemid") int itemid,
							 @PathParam("username") String username) {
		AbstractStep step = p.loadStep(stepid);
		AbstractItem item = p.loadItem(itemid);
		AbstractUser user = p.loadUser(username);
		pm.selectProcessor(step, (Item)item, (User)user);
		return Response.status(200).build();
	}
	
}
