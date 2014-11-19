package restserver.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import persistence.Persistence;
import abstractbeans.AbstractWorkflow;


/**
 * 
 * @author akoen001
 *
 * REST Service, providing workflows selected by ID
 */
@Path("items")
public class RequestResource {
	
	Persistence db = new Persistence();
	AbstractWorkflow workflow = new AbstractWorkflow();
	
	/**
	 * 
	 * @param workflowid
	 * @return the requested workflow
	 */
	@GET @Path("workflow/{workflowid}")
	@Produces(MediaType.APPLICATION_XML)
	public AbstractWorkflow getWorkflowAsXML (@PathParam("workflowid") int workflowid) {
		workflow.setId(17);
		db.storeWorkflow(workflow);
		System.out.println("GET ->" + workflowid);
		return db.loadWorkflow(workflowid);
	}
}