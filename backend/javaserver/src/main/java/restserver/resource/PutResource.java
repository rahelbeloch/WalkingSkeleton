package restserver.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import moduledi.SingleModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

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

	Injector i = Guice.createInjector(new SingleModule());
	Persistence p = i.getInstance(Persistence.class);
	
	/**
	 * 
	 * @param workflow
	 * @return 
	 */
	@PUT @Path("workflow")
	@Consumes(MediaType.APPLICATION_XML)
	public Response updateWorkflow(@PathParam("workflow") AbstractWorkflow workflow) {
		System.out.println("UPDATE -> " + workflow.getId());
		p.storeWorkflow(workflow);
		return Response.status(201).build();
	}
	
}