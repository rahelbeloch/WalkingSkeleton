package restserver.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import moduledi.SingleModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

import persistence.Persistence;

/**
 * 
 * @author akoen001
 *
 * REST Service for deleting certain data
 */
@Path("delete")
public class DeleteResource {

	Injector i = Guice.createInjector(new SingleModule());
	Persistence p = i.getInstance(Persistence.class);
	
	/**
	 * 
	 * @param workflowid
	 * @return HTTP 200, if successful
	 */
	@DELETE @Path("workflow/{workflowid}")
	public Response deleteWorkflow (@PathParam("workflowid") int workflowid) {
		System.out.println("DELETE -> " + workflowid);
		p.deleteWorkflow(workflowid);
		return Response.status(200).build();
	}
	
}
