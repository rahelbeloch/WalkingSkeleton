package restserver.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import beans.Workflow;

@Path("send")
public class PostResource {
	
	@POST @Path("workflow")
	@Consumes(MediaType.APPLICATION_XML)
	public Workflow saveWorkflow (Workflow receivedWorkflow) {
		return receivedWorkflow;
	}
	
}
