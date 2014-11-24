package restserver.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import abstractbeans.AbstractWorkflow;
import persistence.PersistenceImp;

/**
 * 
 * @author akoen001
 *
 * REST Service, storing given information in the database
 */
public class PostResource {
	
	PersistenceImp db = new PersistenceImp();
	
	/**
	 * 
	 * @param receivedWorkflow
	 * @return Response if storage was successful
	 */
	@POST @Path("send/workflow")
	@Consumes(MediaType.APPLICATION_XML)
	public Response saveWorkflow (AbstractWorkflow receivedWorkflow) {
		db.storeWorkflow(receivedWorkflow);
		return Response.status(201).build();
	}
	
	@POST @Path("start/{workflowid}/{userid}")
	public Response startWorkflow (@PathParam("workflowid") int workflowid, @PathParam("userid") int userid) {
		//Workflow starten
		return Response.status(201).build();
	}
	
	@POST @Path("forward/{stepid}/{itemid}/{userid}")
	public Response forward (@PathParam("stepid") int stepid, @PathParam("itemid") int itemid,
							 @PathParam("userid") int userid) {
		//Weiterschalten
		return Response.status(201).build();
	}
	
}
