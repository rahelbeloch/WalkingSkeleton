package restserver.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import persistence.Persistence;
import beans.Workflow;


/**
 * 
 * @author akoen001
 *
 * REST Service, providing workflows selected by ID
 */
@Path("items")
public class RequestResource {
	
	Persistence db = new Persistence();
	
	/**
	 * 
	 * @param workflowid
	 * @return the requested workflow
	 */
	@GET @Path("workflow/{workflowid}")
	@Produces(MediaType.APPLICATION_XML)
	public Workflow getWorkflowAsXML (@PathParam("workflowid") int workflowid) {
		return db.loadWorkflow(workflowid);
	}
}