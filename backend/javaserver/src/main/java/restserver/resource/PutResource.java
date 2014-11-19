package restserver.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import abstractbeans.AbstractWorkflow;
import persistence.Persistence;

/**
 * 
 * @author akoen001
 *
 * REST Service, updating given data
 */
@Path("update")
public class PutResource {

	Persistence db = new Persistence();
	
	/**
	 * 
	 * @param workflow
	 * @return 
	 */
	@POST @Path("workflow")
	@Consumes(MediaType.APPLICATION_XML)
	public Response updateWorkflow(@PathParam("workflow") AbstractWorkflow workflow) {
		db.storeWorkflow(workflow);
		return Response.status(201).build();
	}
	
}