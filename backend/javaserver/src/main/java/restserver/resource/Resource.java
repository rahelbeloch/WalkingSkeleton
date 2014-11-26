package restserver.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import abstractbeans.AbstractWorkflow;

@Path("resource")
public class Resource {

	/**
	 * 
	 * @param workflowid
	 * @return the requested workflow
	 */	@GET @Path("abstractworkflow/{workflowid}")
	@Produces(MediaType.APPLICATION_XML)
	public AbstractWorkflow getWorkflowAsXML (@PathParam("workflowid") int workflowid) {
		System.out.println("GET -> " + workflowid);
		AbstractWorkflow workflow = new AbstractWorkflow();
		workflow.setId(workflowid);
		return workflow;
	}
	
	/**
	 * 
	 * @param workflowid
	 * @return the requested workflow
	 */
	@GET @Path("abstractworkflow")
	@Produces(MediaType.APPLICATION_XML)
	public String getAllWorkflows () {
		System.out.println("GETALL");
		return "alle workflows";
	}
	
	/**
	 * 
	 * receives a workflow and stores it into the database
	 * 
	 * @param receivedWorkflow
	 * @return true or false as String
	 */
	@POST @Path("abstractworkflow/{workflowid}")
	@Produces(MediaType.TEXT_XML)
	public String saveWorkflow (String a) {
		System.out.println("SEND -> " + a);
		//p.storeWorkflow(receivedWorkflow);
		return "<response>true</response>";
	}
	
	/**
	 * 
	 * @param workflow
	 * @return String true or false
	 */
	@PUT @Path("abstractworkflow/{workflowid}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateWorkflow(@PathParam("abstractworkflow") AbstractWorkflow workflow) {
		System.out.println("UPDATE -> " + workflow.getId());
		//p.storeWorkflow(workflow);
		return "true";
	}
	
	/**
	 * 
	 * @param workflowid
	 * @return deleted workflow, if successful
	 */
	@DELETE @Path("abstractworkflow/{workflowid}")
	@Produces(MediaType.APPLICATION_XML)
	public AbstractWorkflow deleteWorkflow (@PathParam("workflowid") int workflowid) {
		System.out.println("DELETE -> " + workflowid);
		AbstractWorkflow workflow = new AbstractWorkflow();
		workflow.setId(workflowid);
		return workflow;
	}
	
}
