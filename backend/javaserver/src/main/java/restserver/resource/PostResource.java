package restserver.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import persistence.Persistence;
import beans.Workflow;

@Path("send")
public class PostResource {
	
	Persistence db = new Persistence();
	
	@POST @Path("workflow")
	@Consumes(MediaType.APPLICATION_XML)
	public Response saveWorkflow (Workflow receivedWorkflow) {
		db.storeWorkflow(receivedWorkflow);
		return Response.status(201).build();
	}
	
}
