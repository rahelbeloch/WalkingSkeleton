package restserver.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import persistence.PersistenceImp;

/**
 * 
 * @author akoen001
 *
 * REST Service for deleting certain data
 */
@Path("delete")
public class DeleteResource {

	PersistenceImp db = new PersistenceImp();
	
	/**
	 * 
	 * @param workflowid
	 * @return HTTP 200, if successful
	 */
	@DELETE @Path("workflow/{workflowid}")
	public Response deleteWorkflow (@PathParam("workflowid") int workflowid) {
		db.deleteWorkflow(workflowid);
		System.out.println("DELETE -> " + workflowid);
		return Response.status(200).build();
	}
	
}
