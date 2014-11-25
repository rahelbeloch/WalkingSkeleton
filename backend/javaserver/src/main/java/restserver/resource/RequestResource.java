package restserver.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import moduleDI.SingleModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

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
	
	Injector i = Guice.createInjector(new SingleModule());
	Persistence p = i.getInstance(Persistence.class);
	
	/**
	 * 
	 * @param workflowid
	 * @return the requested workflow
	 */
	@GET @Path("workflow/{workflowid}")
	@Produces(MediaType.APPLICATION_XML)
	public AbstractWorkflow getWorkflowAsXML (@PathParam("workflowid") int workflowid) {
		System.out.println("GET ->" + workflowid);
		return p.loadWorkflow(workflowid);
	}
}