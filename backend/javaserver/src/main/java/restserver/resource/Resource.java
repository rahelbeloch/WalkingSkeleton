package restserver.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.Workflow;


/**
 * 
 * @author akoen001
 *
 * REST Service, providing workflows selected by ID
 */
@Path("items")
public class Resource {
	@GET @Path("workflow/{workflowid}")
	@Produces(MediaType.APPLICATION_XML)
	/**
	 * 
	 * @param workflowid
	 * @return the requested workflow
	 */
	public Workflow getWorkflowAsXML (@PathParam("workflowid") int workflowid) {
		Workflow workflow = new Workflow();
		workflow.setId(workflowid);
		return workflow;
	}
}